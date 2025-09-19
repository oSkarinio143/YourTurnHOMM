package pl.oskarinio.yourturnhomm.app.business.battle;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.oskarinio.yourturnhomm.domain.model.battle.RoundInfo;
import pl.oskarinio.yourturnhomm.domain.port.battle.MessageCreator;
import pl.oskarinio.yourturnhomm.domain.port.out.MessageSender;
import pl.oskarinio.yourturnhomm.domain.usecase.battle.MessageCreatorUseCase;

@Slf4j
@Service
public class MessageCreatorService implements MessageCreator {
    private final MessageCreatorUseCase messageCreatorUseCase;

    public MessageCreatorService(MessageSender messageSender) {
        this.messageCreatorUseCase = new MessageCreatorUseCase(messageSender);
    }

    @Override
    public void sendRoundMess(RoundInfo roundInfo) {
        log.debug("Wysyłam wiadomość o rozpoczęciu rundy. Runda = {}", roundInfo.getRoundCounter());
        messageCreatorUseCase.sendRoundMess(roundInfo);
    }

    @Override
    public void sendAttackFasterMess(RoundInfo roundInfo) {
        log.debug("Wysyłam wiadomość: szybsza jednostka atakuje. Runda = {}", roundInfo.getRoundCounter());
        messageCreatorUseCase.sendAttackFasterMess(roundInfo);
    }

    @Override
    public void sendAttackSlowerMess(RoundInfo roundInfo) {
        log.debug("Wysyłam wiadomość: wolniejsza jednostka atakuje. Runda = {}", roundInfo.getRoundCounter());
        messageCreatorUseCase.sendAttackSlowerMess(roundInfo);
    }

    @Override
    public void sendWinnerMess(RoundInfo roundInfo) {
        log.debug("Wysyłam wiadomość o zwycięzcy. Runda = {}", roundInfo.getRoundCounter());
        messageCreatorUseCase.sendWinnerMess(roundInfo);
    }
}
