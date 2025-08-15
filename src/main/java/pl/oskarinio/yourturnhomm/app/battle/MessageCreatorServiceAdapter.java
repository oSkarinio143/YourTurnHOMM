package pl.oskarinio.yourturnhomm.app.battle;

import org.springframework.stereotype.Service;
import pl.oskarinio.yourturnhomm.domain.battle.service.MessageCreatorService;
import pl.oskarinio.yourturnhomm.domain.battle.port.in.CommunicationUseCase;
import pl.oskarinio.yourturnhomm.domain.battle.port.in.MessageCreatorUseCase;
import pl.oskarinio.yourturnhomm.domain.battle.model.RoundInfo;

@Service
public class MessageCreatorServiceAdapter implements MessageCreatorUseCase {
    private final MessageCreatorService messageCreatorService;

    public MessageCreatorServiceAdapter(CommunicationUseCase communicationUseCase) {
        this.messageCreatorService = new MessageCreatorService(communicationUseCase);
    }

    @Override
    public void sendRoundMess(RoundInfo roundInfo) {
        messageCreatorService.sendRoundMess(roundInfo);
    }

    @Override
    public void sendAttackFasterMess(RoundInfo roundInfo) {
        messageCreatorService.sendAttackFasterMess(roundInfo);
    }

    @Override
    public void sendAttackSlowerMess(RoundInfo roundInfo) {
        messageCreatorService.sendAttackSlowerMess(roundInfo);
    }

    @Override
    public void sendWinnerMess(RoundInfo roundInfo) {
        messageCreatorService.sendWinnerMess(roundInfo);
    }
}
