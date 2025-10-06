package pl.oskarinio.yourturnhomm.infrastructure.security.filter.bearertoken;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;
import pl.oskarinio.yourturnhomm.domain.model.Route;

import java.util.List;

//Sprawdza, czy token jest w ciasteczku
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


        if(PUBLIC_PATHS.contains(path))
            return null;

        if (cookieAccess != null && !cookieAccess.getValue().isEmpty())
            return cookieAccess.getValue();

        if(request.getAttribute("accessToken") != null)
            return request.getAttribute("accessToken").toString();
        return null;
    }
}
