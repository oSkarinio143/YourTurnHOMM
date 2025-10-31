package pl.oskarinio.yourturnhomm.infrastructure.adapter.in.controller.battle;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.reactive.function.client.WebClient;
import pl.oskarinio.yourturnhomm.domain.model.Route;
import pl.oskarinio.yourturnhomm.domain.model.battle.Unit;
import pl.oskarinio.yourturnhomm.infrastructure.adapter.in.model.DuelFormRequest;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Import(DuelControllerTest.TestSecurityConfig.class) // opcjonalnie, jeśli konfiguracja jest poza klasą testową
class DuelControllerTest {

    @TestConfiguration
    static class TestSecurityConfig {
        @Bean
        @Order(Ordered.HIGHEST_PRECEDENCE) // upewnia że ta chain ma pierwszeństwo
        public SecurityFilterChain testFilterChain(HttpSecurity http) throws Exception {
            http
                    .securityMatcher("/**") // testowo dla wszystkiego
                    .authorizeHttpRequests(auth -> auth
                            .anyRequest().permitAll()
                    )
                    .csrf(csrf -> csrf.disable())
                    .headers(headers -> headers.frameOptions(frame -> frame.disable()));
            return http.build();
        }
    }

    @LocalServerPort
    int port;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Battle")
    void battleSseEmitterReq_Diagnostic() throws Exception {
        System.out.println("tutaj");
        Unit leftUnit = new Unit("leftUnit", 10, 10, 10, 10, 10, 10, "desc");
        Unit rightUnit = new Unit("rightUnit", 10, 10, 10, 10, 10, 10, "desc");

        WebClient testWebClient = WebClient.builder()
                .baseUrl("http://localhost:" + port)
                .build();

        MvcResult mvcResult = mockMvc.perform(post(Route.MAIN + Route.USER + Route.DUEL + Route.BATTLE)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("userUUID", UUID.randomUUID().toString())
                .param("leftQuantity", String.valueOf(1))
                .param("rightQuantity", String.valueOf(1))
                .param("leftHeroAttack", String.valueOf(1))
                .param("leftHeroDefense", String.valueOf(1))
                .param("rightHeroAttack", String.valueOf(1))
                .param("rightHeroDefense", String.valueOf(1))
                .param("leftUnit.name", leftUnit.getName())
                .param("leftUnit.attack", String.valueOf(leftUnit.getAttack()))
                .param("leftUnit.defense", String.valueOf(leftUnit.getDefense()))
                .param("leftUnit.minDamage", String.valueOf(leftUnit.getMinDamage()))
                .param("leftUnit.maxDamage", String.valueOf(leftUnit.getMaxDamage()))
                .param("leftUnit.hp", String.valueOf(leftUnit.getHp()))
                .param("leftUnit.speed", String.valueOf(leftUnit.getSpeed()))
                .param("leftUnit.description", leftUnit.getDescription())
                .param("rightUnit.name", rightUnit.getName())
                .param("rightUnit.attack", String.valueOf(rightUnit.getAttack()))
                .param("rightUnit.defense", String.valueOf(rightUnit.getDefense()))
                .param("rightUnit.minDamage", String.valueOf(rightUnit.getMinDamage()))
                .param("rightUnit.maxDamage", String.valueOf(rightUnit.getMaxDamage()))
                .param("rightUnit.hp", String.valueOf(rightUnit.getHp()))
                .param("rightUnit.speed", String.valueOf(rightUnit.getSpeed()))
                .param("rightUnit.description", rightUnit.getDescription()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("duelForm"))
                .andReturn();

        DuelFormRequest duelFormRequest = (DuelFormRequest) mvcResult.getModelAndView().getModel().get("duelForm");
        UUID generatedUUID = duelFormRequest.getUserUUID();

        Flux<ServerSentEvent<String>> responseSSE = testWebClient.get()
                .uri(uri -> uri
                        .path(Route.MAIN + Route.DUEL + Route.BATTLE + Route.STREAM)
                        .queryParam("userUUID", generatedUUID.toString())
                        .build())
                .retrieve()
                .bodyToFlux(new ParameterizedTypeReference<ServerSentEvent<String>>() {});

        List<String> events = responseSSE
                .map(ServerSentEvent::data)          // Flux<String>
                .filter(Objects::nonNull)
                .take(5)                             // ile chcesz sprawdzić
                .collectList()
                .block(Duration.ofSeconds(5));

        assertNotNull(events);
        assertFalse(events.isEmpty());

        Pattern p = Pattern.compile("^(ROUND|CLOSE|VICTORY|ATTACKF|ATTACKS).*");
        assertTrue(events.stream().allMatch(e -> p.matcher(e).matches()), "Nie wszystkie eventy mają dozwolony prefix: " + events);
    }
}
