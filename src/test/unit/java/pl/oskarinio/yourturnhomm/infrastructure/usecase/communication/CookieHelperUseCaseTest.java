package pl.oskarinio.yourturnhomm.infrastructure.usecase.communication;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import pl.oskarinio.yourturnhomm.domain.model.user.Role;
import pl.oskarinio.yourturnhomm.domain.model.user.UserServiceData;
import pl.oskarinio.yourturnhomm.domain.port.out.Token;
import pl.oskarinio.yourturnhomm.infrastructure.config.TestWebUtilities;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CookieHelperUseCaseTest {
    @Mock
    private Token token;

    private static final Integer ACCESS_TOKEN_SECONDS = 600;
    private static final Integer REFRESH_TOKEN_SECONDS = 3600;
    private static final String USERNAME = "testUsername";
    private static final String PASSWORD = "testPassword";
    private static final String ACCESS_TOKEN = "testAccessToken";
    private static final String REFRESH_TOKEN = "testRefreshToken";
    private static final String REFRESH_COOKIE_NAME = "refreshToken";
    private static final String ACCESS_COOKIE_NAME = "accessToken";

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private Cookie cookieRefresh;
    private Cookie cookieAccess;

    private CookieHelperUseCase cookieHelperUseCase;

    @BeforeEach
    void setUp(){
        cookieHelperUseCase = new CookieHelperUseCase(token, ACCESS_TOKEN_SECONDS, REFRESH_TOKEN_SECONDS);

        TestWebUtilities webUtilities = new TestWebUtilities();
        request = webUtilities.getRequest();
        response =  webUtilities.getResponse();
        cookieRefresh = getCookie(REFRESH_COOKIE_NAME, REFRESH_TOKEN);
        cookieAccess = getCookie(ACCESS_COOKIE_NAME, ACCESS_TOKEN);
    }

    @Test
    @DisplayName("Poprawny username, userRoles, accessToken, refreshToken, response, ustawiam poprawne Cookies")
    void setCookieTokens_correctValues_resultSetCorrectCookies() {
        UserServiceData userServiceData = setCookieTokens_assign();
        cookieHelperUseCase.setCookieTokens(userServiceData, response);
        setCookieTokens_assert();
    }

    @Test
    @DisplayName("Correct username, userRoles, accessToken, refreshToken, null response, rzuca NullPointerException")
    void setCookieTokens_correctUserDataNullResponse_resultNullPointerException() {
        UserServiceData userServiceData = setCookieTokens_assign();
        assertThrows(NullPointerException.class, () -> cookieHelperUseCase.setCookieTokens(userServiceData,null));
    }

    @Test
    @DisplayName("Null username, userRoles, accessToken, refreshToken, correct response, rzuca NullPointerException")
    void setCookieTokens_nullUserDataCorrectResponse_resultNullPointerException() {
        assertThrows(NullPointerException.class, () -> cookieHelperUseCase.setCookieTokens(null, response));
    }

    @Test
    @DisplayName("null username, userRoles, accessToken, refreshToken, response, rzuca NullPointerException")
    void setCookieTokens_nullValues_resultNullPointerException() {
        assertThrows(NullPointerException.class, () -> cookieHelperUseCase.setCookieTokens(null,null));
    }

    private UserServiceData setCookieTokens_assign(){
        UserServiceData userServiceData = getUserServiceData();
        when(token.generateToken(userServiceData, ACCESS_TOKEN_SECONDS)).thenReturn(ACCESS_TOKEN);
        when(token.generateToken(userServiceData,REFRESH_TOKEN_SECONDS)).thenReturn(REFRESH_TOKEN);
        tokenUpdateUserServiceData(userServiceData);

        return userServiceData;
    }

    private  void setCookieTokens_assert(){
        Cookie[] cookies = response.getCookies();
        List<Cookie> cookiesList = List.of(cookies);
        List<String> cookiesNames = cookiesList
                .stream()
                .map(Cookie::getName)
                .toList();

        List<String> cookiesValues = cookiesList
                .stream()
                .map(Cookie::getValue)
                .toList();

        List<Integer> cookiesMaxAge = cookiesList
                .stream()
                .map(Cookie::getMaxAge)
                .toList();

        assertThat(cookiesNames).contains(ACCESS_COOKIE_NAME).contains(REFRESH_COOKIE_NAME);
        assertThat(cookiesValues).contains(ACCESS_TOKEN).contains(REFRESH_TOKEN);
        assertThat(cookiesMaxAge).contains(ACCESS_TOKEN_SECONDS).contains(REFRESH_TOKEN_SECONDS);
    }

    @Test
    @DisplayName("Poprawny cookieRefresh, zwraca poprawny username")
    void getUsernameFromCookie_correctCookieRefresh_resultGetCorrectUsername(){
        request.setCookies(cookieRefresh);

        when(token.extractUsername(REFRESH_TOKEN)).thenReturn(USERNAME);
        String usernameCookie = cookieHelperUseCase.getUsernameFromCookie(request);

        verify(token, times(1)).extractUsername(REFRESH_TOKEN);
        assertThat(usernameCookie).isEqualTo(USERNAME);
    }

    @Test
    @DisplayName("Null cookieRefresh, rzuca NullPointerException")
    void getUsernameFromCookie_nullCookieRefresh_resultGetCorrectUsername(){
        assertThrows(NullPointerException.class, () -> cookieHelperUseCase.getUsernameFromCookie(request));
    }

    @Test
    @DisplayName("Poprawny response, accessCookie usunięte")
    void removeAccessCookie_defaultResponse_resultCookieDeleted(){
        response.addCookie(cookieAccess);
        cookieHelperUseCase.removeAccessCookie(response);
        removeCookie_assert(ACCESS_COOKIE_NAME);
    }

    @Test
    @DisplayName("Null response, rzuca NullPointerException")
    void removeAccessCookie_nullResponse_resultNullPointerException(){
        assertThrows(NullPointerException.class, () -> cookieHelperUseCase.removeAccessCookie(null));
    }

    @Test
    @DisplayName("Poprawny response, refreshCookie usunięte")
    void removeRefreshCookie_defaultResponse_resultCookieDeleted(){
        response.addCookie(cookieRefresh);
        cookieHelperUseCase.removeRefreshCookie(response);
        removeCookie_assert(REFRESH_COOKIE_NAME);
    }

    @Test
    @DisplayName("Null response, rzuca NullPointerException")
    void removeRefreshCookie_nullResponse_resultNullPointerException(){
        assertThrows(NullPointerException.class, () -> cookieHelperUseCase.removeRefreshCookie(null));
    }


    private void removeCookie_assert(String cookieName){
        Cookie[] cookies = response.getCookies();
        List<Cookie> cookiesList = List.of(cookies);
        Cookie cookieToRemove = cookiesList.getLast();

        assertThat(cookieToRemove.getName()).isEqualTo(cookieName);
        assertThat(cookieToRemove.getMaxAge()).isZero();
        assertThat(cookieToRemove.getValue()).isEmpty();
    }

    @Test
    @DisplayName("Poprawny response, request, cookies wyczyszczone")
    void clearCookies_correctResponseRequest_resultCookiesCleaned(){
        request.setCookies(cookieAccess, cookieRefresh);
        cookieHelperUseCase.clearCookies(response, request);
        clearCookies_assert();
    }

    @Test
    @DisplayName("Null response, request, rzuca NullPointerException")
    void clearCookies_nullResponseRequest_resultNullPointerException(){
        assertThrows(NullPointerException.class, () -> cookieHelperUseCase.clearCookies(null, null));
    }

    private void clearCookies_assert(){
        Cookie[] cookies = response.getCookies();
        List<Cookie> cookiesList = List.of(cookies);
        List<String> cookiesValues = cookiesList
                .stream()
                .map(Cookie::getValue)
                .toList();

        assertThat(cookiesValues).containsOnly("");
    }

    private UserServiceData getUserServiceData(){
        UserServiceData userServiceData = new UserServiceData(USERNAME, PASSWORD);
        userServiceData.setRoles(Set.of(Role.ROLE_USER));

        return userServiceData;
    }

    private void tokenUpdateUserServiceData(UserServiceData userServiceData){
        String accessToken = token.generateToken(userServiceData, ACCESS_TOKEN_SECONDS);
        String refreshToken = token.generateToken(userServiceData, REFRESH_TOKEN_SECONDS);
        userServiceData.setAccessToken(accessToken);
        userServiceData.setRefreshToken(refreshToken);
    }

    private Cookie getCookie(String cookieName, String cookieValue){
        Cookie cookie = new Cookie(cookieName, cookieValue);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(3600);
        cookie.setPath("/");
        return cookie;
    }
}

