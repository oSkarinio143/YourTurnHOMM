package oskarinio143.heroes3.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.web.util.WebUtils;

public class CookieBearerTokenResolver implements BearerTokenResolver {

    private final String cookieName = "accessToken";
    @Override
    public String resolve(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, this.cookieName);

        // Jeśli ciasteczko istnieje, zwróć jego wartość (czyli token)
        if (cookie != null) {
            return cookie.getValue();
        }

        return "";
    }
}
