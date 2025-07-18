package oskarinio143.heroes3.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import oskarinio143.heroes3.model.constant.Route;
import oskarinio143.heroes3.model.constant.Side;
import oskarinio143.heroes3.model.form.DuelForm;
import oskarinio143.heroes3.service.DatabaseService;
import oskarinio143.heroes3.service.DuelService;
import oskarinio143.heroes3.service.ExceptionMessageCreator;

@Controller
@RequestMapping(Route.MAIN + Route.DUEL)
@CrossOrigin(origins = "*")
public class DuelController {

    private final ExceptionMessageCreator ExceptionMessageCreator;
    private final DuelService duelService;
    private final DatabaseService databaseService;

    public DuelController(ExceptionMessageCreator ExceptionMessageCreator, DuelService duelService, DatabaseService databaseService) {
        this.ExceptionMessageCreator = ExceptionMessageCreator;
        this.duelService = duelService;
        this.databaseService = databaseService;
    }

    @GetMapping
    public String prepareDuel(Model model,
                              @ModelAttribute DuelForm duelForm){

        if(duelForm.getUserUUID() != null)
            duelService.closeEmitter(duelForm.getUserUUID());
        model.addAttribute("duelForm", duelForm);
        return Route.PACKAGE_DUEL + Route.DUEL;
    }

    @GetMapping(Route.SELECT)
    public String selectUnit(Model model,
                             @ModelAttribute DuelForm duelForm,
                             @RequestParam Side side){

        model.addAttribute("units", databaseService.getAllUnits());
        model.addAttribute("duelForm", duelForm);
        model.addAttribute("side", side.toString());
        return Route.PACKAGE_DUEL + Route.SELECT;
    }

    @PostMapping()
    public String loadUnit(Model model,
                           @ModelAttribute DuelForm duelForm,
                           @RequestParam(required = false) Side side,
                           @RequestParam(required = false) String tempUnitName){

        duelService.loadUnit(duelForm, side, tempUnitName);
        model.addAttribute("duelForm", duelForm);
        return Route.PACKAGE_DUEL + Route.DUEL;
    }

    //Generowanie userUUID na oddzielnym endpointcie powoduje problem z wiadomościami z poprzednich bitew, więc zdecydowałem się na takie rozwiązanie
    @PostMapping(Route.BATTLE)
    public String startBattle(@Valid @ModelAttribute DuelForm duelForm,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes,
                              Model model){

        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("incorrectMessage", ExceptionMessageCreator.createMessageValidError(bindingResult));
            redirectAttributes.addFlashAttribute("duelForm", duelForm);
            return Route.REDIRECT + Route.DUEL;
        }
        duelForm.setUserUUID(duelService.getUserUUID());
        model.addAttribute("duelForm", duelForm);
        duelService.loadBattle(duelForm);
        return Route.PACKAGE_DUEL + Route.BATTLE;
    }
}
