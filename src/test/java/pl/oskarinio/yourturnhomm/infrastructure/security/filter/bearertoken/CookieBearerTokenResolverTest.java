package pl.oskarinio.yourturnhomm.infrastructure.security.filter.bearertoken;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import pl.oskarinio.yourturnhomm.domain.model.Route;
import pl.oskarinio.yourturnhomm.infrastructure.config.TestWebUtilities;
import pl.oskarinio.yourturnhomm.infrastructure.config.filter.RefreshFilter;

import static org.assertj.core.api.Assertions.assertThat;

class CookieBearerTokenResolverTest {
    private static final String PUBLIC_PATH = Route.MAIN + Route.REGISTER;
    private static final String UNPUBLIC_PATH = Route.MAIN;
    private static final String COOKIE_ACCESS_TOKEN = "accessToken";
    private static final String ACCESS_TOKEN_VALUE = "accessTokenValue";
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private FilterChain filterChain;
    private RefreshFilter refreshFilter;

    private CookieBearerTokenResolver cookieBearerTokenResolver;

    @BeforeEach()
    void setUp(){
        cookieBearerTokenResolver = new CookieBearerTokenResolver();

        TestWebUtilities webUtilities = new TestWebUtilities();
        request = webUtilities.getRequest();
        response =  webUtilities.getResponse();
        filterChain = webUtilities.getFilterChain();
    }

    @Test
    @DisplayName("Ustawiam publiczną śćieżkę, metoda zwraca null")
    void resolve_pathIsPublic_nothingHappened(){
        request.setRequestURI(PUBLIC_PATH);

        String accessTokenValue = cookieBearerTokenResolver.resolve(request);

        assertThat(accessTokenValue).isNull();
    }

    @Test
    @DisplayName("Ustawiam niepubliczną ścieżkę, request zawiera cookie z wartością accessToken, metoda zwraca tę wartość")
    void resolve_accessTokenInCookie_returnAccessTokenValue(){
        Cookie cookieAccess = new Cookie(COOKIE_ACCESS_TOKEN, ACCESS_TOKEN_VALUE);
        request.setCookies(cookieAccess);
        request.setRequestURI(UNPUBLIC_PATH);

        String accessTokenValue = cookieBearerTokenResolver.resolve(request);

        assertThat(accessTokenValue).isEqualTo(ACCESS_TOKEN_VALUE);
    }

    @Test
    @DisplayName("Ustawiam niepubliczną ścieżkę, request zawiera attrybut z wartością accessToken, metoda zwraca tę wartość")
    void resolve_accessTokenInAttribute_returnAccessTokenValue(){
        request.setAttribute(COOKIE_ACCESS_TOKEN, ACCESS_TOKEN_VALUE);
        request.setRequestURI(UNPUBLIC_PATH);

        String accessTokenValue = cookieBearerTokenResolver.resolve(request);

        assertThat(accessTokenValue).isEqualTo(ACCESS_TOKEN_VALUE);
    }

    @Test
    @DisplayName("Ustawiam niepubliczną ścieżkę, request zawiera pusty cookie i attrybut z wartością accessToken, metoda zwraca accessToken")
    void resolve_cookieEmptyAttributeHasAccessToken_returnAccessTokenValue(){
        Cookie cookieAccess = new Cookie(COOKIE_ACCESS_TOKEN, "");
        request.setRequestURI(UNPUBLIC_PATH);
        request.setCookies(cookieAccess);
        request.setAttribute(COOKIE_ACCESS_TOKEN, ACCESS_TOKEN_VALUE);

        String accessTokenValue = cookieBearerTokenResolver.resolve(request);

        assertThat(accessTokenValue).isEqualTo(ACCESS_TOKEN_VALUE);
    }

    @Test
    @DisplayName("Ustawiam niepubliczną ścieżkę, request nie zawiera cookie ani attrybutu z wartością accessToken, metoda zwraca null")
    void resolve_cookieAndAttributeWithoutAccessToken_nothingHappened(){
        request.setRequestURI(UNPUBLIC_PATH);

        String accessTokenValue = cookieBearerTokenResolver.resolve(request);

        assertThat(accessTokenValue).isNull();
    }
}
