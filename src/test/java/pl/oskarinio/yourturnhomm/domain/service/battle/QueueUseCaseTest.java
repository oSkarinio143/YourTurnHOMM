package pl.oskarinio.yourturnhomm.domain.service.battle;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.oskarinio.yourturnhomm.domain.port.battle.MessageCreator;
import pl.oskarinio.yourturnhomm.domain.port.out.MdcScheduledExecutor;
import pl.oskarinio.yourturnhomm.domain.usecase.battle.QueueUseCase;

@ExtendWith(MockitoExtension.class)
public class QueueUseCaseTest {
    @Mock
    private MdcScheduledExecutor mdcScheduledExecutor;
    @Mock
    private MessageCreator messageCreator;

    private QueueUseCase queueUseCase;

    @BeforeEach
    void SetUp() {
        queueUseCase = new QueueUseCase(messageCreator, mdcScheduledExecutor);
    }

    @Test
    void createQueue(){

    }
}
