package oskarinio143.heroes3.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.web.util.WebUtils;
import oskarinio143.heroes3.model.Route;
import oskarinio143.heroes3.service.TokenService;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.List;

public class CookieBearerTokenResolver implements BearerTokenResolver {
    private final String COOKIE_ACCESS_TOKEN = "accessToken";
    private final String COOKIE_REFRESH_TOKEN = "refreshToken";
    private static final List<String> PUBLIC_PATHS = List.of(
            Route.MAIN + Route.REGISTER,
            Route.MAIN + Route.LOGIN);

    @Override
    public String resolve(HttpServletRequest request) {
        Cookie cookieAccess = WebUtils.getCookie(request, COOKIE_ACCESS_TOKEN);
        Cookie cookieRefresh = WebUtils.getCookie(request, COOKIE_REFRESH_TOKEN);
        String path = request.getRequestURI();

        if(PUBLIC_PATHS.contains(path))
            return null;

        if (cookieAccess != null)
            return cookieAccess.getValue();
        return null;
    }
}
