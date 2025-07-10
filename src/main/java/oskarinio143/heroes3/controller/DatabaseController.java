package oskarinio143.heroes3.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import oskarinio143.heroes3.model.constant.Route;
import oskarinio143.heroes3.model.entity.Unit;
import oskarinio143.heroes3.service.DatabaseService;
import oskarinio143.heroes3.service.ExceptionHandlerService;

import java.io.IOException;

@Controller
@RequestMapping(Route.MAIN + Route.DATABASE)
public class DatabaseController {

    private final DatabaseService databaseService;
    private final ExceptionHandlerService exceptionHandlerService;

    public DatabaseController(DatabaseService databaseService, ExceptionHandlerService exceptionHandlerService) {
        this.databaseService = databaseService;
        this.exceptionHandlerService = exceptionHandlerService;
    }

    @GetMapping
    public String choseDatabaseOption(){
        return "choseDatabaseOption";
    }

    @GetMapping(Route.ADD)
    public String addUnit(){
        return "addUnit";
    }

    @PostMapping(Route.ADD)
    public String handleAddUnit(@Valid @ModelAttribute Unit unit,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes,
                                @RequestParam MultipartFile image) throws IOException{

        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("incorrectMessage", exceptionHandlerService.createMessageValidError(bindingResult));
            return Route.REDIRECT + Route.ADD;
        }

        databaseService.addUnit(unit, image);
        return Route.REDIRECT + Route.DATABASE;
    }

    @GetMapping(Route.VIEW)
    public String viewUnits(Model model){
        databaseService.viewUnits(model);
        return "viewUnits";
    }

    @GetMapping(Route.DELETE)
    public String deleteUnit(Model model){
        databaseService.viewUnits(model);
        return "deleteUnit";
    }

    @PostMapping(Route.DELETE)
    public String handleDeleteUnit(@RequestParam String name) {
        databaseService.removeUnit(name);
        return Route.REDIRECT + Route.DATABASE;
    }

    @GetMapping(Route.MODIFY)
    public String handleModify(Model model){
        databaseService.viewUnits(model);
        return "modify";
    }

    @GetMapping(Route.MODIFY + Route.UNIT)
    public String handleModifyUnit(@RequestParam String name, Model model) {
        databaseService.viewSingleUnit(model, name);
        return "modifyUnit";
    }

    @PostMapping(Route.MODIFY + Route.UNIT)
    public String saveModifiedUnit(@Valid @ModelAttribute Unit unit,
                                   BindingResult bindingResult,
                                   RedirectAttributes redirectAttributes) throws IOException {

        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("incorrectMessage", exceptionHandlerService.createMessageValidError(bindingResult));
            redirectAttributes.addAttribute("name", unit.getName());
            return Route.REDIRECT + Route.DATABASE + Route.MODIFY + Route.UNIT;
        }
        databaseService.modifyUnit(unit);
        return Route.REDIRECT + Route.DATABASE + Route.MODIFY;

    }
}
