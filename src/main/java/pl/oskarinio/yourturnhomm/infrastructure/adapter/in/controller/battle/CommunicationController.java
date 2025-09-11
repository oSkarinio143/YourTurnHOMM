package pl.oskarinio.yourturnhomm.infrastructure.adapter.in.controller.battle;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import pl.oskarinio.yourturnhomm.domain.model.Route;
import pl.oskarinio.yourturnhomm.infrastructure.temp.CommunicationUseCase;

@RestController
@RequestMapping(Route.MAIN + Route.DUEL)
@CrossOrigin(origins = "*")
@Slf4j
class CommunicationController {
    private final CommunicationUseCase communicationUseCase;

    public CommunicationController(CommunicationUseCase communicationUseCase) {
        this.communicationUseCase = communicationUseCase;
    }

    @GetMapping(Route.GENERATEUUID)
    public String generateUUID(){
        log.debug("Tworze userUUID");
        return communicationUseCase.createUserUUID();
    }

    @GetMapping(path = Route.BATTLE + Route.STREAM, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream(@RequestParam String userUUID) {
        log.debug("Tworze emitter, userUUID = {}", userUUID);
        return communicationUseCase.createEmitter(userUUID);
    }
}