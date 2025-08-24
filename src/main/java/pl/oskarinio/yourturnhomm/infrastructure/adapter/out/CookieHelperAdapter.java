package pl.oskarinio.yourturnhomm.app.implementation.rest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pl.oskarinio.yourturnhomm.app.port.in.rest.CookieHelperUseCase;
import pl.oskarinio.yourturnhomm.domain.service.rest.CookieHelper;
import pl.oskarinio.yourturnhomm.app.port.in.user.TokenUseCase;
import pl.oskarinio.yourturnhomm.domain.model.user.UserServiceData;

@Component
public class CookieHelperAdapter implements CookieHelperUseCase {
    private final CookieHelper cookieHelper;

    public CookieHelperAdapter(TokenUseCase tokenUseCase,
                               @Value("${token.access.seconds}") long accessSeconds,
                               @Value("${token.refresh.seconds}") long refreshSeconds) {
        this.cookieHelper = new CookieHelper(tokenUseCase, accessSeconds, refreshSeconds);
    }

    @Override
    public void setCookieTokens(UserServiceData loginServiceData, HttpServletResponse response) {
        cookieHelper.setCookieTokens(loginServiceData,response);
    }

    @Override
    public String getUsernameFromCookie(HttpServletRequest request) {
        return cookieHelper.getUsernameFromCookie(request);
    }

    @Override
    public void removeAccessCookie(HttpServletResponse response) {
        cookieHelper.removeAccessCookie(response);
    }

    @Override
    public void removeRefreshCookie(HttpServletResponse response) {
        cookieHelper.removeRefreshCookie(response);
    }

    @Override
    public void clearCookies(HttpServletResponse response, HttpServletRequest request) {
        cookieHelper.clearCookies(response,request);
    }
}
