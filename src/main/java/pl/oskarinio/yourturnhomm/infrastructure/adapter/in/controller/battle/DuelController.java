package pl.oskarinio.yourturnhomm.infrastructure.adapter.in.controller.battle;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.oskarinio.yourturnhomm.domain.model.Route;
import pl.oskarinio.yourturnhomm.domain.model.battle.Side;
import pl.oskarinio.yourturnhomm.domain.port.in.battle.DuelUseCase;
import pl.oskarinio.yourturnhomm.domain.port.in.unit.UnitManagerPort;
import pl.oskarinio.yourturnhomm.domain.rest.ExceptionMessageCreator;
import pl.oskarinio.yourturnhomm.infrastructure.adapter.in.model.DuelForm;
import pl.oskarinio.yourturnhomm.infrastructure.temp.CommunicationUseCase;

@Slf4j
@Controller
@RequestMapping(Route.MAIN + Route.USER + Route.DUEL)
@CrossOrigin(origins = "*")
class DuelController {

    private final ExceptionMessageCreator exceptionMessageCreator;
    private final CommunicationUseCase communicationUseCase;
    private final DuelUseCase duelUseCase;
    private final UnitManagerPort databaseUseCase;

    public DuelController(ExceptionMessageCreator exceptionMessageCreator, CommunicationUseCase communicationUseCase, DuelUseCase duelUseCase, UnitManagerPort databaseUseCase) {
        this.exceptionMessageCreator = exceptionMessageCreator;
        this.communicationUseCase = communicationUseCase;
        this.duelUseCase = duelUseCase;
        this.databaseUseCase = databaseUseCase;
    }

    @GetMapping
    public String prepareDuel(Model model,
                              @ModelAttribute DuelForm duelForm){

        log.info("Uzytkownik przygotowuje pojedynek");
        if(duelForm.getUserUUID() != null)
            communicationUseCase.closeConnection(duelForm.getUserUUID());
        model.addAttribute("duelForm", duelForm);
        return Route.PACKAGE_DUEL + Route.DUEL;
    }

    @GetMapping(Route.SELECT)
    public String selectUnit(Model model,
                             @ModelAttribute DuelForm duelForm,
                             @RequestParam Side side){

        log.info("Uzytkownik wybiera jednostke");
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
        log.debug("Jednostka zaladowana");
        return Route.PACKAGE_DUEL + Route.DUEL;
    }

    @PostMapping(Route.BATTLE)
    public String startBattle(@Valid @ModelAttribute DuelForm duelForm,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes,
                              Model model){

        if(bindingResult.hasErrors()){
            log.warn("Nie udalo sie rozpoczac pojedynku - wprowadzono zle dane");
            redirectAttributes.addFlashAttribute("incorrectMessage", exceptionMessageCreator.createMessageValidError(bindingResult));
            redirectAttributes.addFlashAttribute("duelForm", duelForm);
            return Route.REDIRECT + Route.USER + Route.DUEL;
        }
        duelForm.setUserUUID(communicationUseCase.createUserUUID());
        model.addAttribute("duelForm", duelForm);
        duelUseCase.loadBattle(duelForm);
        log.info("Uzytkownik rozpoczyna pojedynek");
        return Route.PACKAGE_DUEL + Route.BATTLE;
    }
}
