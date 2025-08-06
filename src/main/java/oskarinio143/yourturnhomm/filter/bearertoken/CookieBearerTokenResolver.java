package oskarinio143.yourturnhomm.filter.bearertoken;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;
import oskarinio143.yourturnhomm.model.constant.Route;

import java.util.List;

@Component
public class CookieBearerTokenResolver implements BearerTokenResolver {

    private final String COOKIE_ACCESS_TOKEN = "accessToken";
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
