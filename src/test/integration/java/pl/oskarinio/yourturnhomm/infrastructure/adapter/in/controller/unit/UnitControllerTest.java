package pl.oskarinio.yourturnhomm.infrastructure.adapter.in.controller.unit;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import pl.oskarinio.yourturnhomm.domain.model.Route;
import pl.oskarinio.yourturnhomm.domain.model.battle.Unit;
import pl.oskarinio.yourturnhomm.domain.port.unit.UnitManagement;
import pl.oskarinio.yourturnhomm.infrastructure.adapter.in.controller.AdminProfile;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AdminProfile
@SpringBootTest
@AutoConfigureMockMvc
class UnitControllerTest {
    private final MockMvc mockMvc;
    private final UnitManagement unitManagement;

    private final MockMultipartFile IMAGE = getTestFile();
    private static final String UNIT_NAME = "testUnit";
    private static final String ATTACK = "1";
    private static final String DEFENSE = "1";
    private static final String MIN_DAMAGE = "1";
    private static final String MAX_DAMAGE = "1";
    private static final String HP = "1";
    private static final String SPEED = "1";
    private static final String DESCRIPTION = "testing";
    private static final String IMAGE_PATH = "imagePath";
    private static final String INCORRECT_ATTACK = "-1";
    private static final String INCORRECT_MIN_DAMAGE = "99";
    private static final String INCORRECT_MESSAGE = "incorrectMessage";
    private static final String DATABASE_ADMIN_PATH = Route.MAIN + Route.ADMIN + Route.DATABASE;
    private static final String DATABASE_USER_PATH = Route.MAIN + Route.USER + Route.DATABASE;
    @BeforeEach
    void setUp(){
        clearRepo();
    }

    @Autowired
    public UnitControllerTest(MockMvc mockMvc, UnitManagement unitManagement) {
        this.mockMvc = mockMvc;
        this.unitManagement = unitManagement;
    }

    @Test
    @DisplayName("przekazuje poprawne wartości, jednostka zostaje dodana")
    void addUnit_passCorrectValues_resultUnitAdded() throws Exception {
        performAddRequest()
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(DATABASE_USER_PATH));

