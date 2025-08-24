package pl.oskarinio.yourturnhomm.app.port.in.battle;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface CommunicationUseCase {
    String createUserUUID();
    SseEmitter createEmitter(String userUUID);
    void sendMessage(String userUUID, String message);
    void closeConnection(String userUUID);
}
