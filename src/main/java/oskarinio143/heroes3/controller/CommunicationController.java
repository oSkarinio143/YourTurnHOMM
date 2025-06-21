package oskarinio143.heroes3.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import oskarinio143.heroes3.service.CommunicationService;


@RestController
@RequestMapping("oskarinio143/heroes/duel/")
@CrossOrigin(origins = "*")
public class CommunicationController {
    private final CommunicationService communicationService;

    public CommunicationController(CommunicationService communicationService) {
        this.communicationService = communicationService;
    }

    @GetMapping("generateUUID")
    public String generateUUID(){
        return communicationService.createUserUUID();
    }

    @GetMapping(path = "battle/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream(@RequestParam String userUUID) {
        return communicationService.createEmitter(userUUID);
    }
}