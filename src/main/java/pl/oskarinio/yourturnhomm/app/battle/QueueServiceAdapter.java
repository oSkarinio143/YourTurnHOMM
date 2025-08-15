package pl.oskarinio.yourturnhomm.app.battle;

import org.springframework.stereotype.Service;
import pl.oskarinio.yourturnhomm.domain.battle.service.QueueService;
import pl.oskarinio.yourturnhomm.domain.battle.port.in.MessageCreatorUseCase;
import pl.oskarinio.yourturnhomm.domain.battle.port.in.QueueUseCase;
import pl.oskarinio.yourturnhomm.domain.battle.model.RoundInfo;

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
