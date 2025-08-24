package pl.oskarinio.yourturnhomm.infrastructure.adapter.in.controller.database;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.oskarinio.yourturnhomm.app.port.in.database.DatabaseUseCase;
import pl.oskarinio.yourturnhomm.domain.model.Route;
import pl.oskarinio.yourturnhomm.domain.model.battle.Unit;
import pl.oskarinio.yourturnhomm.domain.rest.ExceptionMessageCreator;

import java.io.IOException;

@Controller
@RequestMapping(Route.MAIN + Route.DATABASE)
public class DatabaseController {

    private final DatabaseUseCase databaseUseCase;
    private final ExceptionMessageCreator exceptionHandlerService;

    public DatabaseController(DatabaseUseCase databaseUseCase, ExceptionMessageCreator exceptionHandlerService) {
        this.databaseUseCase = databaseUseCase;
        this.exceptionHandlerService = exceptionHandlerService;
    }

    @GetMapping
    public String choseDatabaseOption(){
        return Route.PACKAGE_DATABASE + Route.DATABASE;
    }

    @GetMapping(Route.ADD)
    public String addUnit(){
        return Route.PACKAGE_DATABASE + Route.VIEW_ADD;
    }

    @PostMapping(Route.ADD)
    public String handleAddUnit(@Valid @ModelAttribute Unit unit,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes,
                                @RequestParam MultipartFile image) throws IOException{

        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("incorrectMessage", exceptionHandlerService.createMessageValidError(bindingResult));
            return Route.REDIRECT + Route.DATABASE + Route.ADD;
        }

        databaseUseCase.addUnit(unit, image);
        return Route.REDIRECT + Route.DATABASE;
    }

    @GetMapping(Route.SHOW)
    public String viewUnits(Model model){
        model.addAttribute("units", databaseUseCase.getAllUnits());
        return Route.PACKAGE_DATABASE + Route.VIEW_SHOW_UNITS;
    }

    @GetMapping(Route.DELETE)
    public String deleteUnit(Model model){
        model.addAttribute("units", databaseUseCase.getAllUnits());
        return Route.PACKAGE_DATABASE + Route.VIEW_DELETE_UNIT;
    }

    @PostMapping(Route.DELETE)
    public String handleDeleteUnit(@RequestParam String name) {
        databaseUseCase.removeUnit(name);
        return Route.REDIRECT + Route.DATABASE;
    }

    @GetMapping(Route.MODIFY)
    public String handleModify(Model model){
        model.addAttribute("units", databaseUseCase.getAllUnits());
        return Route.PACKAGE_DATABASE + Route.VIEW_MODIFY;
    }

    @GetMapping(Route.MODIFY + Route.UNIT)
    public String handleModifyUnit(@RequestParam String name,
                                   Model model) {

        model.addAttribute("unit", databaseUseCase.getSingleUnit(name));
        return Route.PACKAGE_DATABASE + Route.VIEW_MODIFY_UNIT;
    }

    @PostMapping(Route.MODIFY + Route.UNIT)
    public String saveModifiedUnit(@Valid @ModelAttribute Unit unit,
                                   BindingResult bindingResult,
                                   RedirectAttributes redirectAttributes){

        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("incorrectMessage", exceptionHandlerService.createMessageValidError(bindingResult));
            redirectAttributes.addAttribute("name", unit.getName());
            return Route.REDIRECT + Route.DATABASE + Route.MODIFY + Route.UNIT;
        }
        databaseUseCase.modifyUnit(unit);
        return Route.REDIRECT + Route.DATABASE + Route.MODIFY;
    }
}
