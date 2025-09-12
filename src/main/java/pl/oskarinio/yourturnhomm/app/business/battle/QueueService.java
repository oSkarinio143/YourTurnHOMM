package pl.oskarinio.yourturnhomm.app.business.battle;

import org.springframework.stereotype.Service;
import pl.oskarinio.yourturnhomm.domain.model.battle.RoundInfo;
import pl.oskarinio.yourturnhomm.domain.port.battle.MessageCreator;
import pl.oskarinio.yourturnhomm.domain.port.battle.Queue;
import pl.oskarinio.yourturnhomm.domain.usecase.battle.QueueUseCase;

@Service
public class QueueService implements Queue {

    private final QueueUseCase queueUseCase;

    public QueueService(MessageCreator messageCreator){
        this.queueUseCase = new QueueUseCase(messageCreator);
    }

    @Override
    public void createQueue(RoundInfo roundInfo) {
        queueUseCase.createQueue(roundInfo);
    }
}
