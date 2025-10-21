package pl.oskarinio.yourturnhomm.infrastructure.config.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.util.WebUtils;
import pl.oskarinio.yourturnhomm.app.technology.communication.CookieHelper;
import pl.oskarinio.yourturnhomm.domain.model.Route;
import pl.oskarinio.yourturnhomm.domain.model.user.User;
import pl.oskarinio.yourturnhomm.domain.model.user.UserServiceData;
import pl.oskarinio.yourturnhomm.domain.port.out.Token;
import pl.oskarinio.yourturnhomm.domain.port.out.UserRepository;
import pl.oskarinio.yourturnhomm.infrastructure.config.TestWebUtilities;

import java.io.IOException;
import java.time.Clock;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static pl.oskarinio.yourturnhomm.domain.model.user.Role.ROLE_USER;

@ExtendWith({MockitoExtension.class})
class RefreshFilterTest {
    @Mock
    private Token token;
    @Mock
    private CookieHelper cookieHelperService;
    @Mock
    private Clock clock;
    @Mock
    private UserRepository userRepository;

    private static final String PUBLIC_PATH = Route.MAIN + Route.REGISTER;
    private static final String UNPUBLIC_PATH = Route.MAIN;
    private static final String COOKIE_ACCESS_TOKEN = "accessToken";
    private static final String COOKIE_REFRESH_TOKEN = "refreshToken";
    private static final String ACCESS_TOKEN_VALUE = "accessTokenValue";
    private static final String REFRESH_TOKEN_VALUE = "refreshTokenValue";
    private static final String FALSE_TOKEN_VALUE = "falseTokenValue";
    private static final String USERNAME = "testUsername";
    private static final String PASSWORD = "1234";
    private static final Instant INSTANT = Instant.parse("2025-10-06T10:15:30.00Z");

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private FilterChain filterChain;
    private RefreshFilter refreshFilter;

    @BeforeEach
    void SetUp(){
        refreshFilter = new RefreshFilter(token, userRepository, cookieHelperService, clock);

        TestWebUtilities webUtilities = new TestWebUtilities();
        request = webUtilities.getRequest();
        response =  webUtilities.getResponse();
        filterChain = webUtilities.getFilterChain();
    }

    @Test
    @DisplayName("Publiczna sciezka, metoda nic nie robi")
    void doFilterInternal_publcPath_nothingHappened() throws ServletException, IOException {
        setRequestPathWithDetails(PUBLIC_PATH, false);

        refreshFilter.doFilterInternal(request,response,filterChain);

        verify(token, never()).isTokenExpiredSafe(any());
    }

    @Test
    @DisplayName("Niepubliczna sciezka, accessToken poprawny, metoda nic nie robi")
    void doFilterInternal_accessTokenCorrect_nothingHappened() throws ServletException, IOException {
        setRequestPathWithDetails(UNPUBLIC_PATH, true);

        when(token.isTokenExpiredSafe(ACCESS_TOKEN_VALUE)).thenReturn(false);
        refreshFilter.doFilterInternal(request,response,filterChain);

        verify(cookieHelperService, never()).setCookieTokens(any(), any());
        String futureAccessToken = Objects.requireNonNull(WebUtils.getCookie(request, COOKIE_ACCESS_TOKEN)).getValue();
        assertThat(futureAccessToken).isEqualTo(ACCESS_TOKEN_VALUE);
    }

    @Test
    @DisplayName("Niepubliczna ścieżka, accessToken null, refreshToken null, metoda czysci cookies")
    void doFilterInternal_tokenRefreshNullAccessNull_clearCookies() throws ServletException, IOException {
        setRequestPathWithDetails(UNPUBLIC_PATH, false);

        refreshFilter.doFilterInternal(request,response,filterChain);

        verify(cookieHelperService).clearCookies(response, request);
    }

