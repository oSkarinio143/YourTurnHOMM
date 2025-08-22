package pl.oskarinio.yourturnhomm.infrastructure.adapter.in.controller.battle;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.oskarinio.yourturnhomm.app.battle.port.in.DuelUseCase;
import pl.oskarinio.yourturnhomm.app.database.port.in.DatabaseUseCase;
import pl.oskarinio.yourturnhomm.domain.model.Route;
import pl.oskarinio.yourturnhomm.domain.model.battle.Side;
import pl.oskarinio.yourturnhomm.infrastructure.adapter.in.model.form.DuelForm;
import pl.oskarinio.yourturnhomm.domain.service.rest.ExceptionMessageCreator;

@Controller
@RequestMapping(Route.MAIN + Route.DUEL)
@CrossOrigin(origins = "*")
public class DuelController {

    private final ExceptionMessageCreator ExceptionMessageCreator;
    private final DuelUseCase duelUseCase;
    private final DatabaseUseCase databaseUseCase;

    public DuelController(ExceptionMessageCreator ExceptionMessageCreator, DuelUseCase duelUseCase, DatabaseUseCase databaseUseCase) {
        this.ExceptionMessageCreator = ExceptionMessageCreator;
        this.duelUseCase = duelUseCase;
        this.databaseUseCase = databaseUseCase;
    }

    @GetMapping
    public String prepareDuel(Model model,
                              @ModelAttribute DuelForm duelForm){

        if(duelForm.getUserUUID() != null)
            duelUseCase.closeEmitter(duelForm.getUserUUID());
        model.addAttribute("duelForm", duelForm);
        return Route.PACKAGE_DUEL + Route.DUEL;
    }

    @GetMapping(Route.SELECT)
    public String selectUnit(Model model,
                             @ModelAttribute DuelForm duelForm,
                             @RequestParam Side side){

        model.addAttribute("units", databaseUseCase.getAllUnits());
        model.addAttribute("duelForm", duelForm);
        model.addAttribute("side", side.toString());
        return Route.PACKAGE_DUEL + Route.SELECT;
    }

    @PostMapping()
    public String loadUnit(Model model,
                           @ModelAttribute DuelForm duelForm,
                           @RequestParam(required = false) Side side,
                           @RequestParam(required = false) String tempUnitName){

        duelUseCase.loadUnit(duelForm, side, tempUnitName);
        model.addAttribute("duelForm", duelForm);
        return Route.PACKAGE_DUEL + Route.DUEL;
    }

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
        duelForm.setUserUUID(duelUseCase.getUserUUID());
        model.addAttribute("duelForm", duelForm);
        duelUseCase.loadBattle(duelForm);
        return Route.PACKAGE_DUEL + Route.BATTLE;
    }
}