        assertThat(unitManagement.getSingleUnit(UNIT_NAME)).isNotNull();
    }

    @Test
    @DisplayName("przekazuje niepoprawną wartość (attack = -1), jednostka nie zostaje dodana, blad walidacji")
    void addUnit_passIncorrectAttackValue_resultException() throws Exception {
        performAddRequest(INCORRECT_ATTACK, DEFENSE, MIN_DAMAGE, MAX_DAMAGE, HP, SPEED, DESCRIPTION)
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists(INCORRECT_MESSAGE))
                .andExpect(redirectedUrl(DATABASE_ADMIN_PATH + Route.ADD));

        assertThrows(EntityNotFoundException.class, () -> unitManagement.getSingleUnit(UNIT_NAME));
    }

    @Test
    @DisplayName("przekazuje niepoprawną wartość (minDamage > maxDamage), jednostka nie zostaje dodana, blad walidacji")
    void addUnit_passIncorrectDamageValue_resultException() throws Exception {
        performAddRequest(ATTACK, DEFENSE, INCORRECT_MIN_DAMAGE, MAX_DAMAGE, HP, SPEED, DESCRIPTION)
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists(INCORRECT_MESSAGE))
                .andExpect(redirectedUrl(DATABASE_ADMIN_PATH + Route.ADD));

        assertThrows(EntityNotFoundException.class, () -> unitManagement.getSingleUnit(UNIT_NAME));
    }

    @Test
    @DisplayName("dodaje dwie jednostki o tej samej nazwie, jednostka nie zostaje dodana, blad logiki biznesowej")
    void addUnit_twoSameUnitName_resultException() throws Exception {
        performAddRequest();
        performAddRequest()
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("duplicateMessage"))
                .andExpect(redirectedUrl(DATABASE_ADMIN_PATH + Route.ADD));
    }

    @Test
    @DisplayName("modyfikuje przekazując poprawne wartości, jednostka zostaje zmodyfikowana")
    void modifyUnit_passCorrectValues_resultUnitModified() throws Exception {
        performAddRequest();
        performModifyRequest(UNIT_NAME, ATTACK, DEFENSE, MIN_DAMAGE, MAX_DAMAGE, HP, SPEED, DESCRIPTION, IMAGE_PATH)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(DATABASE_ADMIN_PATH + Route.MODIFY));

        assertThat(unitManagement.getSingleUnit(UNIT_NAME).getAttack()).hasToString(ATTACK);
    }

    @Test
    @DisplayName("modyfikuje przekazując niepoprawną wartość (attack = -1), jednostka nie zostaje zmodyfikowana")
    void modifyUnit_passInorrectAttackValue_resultUnitNotModified() throws Exception {
        performAddRequest();
        performModifyRequest(UNIT_NAME, INCORRECT_ATTACK, DEFENSE, MIN_DAMAGE, MAX_DAMAGE, HP, SPEED, DESCRIPTION, IMAGE_PATH)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern(DATABASE_ADMIN_PATH + Route.MODIFY + Route.UNIT + "*"));

        assertThat(unitManagement.getSingleUnit(UNIT_NAME).getAttack()).hasToString(ATTACK);
    }

    @Test
    @DisplayName("modyfikuje przekazując niepoprawną wartość (minDamage > maxDamage), jednostka nie zostaje zmodyfikowana")
    void modifyUnit_passIncorrectDamageValue_resultUnitNotModified() throws Exception {
        performAddRequest();
        performModifyRequest(UNIT_NAME, ATTACK, DEFENSE, INCORRECT_MIN_DAMAGE, MAX_DAMAGE, HP, SPEED, DESCRIPTION, IMAGE_PATH)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern(DATABASE_ADMIN_PATH + Route.MODIFY + Route.UNIT + "*"));

        assertThat(unitManagement.getSingleUnit(UNIT_NAME).getMinDamage()).hasToString(MIN_DAMAGE);
    }

    @Test
    @DisplayName("przekazuje poprawną nazwę, usuwa jednostkę")
    void deleteUnit_correctUnitName_resultUnitDeleted() throws Exception {
        performAddRequest();
        performDeleteRequest(UNIT_NAME)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(DATABASE_USER_PATH ));

        assertThrows(EntityNotFoundException.class, () -> unitManagement.getSingleUnit(UNIT_NAME));
    }

    @Test
    @DisplayName("przekazuje niepoprawną nazwę, dostaje wyjątek, nie usuwa jednostki")
    void deleteUnit_incorrectUnitName_resultUnitNotDeleted() throws Exception {
        performAddRequest();
        assertThrows(ServletException.class, () -> {
            performDeleteRequest("incorrectUnitName")
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl(DATABASE_USER_PATH));
        });

        assertThat(unitManagement.getSingleUnit(UNIT_NAME)).isNotNull();
    }

    private MockMultipartFile getTestFile(){
        return new MockMultipartFile(
                "image",
                "test.png",
                "image/png",
                "testContent".getBytes());
    }

    private void clearRepo(){
        List<String> unitNames = unitManagement.getAllUnits().stream()
                .map(Unit::getName)
                .toList();
        unitNames.forEach(unitManagement::removeUnit);
    }

    private ResultActions performAddRequest () throws Exception {
        return mockMvc.perform(multipart(Route.MAIN + Route.ADMIN + Route.DATABASE + Route.ADD)
                .file(IMAGE)
                .param("name", UNIT_NAME)
                .param("attack", ATTACK)
                .param("defense", DEFENSE)
                .param("minDamage", MIN_DAMAGE)
                .param("maxDamage", MAX_DAMAGE)
                .param("hp", HP)
                .param("speed", SPEED)
                .param("description", DESCRIPTION));

    }

    private ResultActions performAddRequest (String attack,
                                     String defense,
                                     String minDamage,
                                     String maxDamage,
                                     String hp,
                                     String speed,
                                     String description) throws Exception {
        return mockMvc.perform(multipart(Route.MAIN + Route.ADMIN + Route.DATABASE + Route.ADD)
                .file(IMAGE)
                .param("name", UNIT_NAME)
                .param("attack", attack)
                .param("defense", defense)
                .param("minDamage", minDamage)
                .param("maxDamage", maxDamage)
                .param("hp", hp)
                .param("speed", speed)
                .param("description", description));
    }

    private ResultActions performModifyRequest(String name,
                                               String attack,
                                               String defense,
                                               String minDamage,
                                               String maxDamage,
                                               String hp,
                                               String speed,
                                               String description,
                                               String imagePath) throws Exception {
        return mockMvc.perform(post(Route.MAIN + Route.ADMIN + Route.DATABASE + Route.MODIFY + Route.UNIT)
                        .param("name",name)
                        .param("attack", attack)
                        .param("defense", defense)
                        .param("minDamage", minDamage)
                        .param("maxDamage", maxDamage)
                        .param("hp", hp)
                        .param("speed", speed)
                        .param("description", description)
                        .param("imagePath", imagePath));
    }

    private ResultActions performDeleteRequest(String name) throws Exception {
        return mockMvc.perform(post(Route.MAIN + Route.ADMIN + Route.DATABASE + Route.DELETE)
                .param("name", name));
    }
}
