package oskarinio143.heroes3.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final SmartAuthenticationEntryPoint smartAuthenticationEntryPoint;

    public SecurityConfig(CustomAccessDeniedHandler accessDeniedHandler, SmartAuthenticationEntryPoint smartAuthenticationEntryPoint) {
        this.accessDeniedHandler = accessDeniedHandler;
        this.smartAuthenticationEntryPoint = smartAuthenticationEntryPoint;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           JwtDecoder jwtDecoder,
                                           CookieBearerTokenResolver cookieBearerTokenResolver,
                                           RefreshFilter refreshFilter) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/oskarinio143/heroes/login",
                                                    "/oskarinio143/heroes/register",
                                                    "/oskarinio143/heroes/refresh").permitAll()
                        .requestMatchers("/oskarinio143/heroes",
                                                    "/oskarinio143/heroes/database",
                                                    "/oskarinio143/heroes/database/view",
                                                    "/oskarinio143/heroes/duel",
                                                    "/oskarinio143/heroes/duel/**").hasRole("USER")
                        .requestMatchers("/oskarinio143/heroes/database/add",
                                                    "/oskarinio143/heroes/database/modify",
                                                    "/oskarinio143/heroes/database/delete",
                                                    "/oskarinio143/heroes/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(refreshFilter, BearerTokenAuthenticationFilter.class)
                .oauth2ResourceServer(oauth2 -> oauth2
                        .bearerTokenResolver(cookieBearerTokenResolver)
                        .jwt(jwt -> jwt
                                .decoder(jwtDecoder))
                        .authenticationEntryPoint(smartAuthenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                )
                .httpBasic(basic -> basic.disable());
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
