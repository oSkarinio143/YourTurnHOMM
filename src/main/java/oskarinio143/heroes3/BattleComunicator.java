package oskarinio143.heroes3;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import oskarinio143.heroes3.controller.BattleService;

@RestController
@RequestMapping
public class BattleComunicator {

    @GetMapping(value = "/battle/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamBattle() {
        SseEmitter emitter = new SseEmitter();

        // Uruchamiamy nowy wątek, żeby nie blokować głównego
        new Thread(() -> {
            try {
                BattleService simulator = new BattleService(); // twoja klasa symulująca bitwę

//                for (BattleEvent event : simulator) {
//                    emitter.send(SseEmitter.event()
//                            .name("battle-event")
//                            .data(event));
//                }

                emitter.complete();
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        }).start();

        return emitter;
    }
}
