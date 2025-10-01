package pl.oskarinio.yourturnhomm.domain.service.battle;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.oskarinio.yourturnhomm.domain.model.battle.BattleUnit;
import pl.oskarinio.yourturnhomm.domain.model.battle.RoundInfo;
import pl.oskarinio.yourturnhomm.domain.port.out.MessageSender;
import pl.oskarinio.yourturnhomm.domain.usecase.battle.MessageCreatorUseCase;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MessageCreatorUseCaseTest {

    @Mock
    private MessageSender messageSender;

    @Captor
    private ArgumentCaptor<String> captorUuid;
    @Captor
    private ArgumentCaptor<String> captorMessage;

    private String TEST_USERUUID = "uuid123";
    private String TEST_MESSAGE = "message123";

    private MessageCreatorUseCase messageCreatorUseCase;

    @BeforeEach
    void SetUP(){
        messageCreatorUseCase = new MessageCreatorUseCase(messageSender);
    }

    @Test
    void sendRoundMessage_correctValues(){
        RoundInfo roundInfo = getRoundInfo();
        messageCreatorUseCase.sendRoundMess(roundInfo);

        verify(messageSender).sendMessage(captorUuid.capture(), captorMessage.capture());
        assertThat(captorUuid.getValue()).isEqualTo(TEST_USERUUID);
        assertThat(captorMessage.getValue()).isEqualTo("ROUND:Runda 1");
    }

    private RoundInfo getRoundInfo(){
        RoundInfo roundInfo = new RoundInfo(TEST_USERUUID, 1, 100);
        roundInfo.setFasterDmg(200);
        roundInfo.setSlowerDmg(100);
        roundInfo.setFasterDeathUnits(2);
        roundInfo.setSlowerDeathUnits(1);
        roundInfo.setFasterLiveUnits(20);
        roundInfo.setSlowerLiveUnits(10);
        roundInfo.setFasterUnit(getTestUnit(10));
        roundInfo.setSlowerUnit(getTestUnit(1));
        roundInfo.setWinnerUnit(getTestUnit());
        roundInfo.setLoserUnit(getTestUnit());
        roundInfo.setWinner(true);
        roundInfo.setFasterLastAttackUnits(30);
        roundInfo.setTempDelay(1000);
        return roundInfo;
    }

    private BattleUnit getTestUnit(){
        BattleUnit unit = new BattleUnit("testUnit", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        return unit;
    }

    private BattleUnit getTestUnit(int speed){
        BattleUnit unit = new BattleUnit("testUnit", 1, 2, 3, 4, 5, 6, speed, 8, 9, 10);
        return unit;
    }
}
