package pl.oskarinio.yourturnhomm.infrastructure.adapter.in.controller.unit;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.oskarinio.yourturnhomm.domain.model.Route;
import pl.oskarinio.yourturnhomm.domain.model.battle.Unit;
import pl.oskarinio.yourturnhomm.domain.port.in.unit.UnitManagerPort;
import pl.oskarinio.yourturnhomm.domain.rest.ExceptionMessageCreator;

import java.io.IOException;

@Slf4j
@Controller
@RequestMapping(Route.MAIN)
class UnitController {

    private final UnitManagerPort databaseUseCase;
    private final ExceptionMessageCreator exceptionHandlerService;

    public UnitController(UnitManagerPort databaseUseCase, ExceptionMessageCreator exceptionHandlerService) {
        this.databaseUseCase = databaseUseCase;
        this.exceptionHandlerService = exceptionHandlerService;
    }

    @GetMapping(Route.USER + Route.DATABASE)
    public String choseDatabaseOption(){
        log.info("Uzytkownik wybiera opcje bazy danych");
        return Route.PACKAGE_DATABASE + Route.DATABASE;
    }

    @GetMapping(Route.ADMIN + Route.DATABASE + Route.ADD)
    public String addUnit(){
        log.info("Uzytkownik dodaje jednostke do bazy danych");
        return Route.PACKAGE_DATABASE + Route.VIEW_ADD;
    }

    @PostMapping(Route.ADMIN + Route.DATABASE + Route.ADD)
    public String handleAddUnit(@Valid @ModelAttribute Unit unit,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes,
                                @RequestParam MultipartFile image) throws IOException{

        if(bindingResult.hasErrors()){
            log.warn("Dodawanie jednostki nie udalo sie z powodu niepoprawnych danych");
            redirectAttributes.addFlashAttribute("incorrectMessage", exceptionHandlerService.createMessageValidError(bindingResult));
            return Route.REDIRECT + Route.ADMIN + Route.DATABASE + Route.ADD;
        }
        log.debug("Jednostka zostala dodana do bazy danych");
        databaseUseCase.addUnit(unit, image);
        return Route.REDIRECT + Route.USER + Route.DATABASE;
    }

    @GetMapping(Route.USER + Route.DATABASE + Route.SHOW)
    public String viewUnits(Model model){
        log.info("Uzytkownik wyswietla liste jednostek");
        model.addAttribute("units", databaseUseCase.getAllUnits());
        return Route.PACKAGE_DATABASE + Route.VIEW_SHOW_UNITS;
    }

    @GetMapping(Route.ADMIN + Route.DATABASE + Route.DELETE)
    public String deleteUnit(Model model){
        log.info("Uzytkownik wybier jednostke do usuniecia");
        model.addAttribute("units", databaseUseCase.getAllUnits());
        return Route.PACKAGE_DATABASE + Route.VIEW_DELETE_UNIT;
    }

    @PostMapping(Route.ADMIN + Route.DATABASE + Route.DELETE)
    public String handleDeleteUnit(@RequestParam String name) {
        databaseUseCase.removeUnit(name);
        log.debug("Jednostka zostala usunieta z bazy danych");
        return Route.REDIRECT + Route.USER + Route.DATABASE;
    }

    @GetMapping(Route.ADMIN + Route.DATABASE + Route.MODIFY)
    public String handleModify(Model model){
        model.addAttribute("units", databaseUseCase.getAllUnits());
        return Route.PACKAGE_DATABASE + Route.VIEW_MODIFY;
    }

    @GetMapping(Route.ADMIN + Route.DATABASE + Route.MODIFY + Route.UNIT)
    public String handleModifyUnit(@RequestParam String name,
                                   Model model) {

        model.addAttribute("unit", databaseUseCase.getSingleUnit(name));
        return Route.PACKAGE_DATABASE + Route.VIEW_MODIFY_UNIT;
    }

    @PostMapping(Route.ADMIN + Route.DATABASE + Route.MODIFY + Route.UNIT)
    public String saveModifiedUnit(@Valid @ModelAttribute Unit unit,
                                   BindingResult bindingResult,
                                   RedirectAttributes redirectAttributes){

        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("incorrectMessage", exceptionHandlerService.createMessageValidError(bindingResult));
            redirectAttributes.addAttribute("name", unit.getName());
            return Route.REDIRECT + Route.ADMIN + Route.DATABASE + Route.MODIFY + Route.UNIT;
        }
        databaseUseCase.modifyUnit(unit);
        return Route.REDIRECT + Route.ADMIN + Route.DATABASE + Route.MODIFY;
    }
}
