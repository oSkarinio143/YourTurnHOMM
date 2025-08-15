package pl.oskarinio.yourturnhomm.domain.rest.port.in;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pl.oskarinio.yourturnhomm.domain.user.model.UserServiceData;

public interface CookieHelperUseCase {
    void setCookieTokens(UserServiceData loginServiceData, HttpServletResponse response);
    String getUsernameFromCookie(HttpServletRequest request);
    void removeAccessCookie(HttpServletResponse response);
    void removeRefreshCookie(HttpServletResponse response);
    void clearCookies(HttpServletResponse response, HttpServletRequest request);
}
