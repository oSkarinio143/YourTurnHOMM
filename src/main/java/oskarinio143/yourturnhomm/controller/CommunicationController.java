package oskarinio143.yourturnhomm.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import oskarinio143.yourturnhomm.model.constant.Route;
import oskarinio143.yourturnhomm.service.CommunicationService;

@RestController
@RequestMapping(Route.MAIN + Route.DUEL)
@CrossOrigin(origins = "*")
public class CommunicationController {
    private final CommunicationService communicationService;

    public CommunicationController(CommunicationService communicationService) {
        this.communicationService = communicationService;
    }

    @GetMapping(Route.GENERATEUUID)
    public String generateUUID(){
        return communicationService.createUserUUID();
    }

    @GetMapping(path = Route.BATTLE + Route.STREAM, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream(@RequestParam String userUUID) {
        return communicationService.createEmitter(userUUID);
    }
}