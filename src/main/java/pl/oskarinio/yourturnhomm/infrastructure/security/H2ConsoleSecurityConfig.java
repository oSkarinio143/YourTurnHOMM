package pl.oskarinio.yourturnhomm.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@Configuration
@Profile("h2")
public class H2ConsoleSecurityConfig {
    @Bean
    @Order(1) // WAŻNE: Wyższy priorytet, aby ta reguła była sprawdzana jako pierwsza
    public SecurityFilterChain h2ConsoleSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Zastosuj tę konfigurację TYLKO do ścieżki konsoli H2
                .securityMatcher(toH2Console())

                // 2. Zezwól na wszystkie żądania do konsoli
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())

                // 3. Wyłącz CSRF dla konsoli
                .csrf(AbstractHttpConfigurer::disable)

                // 4. Zezwól na ramki (<iframe>), ale NIE ustawiaj restrykcyjnego CSP
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

        return http.build();
    }
}

