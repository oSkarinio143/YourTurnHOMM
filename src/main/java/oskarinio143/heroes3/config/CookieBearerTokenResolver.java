package oskarinio143.heroes3.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;
import oskarinio143.heroes3.model.constant.Route;
import oskarinio143.heroes3.service.TokenService;

import java.util.List;

@Component
public class CookieBearerTokenResolver implements BearerTokenResolver {

    @Autowired
    private TokenService tokenService;

    private final String COOKIE_ACCESS_TOKEN = "accessToken";
    private final String COOKIE_REFRESH_TOKEN = "refreshToken";
    private static final List<String> PUBLIC_PATHS = List.of(
            Route.MAIN + Route.REGISTER,
            Route.MAIN + Route.LOGIN);

    @Override
    public String resolve(HttpServletRequest request) {
        Cookie cookieAccess = WebUtils.getCookie(request, COOKIE_ACCESS_TOKEN);
        String path = request.getRequestURI();

        if(PUBLIC_PATHS.contains(path)) {
            return null;
        }

        if (cookieAccess != null) {
            return cookieAccess.getValue();
        }
        return null;
    }
}
