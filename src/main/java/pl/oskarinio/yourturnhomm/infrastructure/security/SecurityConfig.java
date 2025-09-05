package pl.oskarinio.yourturnhomm.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.header.HeaderWriterFilter;
import pl.oskarinio.yourturnhomm.infrastructure.config.filter.LoggingFilter;
import pl.oskarinio.yourturnhomm.infrastructure.security.filter.CspNonceFilter;
import pl.oskarinio.yourturnhomm.infrastructure.security.filter.bearertoken.CookieBearerTokenResolver;
import pl.oskarinio.yourturnhomm.infrastructure.security.filter.bearertoken.CustomAuthenticationEntryPoint;
import pl.oskarinio.yourturnhomm.infrastructure.config.filter.RefreshFilter;
import pl.oskarinio.yourturnhomm.infrastructure.security.filter.bearertoken.CustomAccessDeniedHandler;
import pl.oskarinio.yourturnhomm.domain.model.Route;

@Configuration
@EnableWebSecurity
class SecurityConfig {

    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    public SecurityConfig(CustomAuthenticationEntryPoint customAuthenticationEntryPoint, CustomAccessDeniedHandler customAccessDeniedHandler) {
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
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
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()
                        .requestMatchers(Route.MAIN + Route.LOGIN,
                                Route.MAIN + Route.REGISTER).permitAll()
                        .requestMatchers(Route.MAIN + Route.USER + "/**").hasRole("USER")
                        .requestMatchers(Route.MAIN + Route.ADMIN + "/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .bearerTokenResolver(cookieBearerTokenResolver)
                        .jwt(jwt -> jwt
                                .decoder(jwtDecoder))
                        .accessDeniedHandler(customAccessDeniedHandler)
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                )
                .httpBasic(basic -> basic.disable())
                .headers(headers -> headers
                        .httpStrictTransportSecurity(hsts -> hsts
                                .maxAgeInSeconds(31536000)
                        )
                        .contentTypeOptions(Customizer.withDefaults())
                        .frameOptions(frameOptions -> frameOptions.deny())
                        .contentSecurityPolicy(csp -> csp
                                .policyDirectives("default-src 'self'; " +
                                        "style-src 'self' 'nonce-{nonce}';" +
                                        "script-src 'self' 'nonce-{nonce}';" +
                                        "img-src 'self' data: https://oskarinio143.github.io; " +
                                        "font-src 'self' https://cdnjs.cloudflare.com; " +
                                        "frame-ancestors 'none'; " +
                                        "form-action 'self'; " +
                                        "object-src 'none';")
                        )
                )
                .addFilterBefore(refreshFilter, BearerTokenAuthenticationFilter.class)
                .addFilterBefore(new CspNonceFilter(), HeaderWriterFilter.class);
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