    @Test
    @DisplayName("Niepubliczna ścieżka, accessToken niepoprawny, refreshToken null, metoda czysci cookies")
    void doFilterInternal_tokenRefreshNullAccessIncorrect_clearCookies() throws ServletException, IOException {
        setRequestPathWithDetails(UNPUBLIC_PATH, false);
        setCookies(request, FALSE_TOKEN_VALUE, null);

        when(token.isTokenExpiredSafe(FALSE_TOKEN_VALUE)).thenReturn(true);
        refreshFilter.doFilterInternal(request,response,filterChain);

        verify(cookieHelperService).clearCookies(response, request);
    }

    @Test
    @DisplayName("Niepubliczna ścieżka, accessToken null, refreshToken niepoprawny, metoda czysci cookies")
    void doFilterInternal_tokenRefreshIncorrectAccessNull_clearCookies() throws ServletException, IOException {
        setRequestPathWithDetails(UNPUBLIC_PATH, false);
        setCookies(request, null, FALSE_TOKEN_VALUE);

        when(token.isTokenExpiredSafe(FALSE_TOKEN_VALUE)).thenReturn(true);
        refreshFilter.doFilterInternal(request,response,filterChain);

        verify(cookieHelperService).clearCookies(response, request);
    }

    @Test
    @DisplayName("Niepubliczna ścieżka, accessToken niepoprawny, refreshToken niepoprawny, metoda czysci cookies")
    void doFilterInternal_tokenRefreshIncorrectAccessIncorrect_clearCookies() throws ServletException, IOException {
        setRequestPathWithDetails(UNPUBLIC_PATH, false);
        setCookies(request, FALSE_TOKEN_VALUE, FALSE_TOKEN_VALUE);

        when(token.isTokenExpiredSafe(FALSE_TOKEN_VALUE)).thenReturn(true);
        refreshFilter.doFilterInternal(request,response,filterChain);

        verify(cookieHelperService).clearCookies(response, request);
    }

    @Test
    @DisplayName("Niepubliczna ścieżka, accessToken niepoprawny, refreshToken poprawny, użytkownik niepoprawny, metoda czysci cookies")
    void doFilterInternal_userIncorrect_clearCookies() throws ServletException, IOException {
        setRequestPathWithDetails(UNPUBLIC_PATH, false);
        setCookies(request, null, REFRESH_TOKEN_VALUE);

        when(token.extractUsername(REFRESH_TOKEN_VALUE)).thenReturn(USERNAME);
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());
        refreshFilter.doFilterInternal(request,response,filterChain);

        verify(cookieHelperService).clearCookies(response, request);
    }

    @Test
    @DisplayName("Niepubliczna ścieżka, accessToken niepoprawny, refreshToken poprawny, użytkownik poprawny, metoda ustawia nowy accessToken")
    void doFilterInternal_correctValues_setAccessToken() throws ServletException, IOException {
        setRequestPathWithDetails(UNPUBLIC_PATH, false);
        setCookies(request, null, REFRESH_TOKEN_VALUE);
        User user = getUser();

        when(clock.instant()).thenReturn(INSTANT);
        when(token.extractUsername(REFRESH_TOKEN_VALUE)).thenReturn(USERNAME);
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(token.generateToken(any(UserServiceData.class), anyLong())).thenReturn(ACCESS_TOKEN_VALUE);
        refreshFilter.doFilterInternal(request,response,filterChain);

        assertThat(request.getAttribute(COOKIE_ACCESS_TOKEN)).isEqualTo(ACCESS_TOKEN_VALUE);
    }

    private void setRequestPathWithDetails(String pathAccessLevel, boolean whetherSetCookies){
        request.setRequestURI(pathAccessLevel);
        if(whetherSetCookies)
            setCookies(request,ACCESS_TOKEN_VALUE,REFRESH_TOKEN_VALUE);
    }

    private void setCookies(MockHttpServletRequest request, String accessTokenValue, String refreshTokenValue){
        Cookie cookieAccess = new Cookie(COOKIE_ACCESS_TOKEN, accessTokenValue);
        Cookie cookieReresh = new Cookie(COOKIE_REFRESH_TOKEN, refreshTokenValue);
        request.setCookies(cookieAccess, cookieReresh);
    }

    private User getUser(){
        User user = new User();
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(ROLE_USER));
        return user;
    }
}
