package pl.oskarinio.yourturnhomm.infrastructure.adapter.in.controller.battle;

import jakarta.servlet.ServletException;
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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.reactive.function.client.WebClient;
import pl.oskarinio.yourturnhomm.domain.model.Route;
import pl.oskarinio.yourturnhomm.domain.model.battle.Side;
import pl.oskarinio.yourturnhomm.domain.model.battle.Unit;
import pl.oskarinio.yourturnhomm.domain.model.form.DuelForm;
import pl.oskarinio.yourturnhomm.infrastructure.adapter.in.model.DuelFormRequest;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Import(DuelControllerTest.TestSecurityConfig.class)
class DuelControllerTest {
    @TestConfiguration
    static class TestSecurityConfig {
        @Bean
        @Order(Ordered.HIGHEST_PRECEDENCE)
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
    private int port;

    @Autowired
    private MockMvc mockMvc;

    private static final Unit LEFT_UNIT = new Unit("leftUnit", 10, 10, 10, 10, 10, 10, "desc");
    private static final Unit RIGHT_UNIT = new Unit("rightUnit", 10, 10, 10, 10, 10, 10, "desc");

    private static final String BASIC_ROUTE = Route.MAIN + Route.USER + Route.DUEL;
    private static final String DUEL_FORM = "duelForm";
    private static final String USER_UUID = "userUUID";
    private static final String TEMP_UNIT_NAME_VALUE = "Aniol";
    private static final String LEFT_QUANTITY = "leftQuantity";
    private static final String RIGHT_QUANTITY = "rightQuantity";
    private static final String LEFT_HERO_ATTACK = "leftHeroAttack";
    private static final String LEFT_HERO_DEFENSE = "leftHeroDefense";
    private static final String RIGHT_HERO_ATTACK = "rightHeroAttack";
    private static final String RIGHT_HERO_DEFENSE = "rightHeroDefense";
    private static final String LEFT_UNIT_NAME = "leftUnit.name";
    private static final String LEFT_UNIT_ATTACK = "leftUnit.attack";
    private static final String LEFT_UNIT_DEFENSE = "leftUnit.defense";
    private static final String LEFT_UNIT_MIN_DAMAGE = "leftUnit.minDamage";
    private static final String LEFT_UNIT_MAX_DAMAGE = "leftUnit.maxDamage";
    private static final String LEFT_UNIT_HP = "leftUnit.hp";
    private static final String LEFT_UNIT_SPEED = "leftUnit.speed";
    private static final String LEFT_UNIT_DESCRIPTION = "leftUnit.description";
    private static final String RIGHT_UNIT_NAME = "rightUnit.name";
    private static final String RIGHT_UNIT_ATTACK = "rightUnit.attack";
    private static final String RIGHT_UNIT_DEFENSE = "rightUnit.defense";
    private static final String RIGHT_UNIT_MIN_DAMAGE = "rightUnit.minDamage";
    private static final String RIGHT_UNIT_MAX_DAMAGE = "rightUnit.maxDamage";
    private static final String RIGHT_UNIT_HP = "rightUnit.hp";
    private static final String RIGHT_UNIT_SPEED = "rightUnit.speed";
    private static final String RIGHT_UNIT_DESCRIPTION = "rightUnit.description";

    private static final String SIDE = "side";
    private static final String TEMP_UNIT_NAME = "tempUnitName";
    private static final String LEFT_QUANTITY_VALUE = "1";
    private static final String LEFT_HERO_ATTACK_VALUE = "1";
    private static final String LEFT_HERO_DEFENSE_VALUE = "1";
    private static final String LEFT_UNIT_NAME_VALUE = LEFT_UNIT.getName();
    private static final String LEFT_UNIT_ATTACK_VALUE = LEFT_UNIT.getAttack().toString();
    private static final String LEFT_UNIT_DEFENSE_VALUE = LEFT_UNIT.getDefense().toString();
    private static final String LEFT_UNIT_MIN_DAMAGE_VALUE = LEFT_UNIT.getMinDamage().toString();
    private static final String LEFT_UNIT_MAX_DAMAGE_VALUE = LEFT_UNIT.getMaxDamage().toString();
    private static final String LEFT_UNIT_HP_VALUE = LEFT_UNIT.getHp().toString();
    private static final String LEFT_UNIT_SPEED_VALUE = LEFT_UNIT.getSpeed().toString();
    private static final String LEFT_UNIT_DESC_VALUE = LEFT_UNIT.getDescription();
    private static final String RIGHT_QUANTITY_VALUE = "1";
    private static final String RIGHT_HERO_ATTACK_VALUE = "1";
    private static final String RIGHT_HERO_DEFENSE_VALUE = "1";
    private static final String RIGHT_UNIT_NAME_VALUE = RIGHT_UNIT.getName();
    private static final String RIGHT_UNIT_ATTACK_VALUE = RIGHT_UNIT.getAttack().toString();
    private static final String RIGHT_UNIT_DEFENSE_VALUE = RIGHT_UNIT.getDefense().toString();
    private static final String RIGHT_UNIT_MIN_DAMAGE_VALUE = RIGHT_UNIT.getMinDamage().toString();
    private static final String RIGHT_UNIT_MAX_DAMAGE_VALUE = RIGHT_UNIT.getMaxDamage().toString();
    private static final String RIGHT_UNIT_HP_VALUE = RIGHT_UNIT.getHp().toString();
    private static final String RIGHT_UNIT_SPEED_VALUE = RIGHT_UNIT.getSpeed().toString();
    private static final String RIGHT_UNIT_DESC_VALUE = RIGHT_UNIT.getDescription();

