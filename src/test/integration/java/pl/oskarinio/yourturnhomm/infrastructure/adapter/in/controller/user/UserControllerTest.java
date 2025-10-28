package pl.oskarinio.yourturnhomm.infrastructure.adapter.in.controller.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import pl.oskarinio.yourturnhomm.domain.model.Route;
import pl.oskarinio.yourturnhomm.domain.model.exception.UsernameNotFoundException;
import pl.oskarinio.yourturnhomm.domain.model.user.UserServiceData;
import pl.oskarinio.yourturnhomm.domain.port.out.Token;
import pl.oskarinio.yourturnhomm.domain.port.user.Admin;
import pl.oskarinio.yourturnhomm.domain.port.user.UserManagement;
import pl.oskarinio.yourturnhomm.infrastructure.adapter.in.controller.UserProfile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@UserProfile
class UserControllerTest {

    private final MockMvc mockMvc;
    private final UserManagement userManagement;
    private final Admin admin;
    private final Token token;
    private final PasswordEncoder passwordEncoder;

    private static final String USERNAME = "testUsername";
    private static final String PASSWORD = "testPassword";
    private static final String USERNAME_FIELD = "username";
    private static final String PASSWORD_FIELD = "password";
    private static final String ACCESS_TOKEN = "accessToken";
    private static final String ERROR_MESSAGE = "errorMessage";


    @Autowired
    public UserControllerTest(MockMvc mockMvc, UserManagement userManagement, Admin admin, Token token, PasswordEncoder passwordEncoder) {
        this.mockMvc = mockMvc;
        this.userManagement = userManagement;
        this.admin = admin;
        this.token = token;
        this.passwordEncoder = passwordEncoder;
    }

    @BeforeEach
    void SetUp(){
        admin.getUserList().forEach(u -> {
            admin.deleteUser(u.getUsername());
        });
    }

    @Test
    @DisplayName("Podaje poprawne dane, użytkownik zarejestrowany")
    void registerUser_correctValues_userRegistered() throws Exception {
        performRegisterRequest(USERNAME, PASSWORD, PASSWORD)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(Route.MAIN));

