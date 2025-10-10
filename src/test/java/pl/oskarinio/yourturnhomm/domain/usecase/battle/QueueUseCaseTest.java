package pl.oskarinio.yourturnhomm.domain.usecase.battle;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;
import static pl.oskarinio.yourturnhomm.domain.usecase.battle.BattleUtilities.getRoundInfo;

@ExtendWith(MockitoExtension.class)
class QueueUseCaseTest {
    @Mock
    private MdcScheduledExecutor scheduler;
    @Mock
    private MessageCreator messageCreator;
    @Captor
    private ArgumentCaptor<Runnable> captorRunnable;

    private QueueUseCase queueUseCase;

    @BeforeEach
    void SetUp() {
        queueUseCase = new QueueUseCase(messageCreator, scheduler);
    }

    @Test
    @DisplayName("Poprawne RoundInfo, kolejka stworzona")
    void createQueue_correctRoundInfo_resultQueueCreated(){
        RoundInfo roundInfo = getRoundInfo();
        queueUseCase.createQueue(roundInfo);
        createQueue_assertOrder();
        createQueue_assertMessages(roundInfo);
    }

    private void createQueue_assertOrder(){
        InOrder inOrderScheduler = inOrder(scheduler);
        inOrderScheduler.verify(scheduler).schedule(captorRunnable.capture(), eq(1L), eq(TimeUnit.SECONDS));
        inOrderScheduler.verify(scheduler).schedule(captorRunnable.capture(), eq(2L), eq(TimeUnit.SECONDS));
        inOrderScheduler.verify(scheduler).schedule(captorRunnable.capture(), eq(3L), eq(TimeUnit.SECONDS));
        inOrderScheduler.verify(scheduler).schedule(captorRunnable.capture(), eq(4L), eq(TimeUnit.SECONDS));
    }

    private void createQueue_assertMessages(RoundInfo roundInfo){
        List<Runnable> runnables = captorRunnable.getAllValues();
        runnables.forEach(Runnable::run);

        InOrder inOrderMessageCreator = inOrder(messageCreator);
        inOrderMessageCreator.verify(messageCreator).sendRoundMess(roundInfo);
        inOrderMessageCreator.verify(messageCreator).sendAttackFasterMess(roundInfo);
        inOrderMessageCreator.verify(messageCreator).sendAttackSlowerMess(roundInfo);
        inOrderMessageCreator.verify(messageCreator).sendWinnerMess(roundInfo);
    }

}
