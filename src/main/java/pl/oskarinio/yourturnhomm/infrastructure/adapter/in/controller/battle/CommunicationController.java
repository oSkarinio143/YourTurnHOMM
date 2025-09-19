package pl.oskarinio.yourturnhomm.infrastructure.adapter.in.controller.battle;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import pl.oskarinio.yourturnhomm.domain.model.Route;
import pl.oskarinio.yourturnhomm.infrastructure.port.communication.Communication;

@RestController
@RequestMapping(Route.MAIN + Route.DUEL)
@CrossOrigin(origins = "*")
@Slf4j
class CommunicationController {
    private final Communication communication;

    public CommunicationController(Communication communication) {
        this.communication = communication;
    }

    @GetMapping(Route.GENERATEUUID)
    public String generateUUID(){
        log.debug("Rozpoczynam tworzenie userUUID");
        return communication.createUserUUID();
    }

    @GetMapping(path = Route.BATTLE + Route.STREAM, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream(@RequestParam String userUUID) {
        log.trace("Rozpoczynam tworzenie emittera, userUUID = {}", userUUID);
        return communication.createEmitter(userUUID);
    }
}