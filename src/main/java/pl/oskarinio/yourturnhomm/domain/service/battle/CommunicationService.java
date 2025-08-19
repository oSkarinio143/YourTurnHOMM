package pl.oskarinio.yourturnhomm.domain.service.battle;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CommunicationService {
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public String createUserUUID(){
        String userUUID;
        do {
            userUUID = UUID.randomUUID().toString();
        } while (emitters.containsKey(userUUID));
        return userUUID;
    }

    public SseEmitter createEmitter(String userUUID){
        SseEmitter emitter = new SseEmitter(3600000L);
        emitter.onCompletion(() -> System.out.println("SSE zakończone"));
        emitter.onTimeout(() -> System.out.println("SSE timeout"));
        emitters.put(userUUID, emitter);
        return emitter;
    }

    public void sendMessage(String userUUID, String message) {
        SseEmitter userEmitter = emitters.get(userUUID);
        if(userEmitter != null) {
            try {
                userEmitter.send(message);
            } catch (IOException e) {
                emitters.remove(userUUID);
                userEmitter.complete();
            }
        }
    }

    public void closeConnection(String userUUID){
        SseEmitter userEmitter = emitters.get(userUUID);
        if(userEmitter != null){
            try {
                userEmitter.send("CLOSE");
            } catch (IOException e) {
                emitters.remove(userUUID);
                userEmitter.complete();
            }
            userEmitter.complete();
            emitters.remove(userUUID);
        }
    }
}
