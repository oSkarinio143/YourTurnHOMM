package pl.oskarinio.yourturnhomm.infrastructure.adapter.in.controller.rest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import pl.oskarinio.yourturnhomm.domain.model.Route;
import pl.oskarinio.yourturnhomm.infrastructure.adapter.in.controller.MvcControllerTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@MvcControllerTest(controllers = CustomErrorController.class)
class CustomErrorControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Ustawiam scieżkę error, dostaje przekierowanie na main")
    void handleError_setErrorPath_resultRedirectToMain() throws Exception {
        mockMvc.perform(get(Route.ERROR))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(Route.MAIN));
    }
}
