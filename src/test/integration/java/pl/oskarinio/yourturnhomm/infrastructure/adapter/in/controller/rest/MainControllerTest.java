package pl.oskarinio.yourturnhomm.infrastructure.adapter.in.controller.rest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import pl.oskarinio.yourturnhomm.domain.model.Route;
import pl.oskarinio.yourturnhomm.infrastructure.adapter.in.controller.MvcControllerTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@MvcControllerTest(controllers = MainController.class)
class MainControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private static final String ERROR_MESSAGE = "adminErrorMessage";

    @Test
    @DisplayName("Request zawiera wiadomość błędu, zwraca widok z modalem")
    void welcome_requestHasAdminError_resultReturnViewModal() throws Exception {
        String errorMessageValue = "testError";
        mockMvc.perform(get(Route.MAIN).requestAttr(ERROR_MESSAGE, errorMessageValue))
                .andExpect(status().isOk())
                .andExpect(view().name(Route.VIEW_MODE))
                .andExpect(model().attribute(ERROR_MESSAGE, errorMessageValue));
    }

    @Test
    @DisplayName("Request nie zawiera wiadomości błędu, zwraca widok bez modala")
    void welocme_requestNoAdminError_resultReturnView() throws Exception {
        mockMvc.perform(get(Route.MAIN))
                .andExpect(status().isOk())
                .andExpect(view().name(Route.VIEW_MODE))
                .andExpect(model().attributeDoesNotExist(ERROR_MESSAGE));
    }
}