    @Test
    @DisplayName("Przesłane wszystkie wartości duelForm, brak side i newUnitName, duelForm dodany do modelu")
    void loadUnit_sendDuelForm_resultDuelFormSet() throws Exception {
        MvcResult mvcResult = sendRequestWithDuelForm(BASIC_ROUTE, Optional.empty(), Optional.empty());

        DuelForm duelForm = (DuelForm) mvcResult.getModelAndView().getModel().get(DUEL_FORM);
        assertThat(duelForm.getLeftUnit().getName()).isEqualTo((LEFT_UNIT_NAME_VALUE));
    }

    @Test
    @DisplayName("Przesłane wszystkie wartości duelForm, newUnit, Side, duelForm dodany do modelu z nową jednostką ustawioną")
    void loadUnit_sendDuelFormSideNewUnit_resultDuelFormSet() throws Exception {
        MvcResult mvcResult = sendRequestWithDuelForm(BASIC_ROUTE, Optional.of(String.valueOf(Side.LEFT)), Optional.of(TEMP_UNIT_NAME_VALUE));

        DuelForm duelForm = (DuelForm) mvcResult.getModelAndView().getModel().get(DUEL_FORM);
        assertThat(duelForm.getLeftUnit().getName()).isEqualTo(TEMP_UNIT_NAME_VALUE);
    }

    @Test
    @DisplayName("Przesłanie wszystkich wartości duelForm,  side, niepoprawnego newUnit, rzuca exception, duelForm nie dodany do modelu")
    void loadUnit_sendDuelFormSideIncorrectNewUnit_resultDuelFormSet(){
        assertThrows(ServletException.class, () -> sendRequestWithDuelForm(BASIC_ROUTE, Optional.of(String.valueOf(Side.LEFT)), Optional.of("incorrectUnitName")));
    }

    @Test
    @DisplayName("Brak duelForm, przesłanie newUnit, side, duelForm dodany do modelu z nową jednostką ustawioną")
    void loadUnit_emptyDuelFormSendNewUnitSide_resultDuelFormSet() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post(Route.MAIN + Route.USER + Route.DUEL)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(SIDE, String.valueOf(Side.LEFT))
                .param(TEMP_UNIT_NAME, TEMP_UNIT_NAME_VALUE))
                .andReturn();

