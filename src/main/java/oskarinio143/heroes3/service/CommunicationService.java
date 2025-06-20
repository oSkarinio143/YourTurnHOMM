package oskarinio143.heroes3.service;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import oskarinio143.heroes3.exception.EmitterUnavailable;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
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
        SseEmitter emitter = new SseEmitter(0L);
        emitter.onCompletion(() -> System.out.println("SSE zakończone"));
        emitter.onTimeout(() -> System.out.println("SSE timeout"));
        emitters.put(userUUID, emitter);
        System.out.println("Poczatek emittera");
        return emitter;
    }

    public void sendMessage(String userUUID, String message) {
        SseEmitter userEmitter = emitters.get(userUUID);
        if(userEmitter != null) {
            try {
                userEmitter.send(message);
            } catch (IOException e) {
                userEmitter.completeWithError(new EmitterUnavailable("EmmitterUnavailable"));
                emitters.remove(userUUID);
            }
        }
    }

    public void closeConnection(String userUUID){
        SseEmitter userEmitter = emitters.get(userUUID);
        if(userEmitter != null){
            try {
                userEmitter.send("CLOSE");
                System.out.println("Koniec Emittera");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            userEmitter.complete();
            emitters.remove(userUUID);
        }
    }
}
