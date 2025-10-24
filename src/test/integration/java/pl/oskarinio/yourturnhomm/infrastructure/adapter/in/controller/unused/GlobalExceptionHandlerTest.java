//package pl.oskarinio.yourturnhomm.infrastructure.adapter.in.controller.rest;
//
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.servlet.MockMvc;
//import pl.oskarinio.yourturnhomm.app.technology.communication.CookieHelper;
//import pl.oskarinio.yourturnhomm.app.technology.communication.ExceptionMessageCreatorService;
//import pl.oskarinio.yourturnhomm.domain.model.Route;
//import pl.oskarinio.yourturnhomm.domain.model.battle.Unit;
//import pl.oskarinio.yourturnhomm.domain.model.exception.DuplicateUnitException;
//import pl.oskarinio.yourturnhomm.domain.port.out.UnitRepository;
//import pl.oskarinio.yourturnhomm.domain.port.unit.UnitManagement;
//import pl.oskarinio.yourturnhomm.domain.port.user.Admin;
//import pl.oskarinio.yourturnhomm.domain.port.user.Login;
//import pl.oskarinio.yourturnhomm.domain.port.user.Register;
//import pl.oskarinio.yourturnhomm.domain.port.user.UserManagement;
//import pl.oskarinio.yourturnhomm.infrastructure.adapter.in.controller.MvcControllerTest;
//import pl.oskarinio.yourturnhomm.infrastructure.adapter.in.controller.unit.UnitController;
//import pl.oskarinio.yourturnhomm.infrastructure.adapter.in.controller.user.AdminController;
//import pl.oskarinio.yourturnhomm.infrastructure.adapter.in.controller.user.UserController;
//import pl.oskarinio.yourturnhomm.infrastructure.config.ImagePathConverter;
//import pl.oskarinio.yourturnhomm.infrastructure.db.entity.UnitEntity;
//import pl.oskarinio.yourturnhomm.infrastructure.db.mapper.MapStruct;
//import pl.oskarinio.yourturnhomm.infrastructure.db.mapper.UnitMapper;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.doThrow;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
//import static org.springframework.test.web.servlet.resTult.MockMvcResultMatchers.status;
//
//@MvcControllerTest(controllers = {GlobalExceptionHandler.class,
//        UnitController.class,
//        AdminController.class,
//        UserController.class})
//
//class GlobalExceptionHandlerTest {
//    @Autowired
//    private MockMvc mockMvc;
//    @MockitoBean
//    private UnitRepository unitRepository;
//    @MockitoBean
//    private ExceptionMessageCreatorService exceptionMessageCreatorService;
//    @MockitoBean
//    private UnitManagement unitManagement;
//    @MockitoBean
//    private ImagePathConverter imagePathConverter;
//    @MockitoBean
//    private UserManagement userManagement;
//    @MockitoBean
//    private Register register;
//    @MockitoBean
//    private Login login;
//    @MockitoBean
//    private CookieHelper cookieHelper;
//    @MockitoBean
//    private MapStruct mapper;
//    @MockitoBean
//    private Admin admin;
//
//    private static final String ERROR_MESSAGE_VALUE = "errorMessageValue";
//    private static final String ERROR_MESSAGE = "errorMessage";
//
//    @Test
//    @DisplayName("")
//    void handleDuplicateUnitException() throws Exception {
//        Unit unit = new Unit("testUnit",1,1,1,1,1,1,"testing");
//        UnitEntity unitEntity = UnitMapper.toEntity(unit);
//        doThrow(DuplicateUnitException.class).when(unitRepository).existsById(any());
//
//        mockMvc.perform(post(Route.MAIN + Route.ADMIN + Route.DATABASE + Route.ADD).param("", unitEntity))
//                .andExpect(status().isFound())
//                .andExpect(flash().attribute("duplicateMessage", ERROR_MESSAGE_VALUE));
//
//    }
//}
