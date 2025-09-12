package pl.oskarinio.yourturnhomm.infrastructure.temp;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
public class CommunicationService implements Communication {
    private final CommunicationUseCase communicationUseCase;

    public CommunicationService(){
        this.communicationUseCase = new CommunicationUseCase();
    }

    @Override
    public String createUserUUID() {
        return communicationUseCase.createUserUUID();
    }

    @Override
    public SseEmitter createEmitter(String userUUID) {
        return communicationUseCase.createEmitter(userUUID);
    }

    @Override
    public void sendMessage(String userUUID, String message) {
        communicationUseCase.sendMessage(userUUID,message);
    }

    @Override
    public void closeConnection(String userUUID) {
        communicationUseCase.closeConnection(userUUID);
    }
}
