package oskarinio143.heroes3.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import oskarinio143.heroes3.model.servicedto.UserServiceData;

@Component
public class CookieHelper {
    //reg, log, ref
    public void setCookieTokens(UserServiceData loginServiceData, HttpServletResponse response){
        setTokenInCookie(response, loginServiceData.getAccessToken(), "accessToken");
        setTokenInCookie(response,loginServiceData.getRefreshToken(), "refreshToken");
    }

    //reg,log,ref
    public void setTokenInCookie(HttpServletResponse response, String token, String name){
        ResponseCookie cookie = ResponseCookie.from(name, token)
                .httpOnly(true)
                .path("/")
                .secure(true)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
