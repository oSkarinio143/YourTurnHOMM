package pl.oskarinio.yourturnhomm.domain.service.rest;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.util.WebUtils;
import pl.oskarinio.yourturnhomm.app.port.in.user.TokenUseCase;
import pl.oskarinio.yourturnhomm.domain.model.user.UserServiceData;

import java.util.Arrays;
import java.util.List;


public class CookieHelper {
    private final TokenUseCase tokenUseCase;
    private long TOKEN_ACCESS_SECONDS;
    private long TOKEN_REFRESH_SECONDS;

    public CookieHelper(TokenUseCase tokenUseCase, long accessSeconds, long refreshSeconds) {
        this.tokenUseCase = tokenUseCase;
        TOKEN_ACCESS_SECONDS = accessSeconds;
        TOKEN_REFRESH_SECONDS = refreshSeconds;
    }

    public void setCookieTokens(UserServiceData loginServiceData, HttpServletResponse response){
        ResponseCookie accessCookie = createAccessCookie(loginServiceData.getAccessToken());
        ResponseCookie refreshCookie = createRefreshCookie(loginServiceData.getRefreshToken());
        setCookies(accessCookie, refreshCookie, response);
    }

    private ResponseCookie createAccessCookie(String token){
        return ResponseCookie.from("accessToken", token)
                .httpOnly(true)
                .path("/")
                .maxAge(TOKEN_ACCESS_SECONDS)
                .secure(true)
                .sameSite("Lax")
                .build();
    }

    private ResponseCookie createRefreshCookie(String token){
        return ResponseCookie.from("refreshToken", token)
                .httpOnly(true)
                .path("/")
                .maxAge(TOKEN_REFRESH_SECONDS)
                .secure(true)
                .sameSite("Strict")
                .build();
    }

    private void setCookies(ResponseCookie accessCookie, ResponseCookie refreshCookie, HttpServletResponse response){
        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
    }

    public String getUsernameFromCookie(HttpServletRequest request){
        Cookie cookie = WebUtils.getCookie(request, "refreshToken");
        String refreshToken = cookie.getValue();
        return tokenUseCase.extractUsername(refreshToken);
    }

    public void removeAccessCookie(HttpServletResponse response){
        Cookie cookie = new Cookie("accessToken", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    public void removeRefreshCookie(HttpServletResponse response){
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    public void clearCookies(HttpServletResponse response, HttpServletRequest request){
        List<Cookie> cookies = Arrays.asList(request.getCookies());
        cookies.forEach(cookie -> {
            Cookie toDelete = new Cookie(cookie.getName(), "");
            toDelete.setPath("/");
            toDelete.setMaxAge(0);
            response.addCookie(toDelete);
        });
    }
}
