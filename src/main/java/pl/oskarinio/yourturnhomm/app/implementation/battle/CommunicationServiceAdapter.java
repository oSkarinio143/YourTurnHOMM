package pl.oskarinio.yourturnhomm.app.implementation.battle;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import pl.oskarinio.yourturnhomm.infrastructure.temp.CommunicationService;
import pl.oskarinio.yourturnhomm.infrastructure.temp.CommunicationUseCase;

@Service
public class CommunicationServiceAdapter implements CommunicationUseCase {
    private final CommunicationService communicationService;

    public CommunicationServiceAdapter(){
        this.communicationService = new CommunicationService();
    }

    @Override
    public String createUserUUID() {
        return communicationService.createUserUUID();
    }

    @Override
    public SseEmitter createEmitter(String userUUID) {
        return communicationService.createEmitter(userUUID);
    }

    @Override
    public void sendMessage(String userUUID, String message) {
        communicationService.sendMessage(userUUID,message);
    }

    @Override
    public void closeConnection(String userUUID) {
        communicationService.closeConnection(userUUID);
    }
}
