package pl.oskarinio.yourturnhomm.infrastructure.adapter.out;

import org.springframework.stereotype.Service;
import pl.oskarinio.yourturnhomm.domain.port.out.MessageSender;
import pl.oskarinio.yourturnhomm.infrastructure.usecase.communication.CommunicationUseCase;

@Service
public class MessageSenderService implements MessageSender {
    private final CommunicationUseCase communicationUseCase;

    public MessageSenderService(CommunicationUseCase communicationUseCase) {
        this.communicationUseCase = communicationUseCase;
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
