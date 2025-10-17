package pl.oskarinio143.yourturnhomm.infrastructure.adapter.in.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import pl.oskarinio.yourturnhomm.domain.model.Route;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = MainControllerTest.class)
class MainControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private static final String ERROR_MESSAGE = "adminErrorMessage";

    @Test
    void welcome_requestHasAdminError_resultReturnViewModal() throws Exception {
        String errorMessageValue = "testError";
        mockMvc.perform(get(Route.MAIN).requestAttr(ERROR_MESSAGE,errorMessageValue))
                .andExpect(status().isOk())
                .andExpect(view().name(Route.VIEW_MODE))
                .andExpect(model().attribute(ERROR_MESSAGE, errorMessageValue));
    }

    @Test
    void welocme_requestNoAdminError_resultReturnView() throws Exception {
        mockMvc.perform(get(Route.MAIN))
                .andExpect(status().isOk())
                .andExpect(view().name(Route.VIEW_MODE))
                .andExpect(model().attributeDoesNotExist(ERROR_MESSAGE));
    }
}
