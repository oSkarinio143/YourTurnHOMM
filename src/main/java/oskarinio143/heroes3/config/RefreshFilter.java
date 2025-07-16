package oskarinio143.heroes3.config;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;
import oskarinio143.heroes3.helper.CookieHelper;
import oskarinio143.heroes3.model.constant.Route;
import oskarinio143.heroes3.model.entity.RefreshToken;
import oskarinio143.heroes3.model.entity.User;
import oskarinio143.heroes3.model.servicedto.UserServiceData;
import oskarinio143.heroes3.repository.UserRepository;
import oskarinio143.heroes3.service.TokenService;

import java.io.IOException;
import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RefreshFilter extends OncePerRequestFilter {
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CookieHelper cookieHelper;
    @Autowired
    private Clock clock;
    @Value("${token.access.seconds}")
    private long TOKEN_ACCESS_SECONDS;
    @Value("${token.refresh.seconds}")
    private long TOKEN_REFRESH_SECONDS;

    /*
    Logika odświeżania access i refresh tokenów - Wiem że to nie optymalne ale zaplanowałem aplikacje wokół tokenów
    i chciałem przy nich pozostać.
     */
    private static final List<String> PUBLIC_PATHS = new ArrayList<>(List.of(
            Route.MAIN + Route.LOGIN,
            Route.MAIN + Route.REGISTER));

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();
        if (PUBLIC_PATHS.contains(path)){
            filterChain.doFilter(request, response);
            return;
        }

        else {
            Cookie cookieAccess = WebUtils.getCookie(request, "accessToken");

            String tokenAccess = "";
            if(cookieAccess != null)
                tokenAccess = cookieAccess.getValue();

            if (cookieAccess == null || tokenService.isTokenExpiredSafe(tokenAccess)) {
                Cookie cookieRefresh = WebUtils.getCookie(request, "refreshToken");
                String tokenRefresh = "";
                if(cookieRefresh != null)
                    tokenRefresh = cookieRefresh.getValue();

                if (cookieRefresh == null || tokenService.isTokenExpiredSafe(tokenRefresh)){
                    if(cookieAccess == null && cookieRefresh == null)
                        cookieHelper.clearCookies(response, request);
                    response.sendRedirect(Route.MAIN + Route.LOGIN + "?logout=auto");
                    return;
                }
                String username = tokenService.extractUsername(tokenRefresh);
                User user = userRepository.findByUsernameOrThrow(username);
                UserServiceData userServiceData = new UserServiceData(user.getUsername(), user.getPassword());
                userServiceData.setRoles(user.getRoles());

                String accessTokenNew = tokenService.generateToken(userServiceData, 1);
                String refreshTokenNew = tokenService.generateToken(userServiceData, TOKEN_REFRESH_SECONDS);
                userServiceData.setAccessToken(accessTokenNew);
                userServiceData.setRefreshToken(refreshTokenNew);

                Instant now = Instant.now(clock);
                RefreshToken refreshToken = new RefreshToken(refreshTokenNew, now, now.plus(TOKEN_REFRESH_SECONDS, ChronoUnit.SECONDS));
                user.setRefreshToken(refreshToken);
                userRepository.save(user);
                cookieHelper.setCookieTokens(userServiceData, response);
            }
            filterChain.doFilter(request, response);
        }
    }
}
