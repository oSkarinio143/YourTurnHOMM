package oskarinio143.heroes3.controller;

import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import oskarinio143.heroes3.service.CommunicationService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


@RestController
@RequestMapping("oskarinio143/heroes/duel/")
@CrossOrigin(origins = "*")
public class BattleComunicator {
    private final CommunicationService communicationService;

    public BattleComunicator(CommunicationService communicationService) {
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