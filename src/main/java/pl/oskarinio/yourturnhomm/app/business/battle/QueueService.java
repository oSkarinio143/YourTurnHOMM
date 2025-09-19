package pl.oskarinio.yourturnhomm.app.business.battle;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import pl.oskarinio.yourturnhomm.domain.model.battle.RoundInfo;
import pl.oskarinio.yourturnhomm.domain.port.battle.MessageCreator;
import pl.oskarinio.yourturnhomm.domain.port.battle.Queue;
import pl.oskarinio.yourturnhomm.domain.port.out.MdcScheduledExecutor;
import pl.oskarinio.yourturnhomm.domain.usecase.battle.QueueUseCase;

@Slf4j
@Service
public class QueueService implements Queue {

    private final QueueUseCase queueUseCase;

    public QueueService(MessageCreator messageCreator, MdcScheduledExecutor mdcScheduledExecutor){
        this.queueUseCase = new QueueUseCase(messageCreator, mdcScheduledExecutor);
    }

    @Override
    public void createQueue(RoundInfo roundInfo) {
        log.debug("Tworzę kolejkę dla rundy. Runda = {}", roundInfo.getRoundCounter());
        System.out.println("MdcekQueue- " + MDC.get("traceId"));
        queueUseCase.createQueue(roundInfo);
    }
}
