package pl.oskarinio.yourturnhomm.app.technology.communication;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import pl.oskarinio.yourturnhomm.infrastructure.port.communication.Communication;
import pl.oskarinio.yourturnhomm.infrastructure.usecase.communication.CommunicationUseCase;

@Slf4j
@Service
public class CommunicationService implements Communication{
    private final CommunicationUseCase communicationUseCase;

    public CommunicationService(CommunicationUseCase communicationUseCase){
        this.communicationUseCase = communicationUseCase;
    }

    @Override
    public String createUserUUID() {
        String uuid = communicationUseCase.createUserUUID();
        log.debug("Tworze nowe userUUID. UUID = {}", uuid);
        return uuid;
    }

    @Override
    public SseEmitter createEmitter(String userUUID) {
        log.debug("Tworze emitter dla uzytkownika. UUID = {}", userUUID);
        return communicationUseCase.createEmitter(userUUID);
    }
}
