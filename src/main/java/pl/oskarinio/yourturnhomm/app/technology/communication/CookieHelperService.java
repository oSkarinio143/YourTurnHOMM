package pl.oskarinio.yourturnhomm.app.technology.communication;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.oskarinio.yourturnhomm.domain.model.user.UserServiceData;
import pl.oskarinio.yourturnhomm.domain.port.out.Token;
import pl.oskarinio.yourturnhomm.infrastructure.port.communication.CookieHelper;
import pl.oskarinio.yourturnhomm.infrastructure.usecase.communication.CookieHelperUseCase;

@Slf4j
@Service
public class CookieHelperService implements CookieHelper {
    private final CookieHelperUseCase cookieHelperUseCase;

    public CookieHelperService(Token token,
                        @Value("${token.access.seconds}") long accessSeconds,
                        @Value("${token.refresh.seconds}") long refreshSeconds) {
        this.cookieHelperUseCase = new CookieHelperUseCase(token, accessSeconds, refreshSeconds);
    }

    @Override
    public void setCookieTokens(UserServiceData loginServiceData, HttpServletResponse response) {
        log.debug("Ustawiam tokeny w cookie dla uzytkownika. Nazwa = {}", loginServiceData.getUsername());
        cookieHelperUseCase.setCookieTokens(loginServiceData,response);
    }

    @Override
    public String getUsernameFromCookie(HttpServletRequest request) {
        String username = cookieHelperUseCase.getUsernameFromCookie(request);
        log.trace("Pobieram nazwe uzytkownika z cookies. Nazwa = {}", username);
        return username;
    }

    @Override
    public void removeAccessCookie(HttpServletResponse response) {
        log.debug("Usuwam access cookie");
        cookieHelperUseCase.removeAccessCookie(response);
    }

    @Override
    public void removeRefreshCookie(HttpServletResponse response) {
        log.debug("Usuwam refresh cookie");
        cookieHelperUseCase.removeRefreshCookie(response);
    }

    @Override
    public void clearCookies(HttpServletResponse response, HttpServletRequest request) {
        log.debug("Czyszcze wszystkie cookies");
        cookieHelperUseCase.clearCookies(response,request);
    }
}
