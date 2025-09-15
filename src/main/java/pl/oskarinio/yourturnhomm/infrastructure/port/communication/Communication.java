package pl.oskarinio.yourturnhomm.infrastructure.port.communication;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface Communication {
    String createUserUUID();
    SseEmitter createEmitter(String userUUID);
}
