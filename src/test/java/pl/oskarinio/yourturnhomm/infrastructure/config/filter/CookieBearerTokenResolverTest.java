package pl.oskarinio.yourturnhomm.infrastructure.config.filter;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.util.WebUtils;
import pl.oskarinio.yourturnhomm.domain.model.Route;
import pl.oskarinio.yourturnhomm.infrastructure.security.filter.bearertoken.CookieBearerTokenResolver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

public class CookieBearerTokenResolverTest {
    private final CookieBearerTokenResolver cookieBearerTokenResolver = new CookieBearerTokenResolver();
    private final String PUBLIC_PATH = Route.MAIN + Route.REGISTER;
    private final String UNPUBLIC_PATH = Route.MAIN;
    private final String ACCESS_TOKEN_NAME = "accessToken";
    private final String ACCESS_TOKEN_VALUE = "accessTokenValue";

    @Test
    @DisplayName("Ustawiam publiczną śćieżkę, metoda zwraca null")
    public void resolve_pathIsPublic_nothingHappened(){
        MockHttpServletRequest request = new MockHttpServletRequest();
        Cookie cookieAccess = WebUtils.getCookie(request, ACCESS_TOKEN_NAME);
        request.setRequestURI(PUBLIC_PATH);

        String accessTokenValue = cookieBearerTokenResolver.resolve(request);

        assertThat(accessTokenValue).isNull();
    }

    @Test
    @DisplayName("Ustawiam niepubliczną ścieżkę, request zawiera cookie z wartością accessToken, metoda zwraca tę wartość")
    public void resolve_accessTokenInCookie_returnAccessTokenValue(){
        MockHttpServletRequest request = new MockHttpServletRequest();
        Cookie cookieAccess = new Cookie(ACCESS_TOKEN_NAME, ACCESS_TOKEN_VALUE);
        request.setCookies(cookieAccess);
        request.setRequestURI(UNPUBLIC_PATH);

        String accessTokenValue = cookieBearerTokenResolver.resolve(request);

        assertThat(accessTokenValue).isEqualTo(ACCESS_TOKEN_VALUE);
    }

    @Test
    @DisplayName("Ustawiam niepubliczną ścieżkę, request zawiera attrybut z wartością accessToken, metoda zwraca tę wartość")
    public void resolve_accessTokenInAttribute_returnAccessTokenValue(){
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setAttribute(ACCESS_TOKEN_NAME, ACCESS_TOKEN_VALUE);
        request.setRequestURI(UNPUBLIC_PATH);

        String accessTokenValue = cookieBearerTokenResolver.resolve(request);

        assertThat(accessTokenValue).isEqualTo(ACCESS_TOKEN_VALUE);
    }

    @Test
    @DisplayName("Ustawiam niepubliczną ścieżkę, request zawiera pusty cookie i attrybut z wartością accessToken, metoda zwraca accessToken")
    public void resolve_cookieEmptyAttributeHasAccessToken_returnAccessTokenValue(){
        MockHttpServletRequest request = new MockHttpServletRequest();
        Cookie cookieAccess = new Cookie(ACCESS_TOKEN_NAME, "");
        request.setRequestURI(UNPUBLIC_PATH);
        request.setCookies(cookieAccess);
        request.setAttribute(ACCESS_TOKEN_NAME, ACCESS_TOKEN_VALUE);

        String accessTokenValue = cookieBearerTokenResolver.resolve(request);

        assertThat(accessTokenValue).isEqualTo(ACCESS_TOKEN_VALUE);
    }

    @Test
    @DisplayName("Ustawiam niepubliczną ścieżkę, request nie zawiera cookie ani attrybutu z wartością accessToken, metoda zwraca null")
    public void resolve_cookieAndAttributeWithoutAccessToken_nothingHappened(){
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI(UNPUBLIC_PATH);

        String accessTokenValue = cookieBearerTokenResolver.resolve(request);

        assertThat(accessTokenValue).isNull();
    }
}