        DuelForm duelForm = (DuelForm) mvcResult.getModelAndView().getModel().get(DUEL_FORM);
        assertThat(duelForm.getLeftUnit().getName()).isEqualTo(TEMP_UNIT_NAME_VALUE);
    }

    @Test
    @DisplayName("Przesłanie wszytkich wartości duelForm, duelForm dodany do modelu, emitter emituje poprawne wiadomości")
    void loadBattle_sendDuelForm_resultEmitterSendMessages() throws Exception {
        WebClient testWebClient = WebClient.builder()
                .baseUrl("http://localhost:" + port)
                .build();

        MvcResult mvcResult = sendRequestWithDuelForm(BASIC_ROUTE + Route.BATTLE, Optional.empty(),Optional.empty());

        DuelFormRequest duelFormRequest = (DuelFormRequest) mvcResult.getModelAndView().getModel().get(DUEL_FORM);
        UUID generatedUUID = duelFormRequest.getUserUUID();

        Flux<ServerSentEvent<String>> responseSSE = testWebClient.get()
                .uri(uri -> uri
                        .path(Route.MAIN + Route.DUEL + Route.BATTLE + Route.STREAM)
                        .queryParam(USER_UUID, generatedUUID.toString())
                        .build())
                .retrieve()
                .bodyToFlux(new ParameterizedTypeReference<ServerSentEvent<String>>() {});

        List<String> events = responseSSE
                .map(ServerSentEvent::data)
                .filter(Objects::nonNull)
                .take(5)
                .collectList()
                .block(Duration.ofSeconds(5));

        assertNotNull(events);
        assertFalse(events.isEmpty());

        Pattern p = Pattern.compile("^(ROUND|CLOSE|VICTORY|ATTACKF|ATTACKS).*");
        assertTrue(events.stream().allMatch(e -> p.matcher(e).matches()));
    }

    @Test
    @DisplayName("Przesłanie niewszytkich wartości duelForm, duel form nie dodany do modelu, brak wiadomości")
    void loadBattle_nullValuesInDuelForm_duelFormNotAddedToModel() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post(BASIC_ROUTE + Route.BATTLE)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)

                .param(USER_UUID, UUID.randomUUID().toString())
                .param(LEFT_QUANTITY, LEFT_QUANTITY_VALUE)
                .param(RIGHT_QUANTITY, RIGHT_QUANTITY_VALUE)
                .param(LEFT_HERO_ATTACK, LEFT_HERO_ATTACK_VALUE)
                .param(LEFT_HERO_DEFENSE, LEFT_HERO_DEFENSE_VALUE)
                .param(RIGHT_HERO_ATTACK, RIGHT_HERO_ATTACK_VALUE)
                .param(RIGHT_HERO_DEFENSE, RIGHT_HERO_DEFENSE_VALUE)

                .param(RIGHT_UNIT_NAME, RIGHT_UNIT_NAME_VALUE)
                .param(RIGHT_UNIT_ATTACK, RIGHT_UNIT_ATTACK_VALUE)
                .param(RIGHT_UNIT_DEFENSE, RIGHT_UNIT_DEFENSE_VALUE)
                .param(RIGHT_UNIT_MIN_DAMAGE, RIGHT_UNIT_MIN_DAMAGE_VALUE)
                .param(RIGHT_UNIT_MAX_DAMAGE, RIGHT_UNIT_MAX_DAMAGE_VALUE)
                .param(RIGHT_UNIT_HP, RIGHT_UNIT_HP_VALUE)
                .param(RIGHT_UNIT_SPEED, RIGHT_UNIT_SPEED_VALUE)
                .param(RIGHT_UNIT_DESCRIPTION, RIGHT_UNIT_DESC_VALUE))
                .andReturn();

        DuelFormRequest duelFormRequest = (DuelFormRequest) mvcResult.getModelAndView().getModel().get(DUEL_FORM);
        assertThat(duelFormRequest).isNull();
    }

    private MvcResult sendRequestWithDuelForm(String Route, Optional<String> side, Optional<String> newUnitName) throws Exception {
        MockHttpServletRequestBuilder requestBuilder = post(Route)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)

                        .param(USER_UUID, UUID.randomUUID().toString())
                        .param(LEFT_QUANTITY, LEFT_QUANTITY_VALUE)
                        .param(RIGHT_QUANTITY, RIGHT_QUANTITY_VALUE)
                        .param(LEFT_HERO_ATTACK, LEFT_HERO_ATTACK_VALUE)
                        .param(LEFT_HERO_DEFENSE, LEFT_HERO_DEFENSE_VALUE)
                        .param(RIGHT_HERO_ATTACK, RIGHT_HERO_ATTACK_VALUE)
                        .param(RIGHT_HERO_DEFENSE, RIGHT_HERO_DEFENSE_VALUE)

                        .param(LEFT_UNIT_NAME, LEFT_UNIT_NAME_VALUE)
                        .param(LEFT_UNIT_ATTACK, LEFT_UNIT_ATTACK_VALUE)
                        .param(LEFT_UNIT_DEFENSE, LEFT_UNIT_DEFENSE_VALUE)
                        .param(LEFT_UNIT_MIN_DAMAGE, LEFT_UNIT_MIN_DAMAGE_VALUE)
                        .param(LEFT_UNIT_MAX_DAMAGE, LEFT_UNIT_MAX_DAMAGE_VALUE)
                        .param(LEFT_UNIT_HP, LEFT_UNIT_HP_VALUE)
                        .param(LEFT_UNIT_SPEED, LEFT_UNIT_SPEED_VALUE)
                        .param(LEFT_UNIT_DESCRIPTION, LEFT_UNIT_DESC_VALUE)

                        .param(RIGHT_UNIT_NAME, RIGHT_UNIT_NAME_VALUE)
                        .param(RIGHT_UNIT_ATTACK, RIGHT_UNIT_ATTACK_VALUE)
                        .param(RIGHT_UNIT_DEFENSE, RIGHT_UNIT_DEFENSE_VALUE)
                        .param(RIGHT_UNIT_MIN_DAMAGE, RIGHT_UNIT_MIN_DAMAGE_VALUE)
                        .param(RIGHT_UNIT_MAX_DAMAGE, RIGHT_UNIT_MAX_DAMAGE_VALUE)
                        .param(RIGHT_UNIT_HP, RIGHT_UNIT_HP_VALUE)
                        .param(RIGHT_UNIT_SPEED, RIGHT_UNIT_SPEED_VALUE)
                        .param(RIGHT_UNIT_DESCRIPTION, RIGHT_UNIT_DESC_VALUE);

        side.ifPresent(v -> requestBuilder.param(SIDE, v));
        newUnitName.ifPresent(v -> requestBuilder.param(TEMP_UNIT_NAME, v));

        return mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(model().attributeExists(DUEL_FORM))
                .andReturn();
    }
}
