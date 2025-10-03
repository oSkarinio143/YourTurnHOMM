package pl.oskarinio.yourturnhomm.domain.service.battle;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.oskarinio.yourturnhomm.domain.model.battle.RoundInfo;
import pl.oskarinio.yourturnhomm.domain.port.battle.MessageCreator;
import pl.oskarinio.yourturnhomm.domain.port.out.MdcScheduledExecutor;
import pl.oskarinio.yourturnhomm.domain.usecase.battle.QueueUseCase;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static pl.oskarinio.yourturnhomm.domain.service.battle.BattleUtilities.getRoundInfo;
import static pl.oskarinio.yourturnhomm.domain.service.battle.BattleUtilities.getUserUUID;

@ExtendWith(MockitoExtension.class)
class QueueUseCaseTest {
    @Mock
    private MdcScheduledExecutor scheduler;
    @Mock
    private MessageCreator messageCreator;
    @Captor
    private ArgumentCaptor<Runnable> captorRunnable;

    private static final UUID TEST_USERUUID = getUserUUID();

    private QueueUseCase queueUseCase;

    @BeforeEach
    void SetUp() {
        queueUseCase = new QueueUseCase(messageCreator, scheduler);
    }

    @Test
    void createQueue_correctValues(){
        RoundInfo roundInfo = getRoundInfo();

        queueUseCase.createQueue(roundInfo);
        InOrder inOrderScheduler = inOrder(scheduler);
        inOrderScheduler.verify(scheduler).schedule(captorRunnable.capture(), eq(1L), eq(TimeUnit.SECONDS));
        inOrderScheduler.verify(scheduler).schedule(captorRunnable.capture(), eq(2L), eq(TimeUnit.SECONDS));
        inOrderScheduler.verify(scheduler).schedule(captorRunnable.capture(), eq(3L), eq(TimeUnit.SECONDS));
        inOrderScheduler.verify(scheduler).schedule(captorRunnable.capture(), eq(4L), eq(TimeUnit.SECONDS));

        List<Runnable> runnables = captorRunnable.getAllValues();
        runnables.forEach(Runnable::run);

        InOrder inOrderMessageCreator = inOrder(messageCreator);
        inOrderMessageCreator.verify(messageCreator).sendRoundMess(roundInfo);
        inOrderMessageCreator.verify(messageCreator).sendAttackFasterMess(roundInfo);
        inOrderMessageCreator.verify(messageCreator).sendAttackSlowerMess(roundInfo);
        inOrderMessageCreator.verify(messageCreator).sendWinnerMess(roundInfo);
    }
}
