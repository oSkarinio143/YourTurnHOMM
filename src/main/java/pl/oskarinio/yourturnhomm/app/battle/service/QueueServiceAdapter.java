package pl.oskarinio.yourturnhomm.app.battle.service;

import org.springframework.stereotype.Service;
import pl.oskarinio.yourturnhomm.domain.service.battle.QueueService;
import pl.oskarinio.yourturnhomm.app.battle.port.in.MessageCreatorUseCase;
import pl.oskarinio.yourturnhomm.app.battle.port.in.QueueUseCase;
import pl.oskarinio.yourturnhomm.domain.model.battle.RoundInfo;

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
