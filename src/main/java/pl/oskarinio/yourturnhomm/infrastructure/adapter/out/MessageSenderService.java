package pl.oskarinio.yourturnhomm.infrastructure.adapter.out;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.oskarinio.yourturnhomm.domain.port.out.MessageSender;
import pl.oskarinio.yourturnhomm.infrastructure.usecase.communication.CommunicationUseCase;

@Slf4j
@Service
public class MessageSenderService implements MessageSender {
    private final CommunicationUseCase communicationUseCase;

    public MessageSenderService(CommunicationUseCase communicationUseCase) {
        this.communicationUseCase = communicationUseCase;
    }

    @Override
    public void sendMessage(String userUUID, String message) {
        log.trace("Wysylam wiadomosc do uzytkownika. UUID = {}", userUUID);
        communicationUseCase.sendMessage(userUUID, message);
    }

    @Override
    public void closeConnection(String userUUID) {
        log.debug("Zamykam polaczenie z uzytkownikiem. UUID = {}", userUUID);
        communicationUseCase.closeConnection(userUUID);
    }
}
