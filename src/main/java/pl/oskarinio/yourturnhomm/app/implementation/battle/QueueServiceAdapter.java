package pl.oskarinio.yourturnhomm.app.implementation.battle;

import org.springframework.stereotype.Service;
import pl.oskarinio.yourturnhomm.domain.model.battle.RoundInfo;
import pl.oskarinio.yourturnhomm.domain.port.in.battle.MessageCreatorUseCase;
import pl.oskarinio.yourturnhomm.domain.port.in.battle.QueueUseCase;
import pl.oskarinio.yourturnhomm.domain.service.battle.QueueService;

@Service
public class QueueServiceAdapter implements QueueUseCase {

    private final QueueService queueService;

    public QueueServiceAdapter(MessageCreatorUseCase messageCreatorUseCase){
        this.queueService = new QueueService(messageCreatorUseCase);
    }

    @Override
    public void createQueue(RoundInfo roundInfo) {
        queueService.createQueue(roundInfo);
    }
}
