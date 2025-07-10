package oskarinio143.heroes3.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import oskarinio143.heroes3.model.constant.Route;
import oskarinio143.heroes3.model.servicedto.DuelInfo;
import oskarinio143.heroes3.service.DuelService;
import oskarinio143.heroes3.service.ExceptionHandlerService;

@Controller
@RequestMapping(Route.MAIN + Route.DUEL)
@CrossOrigin(origins = "*")
public class DuelController {

    private final ExceptionHandlerService exceptionHandlerService;
    private final DuelService duelService;

    public DuelController(ExceptionHandlerService exceptionHandlerService, DuelService duelService) {
        this.exceptionHandlerService = exceptionHandlerService;
        this.duelService = duelService;
    }

    @GetMapping
    public String prepareDuel(Model model, @ModelAttribute DuelInfo duelInfo){
        duelService.prepareUnits(model, duelInfo);
        return Route.DUEL;
    }

    @PostMapping(Route.SELECT)
    public String selectUnit(Model model, @ModelAttribute DuelInfo duelInfo, @RequestParam String side){
        duelService.selectUnit(model, duelInfo, side);
        return Route.SELECT;
    }

    @PostMapping()
    public String loadUnit(Model model, @ModelAttribute DuelInfo duelInfo, @RequestParam String side, @RequestParam String name){
        duelService.loadUnit(model, duelInfo, side, name);
        return Route.DUEL;
    }

    @PostMapping(Route.BATTLE)
    public String startBattle(@Valid @ModelAttribute DuelInfo duelInfo,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes,
                              HttpServletRequest request,
                              Model model){
        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("incorrectMessage", exceptionHandlerService.createMessageValidError(bindingResult));
            exceptionHandlerService.passDataDuel(redirectAttributes, request);
            return Route.REDIRECT + Route.DUEL;
        }
        duelService.loadBattle(model, duelInfo);
        return Route.BATTLE;
    }
}
