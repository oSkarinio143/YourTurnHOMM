package pl.oskarinio.yourturnhomm.domain.port.out;

import java.util.UUID;

public interface MessageSender {
    void sendMessage(UUID userUUID, String message);
    void closeConnection(UUID userUUID);
}
