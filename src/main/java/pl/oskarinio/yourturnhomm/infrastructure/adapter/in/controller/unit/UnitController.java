package pl.oskarinio.yourturnhomm.infrastructure.adapter.in.controller.unit;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.oskarinio.yourturnhomm.app.technology.communication.ExceptionMessageCreatorService;
import pl.oskarinio.yourturnhomm.domain.model.Route;
import pl.oskarinio.yourturnhomm.domain.port.unit.UnitManagement;
import pl.oskarinio.yourturnhomm.infrastructure.config.ImagePathConverter;
import pl.oskarinio.yourturnhomm.infrastructure.db.entity.UnitEntity;
import pl.oskarinio.yourturnhomm.infrastructure.db.mapper.UnitMapper;

import java.io.IOException;

@Slf4j
@Controller
@RequestMapping(Route.MAIN)
class UnitController {

    private final UnitManagement unitManagementUseCase;
    private final ExceptionMessageCreatorService exceptionHandlerService;
    private final ImagePathConverter imagePathConverter;

    public UnitController(UnitManagement unitManagementUseCase, ExceptionMessageCreatorService exceptionHandlerService, ImagePathConverter imagePathConverter) {
        this.unitManagementUseCase = unitManagementUseCase;
        this.exceptionHandlerService = exceptionHandlerService;
        this.imagePathConverter = imagePathConverter;
    }

    @GetMapping(Route.USER + Route.DATABASE)
    public String choseDatabaseOption(){
        log.info("Uzytkownik w menu zarządzania jednostkami");
        return Route.PACKAGE_DATABASE + Route.DATABASE;
    }

    @GetMapping(Route.ADMIN + Route.DATABASE + Route.ADD)
    public String addUnit(){
        log.info("Uzytkownik w menu dodania jednostki ");
        return Route.PACKAGE_DATABASE + Route.VIEW_ADD;
    }

    @PostMapping(Route.ADMIN + Route.DATABASE + Route.ADD)
    public String handleAddUnit(@Valid @ModelAttribute UnitEntity unitEntity,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes,
                                @RequestParam MultipartFile image) throws IOException{

        log.info("Uzytkownik wprowadzil dane");
        if(bindingResult.hasErrors()){
            log.warn("Dodawanie jednostki nie udalo sie, wprowadzono niepoprawne dane");
            redirectAttributes.addFlashAttribute("incorrectMessage", exceptionHandlerService.createErrorMessage(bindingResult));
            return Route.REDIRECT + Route.ADMIN + Route.DATABASE + Route.ADD;
        }
        unitEntity.setImagePath(imagePathConverter.convertImageToPath(unitEntity.getName(), image));
        unitManagementUseCase.addUnit(UnitMapper.toDomain(unitEntity));
        log.info("Jednostka zostala dodana do bazy danych");
        return Route.REDIRECT + Route.USER + Route.DATABASE;
    }

    @GetMapping(Route.USER + Route.DATABASE + Route.SHOW)
    public String viewUnits(Model model){
        log.info("Uzytkownik wyswietla liste jednostek");
        model.addAttribute("units", unitManagementUseCase.getAllUnits());
        return Route.PACKAGE_DATABASE + Route.VIEW_SHOW_UNITS;
    }

    @GetMapping(Route.ADMIN + Route.DATABASE + Route.DELETE)
    public String deleteUnit(Model model){
        log.info("Uzytkownik w menu usuniecia jednostki, uruchamiam liste jednostek");
        model.addAttribute("units", unitManagementUseCase.getAllUnits());
        return Route.PACKAGE_DATABASE + Route.VIEW_DELETE_UNIT;
    }

    @PostMapping(Route.ADMIN + Route.DATABASE + Route.DELETE)
    public String handleDeleteUnit(@RequestParam String name) {
        log.info("Uzytkownik wybral jednostke do usuniecia");
        unitManagementUseCase.removeUnit(name);
        log.info("Jednostka zostala usunieta");
        return Route.REDIRECT + Route.USER + Route.DATABASE;
    }

    @GetMapping(Route.ADMIN + Route.DATABASE + Route.MODIFY)
    public String handleModify(Model model){
        log.info("Uzytkownik w menu modyfikacji jednostki, uruchamiam liste jednostek");
        model.addAttribute("units", unitManagementUseCase.getAllUnits());
        return Route.PACKAGE_DATABASE + Route.VIEW_MODIFY;
    }

    @GetMapping(Route.ADMIN + Route.DATABASE + Route.MODIFY + Route.UNIT)
    public String handleModifyUnit(@RequestParam String name,
                                   Model model) {

        log.info("Uzytkownik wybral jednostke do modyfikacji");
        model.addAttribute("unit", unitManagementUseCase.getSingleUnit(name));
        return Route.PACKAGE_DATABASE + Route.VIEW_MODIFY_UNIT;
    }

    @PostMapping(Route.ADMIN + Route.DATABASE + Route.MODIFY + Route.UNIT)
    public String saveModifiedUnit(@Valid @ModelAttribute UnitEntity unitEntity,
                                   BindingResult bindingResult,
                                   RedirectAttributes redirectAttributes){

        log.info("Uzytkownik wprowadza dane");
        if(bindingResult.hasErrors()){
            log.warn("Modyfikacja jednostki nie udala sie, wprowadzono niepoprane dane");
            redirectAttributes.addFlashAttribute("incorrectMessage", exceptionHandlerService.createErrorMessage(bindingResult));
            redirectAttributes.addAttribute("name", unitEntity.getName());
            return Route.REDIRECT + Route.ADMIN + Route.DATABASE + Route.MODIFY + Route.UNIT;
        }
        unitManagementUseCase.modifyUnit(UnitMapper.toDomain(unitEntity));
        log.info("Jednostka zostala zmodyfikowana");
        return Route.REDIRECT + Route.ADMIN + Route.DATABASE + Route.MODIFY;
    }
}
