package pl.oskarinio.yourturnhomm.infrastructure.config.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;
import pl.oskarinio.yourturnhomm.app.technical.communication.CookieHelperService;
import pl.oskarinio.yourturnhomm.domain.model.Route;
import pl.oskarinio.yourturnhomm.domain.model.user.RefreshToken;
import pl.oskarinio.yourturnhomm.domain.model.user.User;
import pl.oskarinio.yourturnhomm.domain.model.user.UserServiceData;
import pl.oskarinio.yourturnhomm.domain.port.repository.UserRepository;
import pl.oskarinio.yourturnhomm.infrastructure.port.communication.Token;

import java.io.IOException;
import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class RefreshFilter extends OncePerRequestFilter {
    @Autowired
    private Token token;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CookieHelperService cookieHelperService;
    @Autowired
    private Clock clock;
    @Value("${token.access.seconds}")
    private long TOKEN_ACCESS_SECONDS;
    @Value("${token.refresh.seconds}")
    private long TOKEN_REFRESH_SECONDS;

    private static final List<String> PUBLIC_PATHS = new ArrayList<>(List.of(
            Route.MAIN + Route.LOGIN,
            Route.MAIN + Route.REGISTER,
            Route.FAVICON));

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if (continueIfPublicPath(request, response, filterChain))
            return;

        Cookie cookieAccess = WebUtils.getCookie(request, "accessToken");
        String tokenAccess = getTokenAccess(cookieAccess);

        if (continueIfAccessTokenGood(cookieAccess, tokenAccess, request, response, filterChain))
            return;

        Cookie cookieRefresh = WebUtils.getCookie(request, "refreshToken");
        String tokenRefresh = getTokenRefresh(cookieRefresh);

        if(continueIfRefreshBad(cookieRefresh, cookieAccess, tokenRefresh, response, request, filterChain))
            return;

        String username = token.extractUsername(tokenRefresh);
        Optional<User> userOptional = userRepository.findByUsername(username);

        if(continueIfUserBad(userOptional, request, response, filterChain))
            return;

        User user = userOptional.get();
        UserServiceData userServiceData = setUserServiceData(user);
        setUserTokens(userServiceData, user, response, request);

        filterChain.doFilter(request, response);
    }

    private boolean continueIfPublicPath(HttpServletRequest request,
                                        HttpServletResponse response,
                                        FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        if (PUBLIC_PATHS.contains(path)) {
            filterChain.doFilter(request, response);
            return true;
        }
        return false;
    }

    private boolean continueIfAccessTokenGood(Cookie cookieAccess,
                                             String tokenAccess,
                                             HttpServletRequest request,
                                             HttpServletResponse response,
                                             FilterChain filterChain) throws ServletException, IOException {
        if (cookieAccess != null && !token.isTokenExpiredSafe(tokenAccess)) {
            filterChain.doFilter(request, response);
            return true;
        }
        return false;
    }

    private String getTokenAccess(Cookie cookieAccess){
        if (cookieAccess != null && !cookieAccess.getValue().isBlank())
            return cookieAccess.getValue();
        return null;
    }

    private String getTokenRefresh(Cookie cookieRefresh) {
        if (cookieRefresh != null && !cookieRefresh.getValue().isBlank())
            return cookieRefresh.getValue();
        return null;
    }

    private boolean continueIfRefreshBad(Cookie cookieRefresh,
                                         Cookie cookieAccess,
                                         String tokenRefresh,
                                         HttpServletResponse response,
                                         HttpServletRequest request,
                                         FilterChain filterChain) throws ServletException, IOException {
        if (cookieRefresh == null || token.isTokenExpiredSafe(tokenRefresh)) {
            cookieHelperService.clearCookies(response, request);
            filterChain.doFilter(request, response);
            return true;
        }
        return false;
    }

    private boolean continueIfUserBad(Optional<User> userOptional,
                                      HttpServletRequest request,
                                      HttpServletResponse response,
                                      FilterChain filterChain) throws ServletException, IOException {
        if (userOptional.isEmpty()) {
            cookieHelperService.clearCookies(response, request);
            filterChain.doFilter(request, response);
            return true;
        }
        return false;
    }

    private UserServiceData setUserServiceData(User user){
        UserServiceData userServiceData = new UserServiceData(user.getUsername(), user.getPassword());
        userServiceData.setRoles(user.getRoles());
        return userServiceData;
    }

    private void saveRefreshToken(String refreshTokenNew, User user){
        Instant now = Instant.now(clock);
        RefreshToken refreshToken = new RefreshToken(refreshTokenNew, now, now.plus(TOKEN_REFRESH_SECONDS, ChronoUnit.SECONDS));
        user.setRefreshToken(refreshToken);
        userRepository.save(user);
    }

    private void setUserTokens(UserServiceData userServiceData,
                               User user,
                               HttpServletResponse response,
                               HttpServletRequest request){

        String accessTokenNew = token.generateToken(userServiceData, TOKEN_ACCESS_SECONDS);
        String refreshTokenNew = token.generateToken(userServiceData, TOKEN_REFRESH_SECONDS);
        userServiceData.setAccessToken(accessTokenNew);
        userServiceData.setRefreshToken(refreshTokenNew);
        saveRefreshToken(refreshTokenNew, user);
        cookieHelperService.setCookieTokens(userServiceData, response);
        request.setAttribute("accessToken", accessTokenNew);
    }
}
