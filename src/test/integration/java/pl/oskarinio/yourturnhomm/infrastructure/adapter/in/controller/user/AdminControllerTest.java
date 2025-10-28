package pl.oskarinio.yourturnhomm.infrastructure.adapter.in.controller.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import pl.oskarinio.yourturnhomm.domain.model.Route;
import pl.oskarinio.yourturnhomm.domain.model.exception.UsernameNotFoundException;
import pl.oskarinio.yourturnhomm.domain.model.user.Role;
import pl.oskarinio.yourturnhomm.domain.port.user.UserManagement;
import pl.oskarinio.yourturnhomm.infrastructure.adapter.in.controller.AdminProfile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AdminProfile
class AdminControllerTest {
    private final MockMvc mockMvc;
    private final UserManagement userManagement;


    private static final String MAIN_ADMIN_USERNAME = "testMainAdmin";
    private static final String ADMIN_USERNAME = "testAdmin";
    private static final String USERNAME = "testUsername";
    private static final String PASSWORD = "testPassword";
    private static final String USERNAME_FIELD = "username";
    private static final String ROUTE_ADMIN = Route.MAIN + Route.ADMIN;

    @Autowired
    public AdminControllerTest(MockMvc mockMvc, UserManagement userManagement) {
        this.mockMvc = mockMvc;
        this.userManagement = userManagement;
    }

    @Test
    @DisplayName("Podaje dane zwykłego użytkownika, użytkownik zostaje usunięty")
    void deleteUser_dataCommonUser_resultUserDeleted() throws Exception {
        performRegisterRequest(USERNAME, PASSWORD, PASSWORD);
        performAdminRequest(Route.DELETE, USERNAME);

        assertThrows(UsernameNotFoundException.class, () -> userManagement.getUserByUsernameOrThrow(USERNAME));
    }

    @Test
    @DisplayName("Podaje dane admina, admin zostaje usunięty")
    void deleteUser_dataAdmin_resultAdminNotDeleted() throws Exception {
        performRegisterRequest(ADMIN_USERNAME, PASSWORD, PASSWORD);
        performAdminRequest(Route.DELETE, ADMIN_USERNAME);

        assertThrows(UsernameNotFoundException.class, () -> userManagement.getUserByUsernameOrThrow(ADMIN_USERNAME));
    }

    @Test
    @DisplayName("Podaje dane głównego admina, główny admin nie zostaje usunięty")
    void deleteUser_dataMainAdmin_resultAdminNotDeleted() throws Exception {
        performRegisterRequest(MAIN_ADMIN_USERNAME, PASSWORD, PASSWORD);
        performAdminRequest(Route.DELETE, MAIN_ADMIN_USERNAME);

        assertThat(userManagement.getUserByUsernameOrThrow(MAIN_ADMIN_USERNAME)).isNotNull();
    }

    @Test
    @DisplayName("Podaje dane zwykłego użytkownika, rola admina zostaje przyznana")
    void grantAdmin_dataCommonUser_resultAdminRoleAdded() throws Exception {
        performRegisterRequest(USERNAME, PASSWORD, PASSWORD);
        performAdminRequest(Route.GRANT, USERNAME);

        assertThat(userManagement.getUserByUsernameOrThrow(USERNAME).getRoles()).contains(Role.ROLE_ADMIN);
    }

    @Test
    @DisplayName("Podaje dane admina, nic się nie dzieje")
    void grantAdmin_dataAdmin_resultNothingHappened() throws Exception {
        performRegisterRequest(ADMIN_USERNAME, PASSWORD, PASSWORD);
        performAdminRequest(Route.GRANT, ADMIN_USERNAME);

        assertThat(userManagement.getUserByUsernameOrThrow(ADMIN_USERNAME).getRoles()).contains(Role.ROLE_ADMIN);
    }

    private void performAdminRequest(String route, String paramValue) throws Exception {
        mockMvc.perform(post(ROUTE_ADMIN + route)
                        .param(USERNAME_FIELD, paramValue))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(ROUTE_ADMIN + route));
    }

    private void performRegisterRequest(String username, String password, String confirmPassword) throws Exception {
        mockMvc.perform(post(Route.MAIN + Route.REGISTER)
                .param(USERNAME_FIELD, username)
                .param("password", password)
                .param("confirmPassword", confirmPassword));
    }
}
