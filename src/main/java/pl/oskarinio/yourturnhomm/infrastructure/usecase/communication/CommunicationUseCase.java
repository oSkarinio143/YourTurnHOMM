package pl.oskarinio.yourturnhomm.infrastructure.usecase.communication;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Component
public class CommunicationUseCase {
    private final Map<UUID, SseEmitter> emitters;

    public CommunicationUseCase (Map<UUID, SseEmitter> emitters){
        this.emitters = emitters;
    }

    public UUID createUserUUID(){
        UUID userUUID;
        do {
            userUUID = UUID.randomUUID();
        } while (emitters.containsKey(userUUID));
        return userUUID;
    }

    public SseEmitter createEmitter(UUID userUUID){
        SseEmitter emitter = new SseEmitter(3600000L);
        emitters.put(userUUID, emitter);
        return emitter;
    }

    public void sendMessage(UUID userUUID, String message) {
        SseEmitter userEmitter = emitters.get(userUUID);
        if(userEmitter != null) {
            try {
                userEmitter.send(message);
            } catch (IOException e) {
                userEmitter.completeWithError(e);
                emitters.remove(userUUID);
            }
        }
    }

    public void closeConnection(UUID userUUID){
        SseEmitter userEmitter = emitters.get(userUUID);
        if(userEmitter != null){
            try {
                userEmitter.send("CLOSE");
            } catch (IOException ignored) {
            } finally {
                userEmitter.complete();
                emitters.remove(userUUID);
            }
        }
    }
}