        assertThat(userManagement.getUserByUsernameOrThrow(USERNAME).getPassword())
                .matches(pwd -> passwordEncoder.matches(PASSWORD, pwd));
    }

    @Test
    @DisplayName("Podaje zbyt krótki username, użytkownik nie zarejestrowany")
    void registerUser_tooShortUsername_userNotRegistered() throws Exception {
        performRegisterRequest("123", PASSWORD, PASSWORD)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(Route.MAIN + Route.REGISTER))
                .andExpect(flash().attributeExists(ERROR_MESSAGE));

        assertThrows(UsernameNotFoundException.class, () -> userManagement.getUserByUsernameOrThrow(USERNAME));
    }

    @Test
    @DisplayName("Podaje zbyt długi username, użytkownik nie zarejestrowany")
    void registerUser_tooLongUsername_userNotRegistered() throws Exception {
        performRegisterRequest("abcdefghijklmnoprstuwyz", PASSWORD, PASSWORD)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(Route.MAIN + Route.REGISTER))
                .andExpect(flash().attributeExists(ERROR_MESSAGE));

        assertThrows(UsernameNotFoundException.class, () -> userManagement.getUserByUsernameOrThrow(USERNAME));
    }

    @Test
    @DisplayName("Podaje różne hasła, użytkownik nie zarejestrowany")
    void registerUser_differentPasswords_userNotRegistered() throws Exception {
        performRegisterRequest(USERNAME, "pass1", "pass2")
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(Route.MAIN + Route.REGISTER))
                .andExpect(flash().attributeExists(ERROR_MESSAGE));

        assertThrows(UsernameNotFoundException.class, () -> userManagement.getUserByUsernameOrThrow(USERNAME));
    }

    @Test
    @DisplayName("Podaje poprawne dane dla istniejącego użytkownika, użytkownik zostaje zalogowany")
    void loginUser_dataExistUser_resultUserLoggedIn() throws Exception {
        performRegisterRequest(USERNAME, PASSWORD, PASSWORD);
        MvcResult mvcResult = performLoginRequest(USERNAME, PASSWORD)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(Route.MAIN))
                .andReturn();

        Cookie accessCookie = mvcResult.getResponse().getCookie(ACCESS_TOKEN);
        assertThat(token.extractUsername(accessCookie.getValue())).isEqualTo(USERNAME);
    }

    @Test
    @DisplayName("Podaje dane dla nie istniejącego użytkownika, użytkownik nie zostaje zalogowany")
    void loginUser_dataNotExistUser_resultUserNotLoggedIn() throws Exception {
        MvcResult mvcResult = performLoginRequest(USERNAME, PASSWORD)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(Route.MAIN + Route.LOGIN))
                .andExpect(flash().attributeExists(ERROR_MESSAGE))
                .andReturn();

        Cookie accessCookie = mvcResult.getResponse().getCookie(ACCESS_TOKEN);
        assertThat(accessCookie).isNull();
    }

    @Test
    @DisplayName("Podaje dane dla istniejącego użytkownika z błędnym hasłem, użytkownik nie zostaje zalogowany")
    void loginUser_dataExistUserBadPassword_resultUserNotLoggedIn() throws Exception {
        MvcResult mvcResult = performLoginRequest(USERNAME, "falsePassword")
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(Route.MAIN + Route.LOGIN))
                .andExpect(flash().attributeExists(ERROR_MESSAGE))
                .andReturn();

        Cookie accessCookie = mvcResult.getResponse().getCookie(ACCESS_TOKEN);
        assertThat(accessCookie).isNull();
    }

    @Test
    @DisplayName("Podaje cookie z nazwą użytkownika, użytkownik został wylogowany")
    void logoutUser_cookieWithUsername_resultUserLogout() throws Exception {
        performRegisterRequest(USERNAME, PASSWORD, PASSWORD);
        String refreshCookieString = getRefreshCookieString();

        mockMvc.perform(post(Route.MAIN + Route.LOGOUT)
                        .cookie(new Cookie("refreshToken", refreshCookieString)))
                        .andExpect(status().is3xxRedirection())
                        .andExpect(redirectedUrl(Route.MAIN + Route.LOGIN))
                        .andReturn();

        assertThat(userManagement.getUserByUsernameOrThrow(USERNAME).getRefreshToken()).isNull();
    }

    @Test
    @DisplayName("Nie podaje cookie, rzuca Exception, użytkownik nie został wylogowany")
    void logoutUser_cookieEmpty_resultUserNotLogout() throws Exception {
        performRegisterRequest(USERNAME, PASSWORD, PASSWORD).andReturn();

        assertThrows(ServletException.class, () -> {
            mockMvc.perform(post(Route.MAIN + Route.LOGOUT))
                    .andExpect(status().is4xxClientError());
        });
        assertThat(userManagement.getUserByUsernameOrThrow(USERNAME).getRefreshToken()).isNotNull();
    }

    private ResultActions performLoginRequest(String username, String password) throws Exception {
        return mockMvc.perform(post(Route.MAIN + Route.LOGIN)
                .param(USERNAME_FIELD, username)
                .param(PASSWORD_FIELD, password));
    }

    private ResultActions performRegisterRequest(String username, String password, String confirmPassword) throws Exception {
        return mockMvc.perform(post(Route.MAIN + Route.REGISTER)
                .param(USERNAME_FIELD, username)
                .param(PASSWORD_FIELD, password)
                .param("confirmPassword", confirmPassword));
    }

    private String getRefreshCookieString(){
        String refreshToken = token.generateToken(new UserServiceData(USERNAME,PASSWORD),60);
        ResponseCookie refreshResponseCookie = ResponseCookie.from(refreshToken).build();
        return refreshResponseCookie.toString();
    }
}
