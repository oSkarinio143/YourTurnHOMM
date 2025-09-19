package pl.oskarinio.yourturnhomm.infrastructure.adapter.in.controller.battle;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.oskarinio.yourturnhomm.app.technology.communication.ExceptionMessageCreatorService;
import pl.oskarinio.yourturnhomm.domain.model.Route;
import pl.oskarinio.yourturnhomm.domain.model.battle.Side;
import pl.oskarinio.yourturnhomm.domain.model.form.DuelForm;
import pl.oskarinio.yourturnhomm.domain.port.battle.Duel;
import pl.oskarinio.yourturnhomm.domain.port.out.MessageSender;
import pl.oskarinio.yourturnhomm.domain.port.unit.UnitManagement;
import pl.oskarinio.yourturnhomm.infrastructure.adapter.in.model.DuelFormRequest;
import pl.oskarinio.yourturnhomm.infrastructure.db.mapper.MapStruct;
import pl.oskarinio.yourturnhomm.infrastructure.port.communication.Communication;

@Slf4j
@Controller
@RequestMapping(Route.MAIN + Route.USER + Route.DUEL)
@CrossOrigin(origins = "*")
class DuelController {

    private final ExceptionMessageCreatorService exceptionMessageCreatorService;
    private final Communication communication;
    private final MessageSender messageSender;
    private final Duel duel;
    private final UnitManagement databaseUseCase;
    private final MapStruct mapper;

    public DuelController(ExceptionMessageCreatorService exceptionMessageCreatorService, Communication communication, MessageSender messageSender, Duel duel, UnitManagement databaseUseCase, MapStruct mapper) {
        this.exceptionMessageCreatorService = exceptionMessageCreatorService;
        this.communication = communication;
        this.messageSender = messageSender;
        this.duel = duel;
        this.databaseUseCase = databaseUseCase;
        this.mapper = mapper;
    }

    @GetMapping
    public String prepareDuel(Model model,
                              @ModelAttribute DuelFormRequest duelFormRequest){

        log.info("Uzytkownik w menu pojedynku");
        if(duelFormRequest.getUserUUID() != null)
            messageSender.closeConnection(duelFormRequest.getUserUUID());
        model.addAttribute("duelForm", duelFormRequest);
        return Route.PACKAGE_DUEL + Route.DUEL;
    }

    @GetMapping(Route.SELECT)
    public String selectUnit(Model model,
                             @ModelAttribute DuelFormRequest duelFormRequest,
                             @RequestParam Side side){

        log.info("Uzytkownik wybiera jednostke");
        model.addAttribute("units", databaseUseCase.getAllUnits());
        model.addAttribute("duelForm", duelFormRequest);
        model.addAttribute("side", side.toString());
        return Route.PACKAGE_DUEL + Route.SELECT;
    }

    @PostMapping()
    public String loadUnit(Model model,
                           @ModelAttribute DuelFormRequest duelFormRequest,
                           @RequestParam(required = false) Side side,
                           @RequestParam(required = false) String tempUnitName){

        System.out.println("Poprzednia jednostka - " + duelFormRequest.getLeftUnit());
        DuelForm duelForm = mapper.toDuelForm(duelFormRequest);
        duel.loadUnit(duelForm, side, tempUnitName);
        model.addAttribute("duelForm", duelForm);
        log.info("Jednostka zostala zaladowana,  {}",tempUnitName);
        return Route.PACKAGE_DUEL + Route.DUEL;
    }

    @PostMapping(Route.BATTLE)
    public String startBattle(@Valid @ModelAttribute DuelFormRequest duelFormRequest,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes,
                              Model model){

        if(bindingResult.hasErrors()){
            log.warn("Nie udalo sie rozpoczac pojedynku - Uzytkownik wprowadzil niepoprawne dane");
            redirectAttributes.addFlashAttribute("incorrectMessage", exceptionMessageCreatorService.createMessageValidError(bindingResult));
            redirectAttributes.addFlashAttribute("duelForm", duelFormRequest);
            return Route.REDIRECT + Route.USER + Route.DUEL;
        }
        log.info("Uzytkownik w intefejsie bitwy");
        duelFormRequest.setUserUUID(communication.createUserUUID());
        model.addAttribute("duelForm", duelFormRequest);
        duel.loadBattle(mapper.toDuelForm(duelFormRequest));
        return Route.PACKAGE_DUEL + Route.BATTLE;
    }
}
