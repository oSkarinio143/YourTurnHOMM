package pl.oskarinio.yourturnhomm.infrastructure.port.cookie;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pl.oskarinio.yourturnhomm.domain.model.user.UserServiceData;

public interface CookieHelper {
    void setCookieTokens(UserServiceData loginServiceData, HttpServletResponse response);
    String getUsernameFromCookie(HttpServletRequest request);
    void removeAccessCookie(HttpServletResponse response);
    void removeRefreshCookie(HttpServletResponse response);
    void clearCookies(HttpServletResponse response, HttpServletRequest request);
}
