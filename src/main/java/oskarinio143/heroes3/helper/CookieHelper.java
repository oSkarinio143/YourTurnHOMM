package oskarinio143.heroes3.helper;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;
import oskarinio143.heroes3.model.servicedto.UserServiceData;
import oskarinio143.heroes3.service.TokenService;

import java.util.Arrays;
import java.util.List;

@Component
public class CookieHelper {
    private final TokenService tokenService;
    @Value("${token.access.seconds}")
    private long TOKEN_ACCESS_SECONDS;
    @Value("${token.refresh.seconds}")
    private long TOKEN_REFRESH_SECONDS;

    public CookieHelper(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    public void setCookieTokens(UserServiceData loginServiceData, HttpServletResponse response){
        ResponseCookie accessCookie = createAccessCookie(loginServiceData.getAccessToken());
        ResponseCookie refreshCookie = createRefreshCookie(loginServiceData.getRefreshToken());
        setCookies(accessCookie, refreshCookie, response);
    }

    public ResponseCookie createAccessCookie(String token){
        return ResponseCookie.from("accessToken", token)
                .httpOnly(true)
                .path("/")
                .maxAge(TOKEN_ACCESS_SECONDS)
                .secure(true)
                .build();
    }

    public ResponseCookie createRefreshCookie(String token){
        return ResponseCookie.from("refreshToken", token)
                .httpOnly(true)
                .path("/")
                .maxAge(TOKEN_REFRESH_SECONDS)
                .secure(true)
                .build();
    }

    public ResponseCookie createExpiredAccessCookie(){
        return ResponseCookie.from("accessToken", null)
                .httpOnly(true)
                .path("/")
                .secure(true)
                .build();
    }

    public void setCookies(ResponseCookie accessCookie, ResponseCookie refreshCookie, HttpServletResponse response){
        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
    }

    public String getUsernameFromCookie(HttpServletRequest request){
        Cookie cookie = WebUtils.getCookie(request, "refreshToken");
        String refreshToken = cookie.getValue();
        return tokenService.extractUsername(refreshToken);
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
