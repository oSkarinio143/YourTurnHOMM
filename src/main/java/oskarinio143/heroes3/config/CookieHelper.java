package oskarinio143.heroes3.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import oskarinio143.heroes3.model.Route;
import oskarinio143.heroes3.model.servicedto.UserServiceData;

@Component
public class CookieHelper {
    //reg, log, ref
    public void setCookieTokens(UserServiceData loginServiceData, HttpServletResponse response){
        ResponseCookie accessCookie = createAccessCookie(loginServiceData.getAccessToken());
        ResponseCookie refreshCookie = createRefreshCookie(loginServiceData.getRefreshToken());
        setCookies(accessCookie, refreshCookie, response);
    }

    public ResponseCookie createAccessCookie(String token){
        return ResponseCookie.from("accessToken", token)
                .httpOnly(true)
                .path("/")
                .secure(true)
                .build();
    }

    public ResponseCookie createRefreshCookie(String token){
        return ResponseCookie.from("refreshToken", token)
                .httpOnly(true)
                .path(Route.MAIN + Route.REFRESH)
                .secure(true)
                .build();
    }

    public void setCookies(ResponseCookie accessCookie, ResponseCookie refreshCookie, HttpServletResponse response){
        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
    }
}
