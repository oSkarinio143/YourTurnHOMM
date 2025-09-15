package pl.oskarinio.yourturnhomm.domain.port.out;

public interface MessageSender {
    void sendMessage(String userUUID, String message);
    void closeConnection(String userUUID);
}
