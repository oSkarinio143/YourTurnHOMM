package pl.oskarinio.yourturnhomm.infrastructure.port.communication;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;

public interface Communication {
    UUID createUserUUID();
    SseEmitter createEmitter(UUID userUUID);
}
