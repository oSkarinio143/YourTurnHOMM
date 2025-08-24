package pl.oskarinio.yourturnhomm.infrastructure.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;
import pl.oskarinio.yourturnhomm.infrastructure.adapter.out.CookieHelperAdapter;
import pl.oskarinio.yourturnhomm.app.port.in.user.TokenUseCase;
import pl.oskarinio.yourturnhomm.app.port.out.repository.UserRepositoryPort;
import pl.oskarinio.yourturnhomm.domain.model.Route;
import pl.oskarinio.yourturnhomm.domain.model.user.RefreshToken;
import pl.oskarinio.yourturnhomm.domain.model.user.User;
import pl.oskarinio.yourturnhomm.domain.model.user.UserServiceData;

import java.io.IOException;
import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class RefreshFilter extends OncePerRequestFilter {
    @Autowired
    private TokenUseCase tokenUseCase;
    @Autowired
    private UserRepositoryPort userRepositoryPort;
    @Autowired
    private CookieHelperAdapter cookieHelperAdapter;
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

            if (cookieAccess == null || tokenUseCase.isTokenExpiredSafe(tokenAccess)) {
                Cookie cookieRefresh = WebUtils.getCookie(request, "refreshToken");
                String tokenRefresh = "";
                if(cookieRefresh != null)
                    tokenRefresh = cookieRefresh.getValue();

                if (cookieRefresh == null || tokenUseCase.isTokenExpiredSafe(tokenRefresh)){
                    if(cookieAccess != null || cookieRefresh != null)
                        cookieHelperAdapter.clearCookies(response, request);
                    filterChain.doFilter(request, response);
                    return;
                }
                String username = tokenUseCase.extractUsername(tokenRefresh);

                Optional<User> userOptional = userRepositoryPort.findByUsername(username);
                if(userOptional.isEmpty()){
                    cookieHelperAdapter.clearCookies(response, request);
                    filterChain.doFilter(request, response);
                    return;
                }
                User user = userOptional.get();
                UserServiceData userServiceData = new UserServiceData(user.getUsername(), user.getPassword());
                userServiceData.setRoles(user.getRoles());

                String accessTokenNew = tokenUseCase.generateToken(userServiceData, TOKEN_ACCESS_SECONDS);
                String refreshTokenNew = tokenUseCase.generateToken(userServiceData, TOKEN_REFRESH_SECONDS);
                userServiceData.setAccessToken(accessTokenNew);
                userServiceData.setRefreshToken(refreshTokenNew);

                Instant now = Instant.now(clock);
                RefreshToken refreshToken = new RefreshToken(refreshTokenNew, now, now.plus(TOKEN_REFRESH_SECONDS, ChronoUnit.SECONDS));
                user.setRefreshToken(refreshToken);
                userRepositoryPort.save(user);
                cookieHelperAdapter.setCookieTokens(userServiceData, response);

                /*
                Uwierzytelnianie użytkownika aby po odświeżeniu tokenów dało się obsłużyć żądanie, inaczej główny filter sprawdziłby
                cookies z request gdzie byłyby stare cookies, żądanie nie zostałoby obsłużone.
                 */
                var authorities = userServiceData.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.name()))
                        .collect(Collectors.toList());

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        null,
                        authorities
                );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request, response);
        }
    }
}
