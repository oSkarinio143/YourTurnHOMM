package pl.oskarinio.yourturnhomm.app.business.battle;

import org.springframework.stereotype.Service;
import pl.oskarinio.yourturnhomm.domain.model.battle.RoundInfo;
import pl.oskarinio.yourturnhomm.domain.port.battle.MessageCreator;
import pl.oskarinio.yourturnhomm.domain.port.out.MessageSender;
import pl.oskarinio.yourturnhomm.domain.usecase.battle.MessageCreatorUseCase;

@Service
public class MessageCreatorService implements MessageCreator {
    private final MessageCreatorUseCase messageCreatorUseCase;

    public MessageCreatorService(MessageSender messageSender) {
        this.messageCreatorUseCase = new MessageCreatorUseCase(messageSender);
    }

    @Override
    public void sendRoundMess(RoundInfo roundInfo) {
        messageCreatorUseCase.sendRoundMess(roundInfo);
    }

    @Override
    public void sendAttackFasterMess(RoundInfo roundInfo) {
        messageCreatorUseCase.sendAttackFasterMess(roundInfo);
    }

    @Override
    public void sendAttackSlowerMess(RoundInfo roundInfo) {
        messageCreatorUseCase.sendAttackSlowerMess(roundInfo);
    }

    @Override
    public void sendWinnerMess(RoundInfo roundInfo) {
        messageCreatorUseCase.sendWinnerMess(roundInfo);
    }
}
