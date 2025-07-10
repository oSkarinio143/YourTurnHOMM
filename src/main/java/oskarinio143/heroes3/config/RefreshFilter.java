package oskarinio143.heroes3.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.ArrayList;
import java.util.List;

@Component
public class RefreshFilter extends OncePerRequestFilter {
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CookieHelper cookieHelper;

    private static final List<String> PUBLIC_PATHS = new ArrayList<>(List.of(
            Route.MAIN + Route.LOGIN,
            Route.MAIN + Route.REGISTER,
            Route.MAIN + Route.REFRESH));

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {


        String path = request.getRequestURI();
        if (PUBLIC_PATHS.contains(path))
            filterChain.doFilter(request, response);

        else {
            Cookie cookieAccess = WebUtils.getCookie(request, "accessToken");
            if(cookieAccess == null){
                filterChain.doFilter(request,response);
                return;
            }

            String tokenAccess = cookieAccess.getValue();
            if (tokenService.isTokenExpiredSafe(tokenAccess)) {

                Cookie cookieRefresh = WebUtils.getCookie(request, "refreshToken");
                if(cookieRefresh == null){
                    filterChain.doFilter(request, response);
                    return;
                }
                String tokenRefresh = cookieRefresh.getValue();
                cookieHelper.clearCookies(response, request);

                if (tokenService.isTokenExpiredSafe(tokenRefresh)){
                    cookieHelper.clearCookies(response, request);
                    response.sendRedirect(Route.MAIN + Route.LOGIN + "?logout=auto");
                    return;
                }

                String username = tokenService.extractUsername(tokenRefresh);
                User user = userRepository.findByUsernameOrThrow(username);
                UserServiceData userServiceData = new UserServiceData(user.getUsername(), user.getPassword());
                userServiceData.setRoles(user.getRoles());

                String accessTokenNew = tokenService.generateToken(userServiceData, 3600000 / 4);
                String refreshTokenNew = tokenService.generateToken(userServiceData, 3600000 * 24 * 7);
                userServiceData.setAccessToken(accessTokenNew);
                userServiceData.setRefreshToken(refreshTokenNew);

                RefreshToken refreshToken = new RefreshToken(refreshTokenNew);
                user.setRefreshToken(refreshToken);
                userRepository.save(user);

                cookieHelper.setCookieTokens(userServiceData, response);
            }
            filterChain.doFilter(request, response);
        }
    }
}
