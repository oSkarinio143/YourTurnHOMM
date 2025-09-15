package pl.oskarinio.yourturnhomm.app.technical.communication;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pl.oskarinio.yourturnhomm.domain.model.user.UserServiceData;
import pl.oskarinio.yourturnhomm.domain.port.out.Token;
import pl.oskarinio.yourturnhomm.infrastructure.port.communication.CookieHelper;
import pl.oskarinio.yourturnhomm.infrastructure.usecase.communication.CookieHelperUseCase;

@Component
public class CookieHelperService implements CookieHelper {
    private final CookieHelperUseCase cookieHelperUseCase;

    public CookieHelperService(Token token,
                               @Value("${token.access.seconds}") long accessSeconds,
                               @Value("${token.refresh.seconds}") long refreshSeconds) {
        this.cookieHelperUseCase = new CookieHelperUseCase(token, accessSeconds, refreshSeconds);
    }

    @Override
    public void setCookieTokens(UserServiceData loginServiceData, HttpServletResponse response) {
        cookieHelperUseCase.setCookieTokens(loginServiceData,response);
    }

    @Override
    public String getUsernameFromCookie(HttpServletRequest request) {
        return cookieHelperUseCase.getUsernameFromCookie(request);
    }

    @Override
    public void removeAccessCookie(HttpServletResponse response) {
        cookieHelperUseCase.removeAccessCookie(response);
    }

    @Override
    public void removeRefreshCookie(HttpServletResponse response) {
        cookieHelperUseCase.removeRefreshCookie(response);
    }

    @Override
    public void clearCookies(HttpServletResponse response, HttpServletRequest request) {
        cookieHelperUseCase.clearCookies(response,request);
    }
}
