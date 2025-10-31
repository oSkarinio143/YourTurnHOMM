package pl.oskarinio.yourturnhomm.infrastructure.adapter.in.controller.battle;

// NOWE IMPORTY - dodaj je na górze pliku

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import pl.oskarinio.yourturnhomm.domain.model.Route;
import pl.oskarinio.yourturnhomm.infrastructure.adapter.in.controller.UserProfile;
import pl.oskarinio.yourturnhomm.infrastructure.port.communication.Communication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureWebTestClient // Można zostawić, jeśli inne testy tego potrzebują
@UserProfile
class CommunicationControllerTestAi {
    private final MockMvc mockMvc;
    private final Communication communication;
    // Nie potrzebujemy już pola WebTestClient dla tego konkretnego testu

    // KROK 1: Wstrzyknij losowy port, na którym działa serwer testowy
    @LocalServerPort
    private int port;

    // KROK 2: Zadeklaruj pole na standardowy, reaktywny WebClient
    private WebClient webClient;

    @Autowired
    public CommunicationControllerTestAi(MockMvc mockMvc, Communication communication) {
        this.mockMvc = mockMvc;
        this.communication = communication;
    }

    // KROK 3: Przed każdym testem skonfiguruj WebClient tak, aby wskazywał na nasz serwer testowy
    @BeforeEach
    void setUp() {
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:" + port)
                .build();
    }


    @Test
    @DisplayName("Should generate UUID correctly")
    void generateUUID_() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(Route.MAIN + Route.DUEL + Route.GENERATEUUID))
                .andExpect(status().isOk())
                .andReturn();

        String stringUUID = mvcResult.getResponse().getContentAsString().substring(1,36);
        UUID generatedUUID = UUID.fromString(stringUUID);

        assertThat(generatedUUID).isNotNull();
    }

    @Test
    @DisplayName("Simple request")
    void simpleReq(){
        WebClient testWebClient = WebClient.builder()
                .baseUrl("http://localhost:" + port)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        Mono<String> response = testWebClient.post()
                .uri(Route.MAIN + Route.LOGIN)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("username","testUsername")
                        .with("password", "12345"))
                .retrieve()
                .bodyToMono(String.class);
        String body = response.block();
    }

    @Test
    @DisplayName("Simple request")
    void simpleReqFlux(){
        WebClient testWebClient = WebClient.builder()
                .baseUrl("http://localhost:" + port)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        Flux<ServerSentEvent<String>> response = testWebClient.post()
                .uri(Route.MAIN + Route.LOGIN)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("username","testUsername")
                        .with("password", "12345"))
                .retrieve()
                .bodyToFlux(new ParameterizedTypeReference<ServerSentEvent<String>>() {});
        ServerSentEvent<String> first = response.next().block(Duration.ofSeconds(5));
    }
