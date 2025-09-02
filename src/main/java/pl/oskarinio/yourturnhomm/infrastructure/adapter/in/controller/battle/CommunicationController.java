package pl.oskarinio.yourturnhomm.infrastructure.adapter.in.controller.battle;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import pl.oskarinio.yourturnhomm.app.port.in.battle.CommunicationUseCase;
import pl.oskarinio.yourturnhomm.domain.model.Route;

@RestController
@RequestMapping(Route.MAIN + Route.DUEL)
@CrossOrigin(origins = "*")
class CommunicationController {
    private final CommunicationUseCase communicationUseCase;

    public CommunicationController(CommunicationUseCase communicationUseCase) {
        this.communicationUseCase = communicationUseCase;
    }

    @GetMapping(Route.GENERATEUUID)
    public String generateUUID(){
        return communicationUseCase.createUserUUID();
    }

    @GetMapping(path = Route.BATTLE + Route.STREAM, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream(@RequestParam String userUUID) {
        return communicationUseCase.createEmitter(userUUID);
    }
}