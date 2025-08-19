package pl.oskarinio.yourturnhomm.app.battle.service;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import pl.oskarinio.yourturnhomm.domain.service.battle.CommunicationService;
import pl.oskarinio.yourturnhomm.app.battle.port.in.CommunicationUseCase;

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