/*
    @Test
    @DisplayName("Simple battle request")
    @AdminProfile
    void simpleBatReq(){
        Unit leftUnit = new Unit("leftUnit", 10, 10, 10, 10, 10, 10, "desc");
        leftUnit.setImagePath("/");
        Unit rightUnit = new Unit("rightUnit", 10, 10, 10, 10, 10, 10, "desc");
        rightUnit.setImagePath("/");
        UUID generatedUUID = UUID.randomUUID();


        WebClient testWebClient = WebClient.builder()
                .baseUrl("http://localhost:" + port)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        Flux<ServerSentEvent<String>> response = testWebClient.post()
                .uri(Route.MAIN + Route.USER + Route.DUEL + Route.BATTLE)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("userUUID", generatedUUID.toString())
                        .with("leftQuantity", String.valueOf(1))
                        .with("rightQuantity", String.valueOf(1))
                        .with("leftHeroAttack", String.valueOf(1))
                        .with("leftHeroDefense", String.valueOf(1))
                        .with("rightHeroAttack", String.valueOf(1))
                        .with("rightHeroDefense", String.valueOf(1))

                        .with("leftUnit.name", leftUnit.getName())
                        .with("leftUnit.attack", String.valueOf(leftUnit.getAttack()))
                        .with("leftUnit.defense", String.valueOf(leftUnit.getDefense()))
                        .with("leftUnit.minDamage", String.valueOf(leftUnit.getMinDamage()))
                        .with("leftUnit.maxDamage", String.valueOf(leftUnit.getMaxDamage()))
                        .with("leftUnit.hp", String.valueOf(leftUnit.getHp()))
                        .with("leftUnit.speed", String.valueOf(leftUnit.getSpeed()))
                        .with("leftUnit.description", leftUnit.getDescription())

                        .with("rightUnit.name", rightUnit.getName())
                        .with("rightUnit.attack", String.valueOf(rightUnit.getAttack()))
                        .with("rightUnit.defense", String.valueOf(rightUnit.getDefense()))
                        .with("rightUnit.minDamage", String.valueOf(rightUnit.getMinDamage()))
                        .with("rightUnit.maxDamage", String.valueOf(rightUnit.getMaxDamage()))
                        .with("rightUnit.hp", String.valueOf(rightUnit.getHp()))
                        .with("rightUnit.speed", String.valueOf(rightUnit.getSpeed()))
                        .with("rightUnit.description", rightUnit.getDescription())
                )
                .retrieve()
                .bodyToFlux(new ParameterizedTypeReference<ServerSentEvent<String>>() {});
        ServerSentEvent<String> first = response.next().block(Duration.ofSeconds(5));
    }

    @Test
    @DisplayName("Diagnostic battle request")
    @AdminProfile
    void simpleBatReq_Diagnostic() {
        WebClient testWebClient = WebClient.builder()
                .baseUrl("http://localhost:" + port)
                .build();


        ClientResponse loginResp = testWebClient.post()
                .uri(Route.MAIN + Route.REGISTER)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("username","testUsername")
                        .with("password","12345")
                        .with("confirmPassword", "12345"))
                .exchangeToMono(Mono::just)
                .block(Duration.ofSeconds(5));


        System.out.println("login cookies " + loginResp.cookies());
        MultiValueMap<String, ResponseCookie> cookies = loginResp.cookies();
        String session = cookies.get("accessToken").get(0).toString().substring(12); // nazwa zależy od cfg (SESSION / JSESSIONID)
        System.out.println("Sesja " + session);

        Unit leftUnit = new Unit("leftUnit", 10, 10, 10, 10, 10, 10, "desc");
        leftUnit.setImagePath("/");
        Unit rightUnit = new Unit("rightUnit", 10, 10, 10, 10, 10, 10, "desc");
        rightUnit.setImagePath("/");
        UUID generatedUUID = UUID.randomUUID();

        String fullUrl = Route.MAIN + Route.USER + Route.DUEL + Route.BATTLE;
        System.out.println("WYSYŁAM ŻĄDANIE POST NA DOKŁADNY URL: " + fullUrl);

        try {
            String responseBody = testWebClient.post()
                    .uri(fullUrl)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData("userUUID", generatedUUID.toString())
                            .with("leftQuantity", String.valueOf(1))
                            .with("rightQuantity", String.valueOf(1))
                            .with("leftHeroAttack", String.valueOf(1))
                            .with("leftHeroDefense", String.valueOf(1))
                            .with("rightHeroAttack", String.valueOf(1))
                            .with("rightHeroDefense", String.valueOf(1))

                            .with("leftUnit.name", leftUnit.getName())
                            .with("leftUnit.attack", String.valueOf(leftUnit.getAttack()))
                            .with("leftUnit.defense", String.valueOf(leftUnit.getDefense()))
                            .with("leftUnit.minDamage", String.valueOf(leftUnit.getMinDamage()))
                            .with("leftUnit.maxDamage", String.valueOf(leftUnit.getMaxDamage()))
                            .with("leftUnit.hp", String.valueOf(leftUnit.getHp()))
                            .with("leftUnit.speed", String.valueOf(leftUnit.getSpeed()))
                            .with("leftUnit.description", leftUnit.getDescription())

                            .with("rightUnit.name", rightUnit.getName())
                            .with("rightUnit.attack", String.valueOf(rightUnit.getAttack()))
                            .with("rightUnit.defense", String.valueOf(rightUnit.getDefense()))
                            .with("rightUnit.minDamage", String.valueOf(rightUnit.getMinDamage()))
                            .with("rightUnit.maxDamage", String.valueOf(rightUnit.getMaxDamage()))
                            .with("rightUnit.hp", String.valueOf(rightUnit.getHp()))
                            .with("rightUnit.speed", String.valueOf(rightUnit.getSpeed()))
                            .with("rightUnit.description", rightUnit.getDescription())
                    )
                    .exchangeToMono(response -> {
                        System.out.println("================= DIAGNOSTYKA ODPOWIEDZI ==================");
                        System.out.println("Otrzymano status HTTP: " + response.statusCode());
                        System.out.println("Nagłówki odpowiedzi: ");
                        response.headers().asHttpHeaders().forEach((name, values) -> {
                            System.out.println("\t" + name + ": " + values);
                        });

                        return response.bodyToMono(String.class)
                                .doOnNext(body -> {
                                    System.out.println("Ciało odpowiedzi: \n" + body);
                                    System.out.println("==========================================================");
                                })
                                .defaultIfEmpty("[Brak ciała odpowiedzi]");
                    })
                    .block(Duration.ofSeconds(10));
        } catch (Exception e) {
            System.err.println("WYSTĄPIŁ BŁĄD PODCZAS WYKONYWANIA ŻĄDANIA: " + e.getMessage());
            e.printStackTrace();
        }
    }*/
}
