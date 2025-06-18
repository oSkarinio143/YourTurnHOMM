package oskarinio143.heroes3;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import oskarinio143.heroes3.controller.BattleService;

import java.io.IOException;


@RestController
@CrossOrigin(origins = "*")
public class BattleComunicator {
    private volatile SseEmitter emitter;

    @GetMapping(path = "/oskarinio143/heroes/duel/battle/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream() {
        if (emitter != null) {
            emitter.complete();    // zamkniemy stare połączenie, jeśli gdzieś „wisiało”
        }
        emitter = new SseEmitter(0L);  // bez limitu czasu
        emitter.onCompletion(() -> System.out.println("SSE zakończone"));
        emitter.onTimeout(() -> System.out.println("SSE timeout"));

        return emitter;
    }

    public void sendMessage(String message) {
        if (emitter != null) {
            try {
                System.out.println("Wysłanie");
                emitter.send(message);
            } catch (IOException e) {
                System.out.println("Zlapane");
                emitter.complete();
            }
        }
    }

    public void closeConnection(){
        if(emitter != null){
            System.out.println("Zamkniecie");
            try {
                emitter.send("CLOSE");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            emitter.complete();
        }
    }
}
