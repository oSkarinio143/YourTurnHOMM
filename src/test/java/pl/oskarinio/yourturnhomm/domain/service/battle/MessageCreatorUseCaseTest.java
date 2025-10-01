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
    private ArgumentCaptor<UUID> captorUuid;
    @Captor
    private ArgumentCaptor<String> captorMessage;

    private UUID TEST_USERUUID = UUID.randomUUID();

    private MessageCreatorUseCase messageCreatorUseCase;

    @BeforeEach
    void SetUp(){
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

    @Test
    void sendAttackFasterMessage_killUnit_resultMessageAttackFasterKill(){
        RoundInfo roundInfo = getRoundInfo();

        messageCreatorUseCase.sendAttackFasterMess(roundInfo);

        verify(messageSender).sendMessage(captorUuid.capture(), captorMessage.capture());
        assertThat(captorUuid.getValue()).isEqualTo(TEST_USERUUID);
        assertThat(captorMessage.getValue()).isEqualTo("ATTACKF:Jednostka 30xtestUnit atakuje pierwsza, zadajac 200.<br>Pokonuje 1xtestUnit");
    }

    @Test
    void sendAttackFasterMessage_noKillUnit_resultMessageAttackFasterBasic(){
        RoundInfo roundInfo = getRoundInfo();
        roundInfo.setSlowerDeathUnits(0);

        messageCreatorUseCase.sendAttackFasterMess(roundInfo);

        verify(messageSender).sendMessage(captorUuid.capture(), captorMessage.capture());
        assertThat(captorUuid.getValue()).isEqualTo(TEST_USERUUID);
        assertThat(captorMessage.getValue()).isEqualTo("ATTACKF:Jednostka 30xtestUnit atakuje pierwsza, zadajac 200.");
    }

    @Test
    void sendAttackSlowerMessage_killUnit_resultMessageAttackSlowerKill(){
        RoundInfo roundInfo = getRoundInfo();

        messageCreatorUseCase.sendAttackSlowerMess(roundInfo);

        verify(messageSender).sendMessage(captorUuid.capture(), captorMessage.capture());
        assertThat(captorUuid.getValue()).isEqualTo(TEST_USERUUID);
        assertThat(captorMessage.getValue()).isEqualTo("ATTACKS:Jednostka 10xtestUnit atakuje druga, zadajac 100.<br>Pokonuje 2xtestUnit");
    }

    @Test
    void sendAttackSlowerMessage_noKillUnit_resultMessageAttackSlowerBasic(){
        RoundInfo roundInfo = getRoundInfo();
        roundInfo.setFasterDeathUnits(0);

        messageCreatorUseCase.sendAttackSlowerMess(roundInfo);

        verify(messageSender).sendMessage(captorUuid.capture(), captorMessage.capture());
        assertThat(captorUuid.getValue()).isEqualTo(TEST_USERUUID);
        assertThat(captorMessage.getValue()).isEqualTo("ATTACKS:Jednostka 10xtestUnit atakuje druga, zadajac 100.");
    }

    @Test
    void sendWinnerMessage_correctValues(){
        RoundInfo roundInfo = getRoundInfo();

        messageCreatorUseCase.sendWinnerMess(roundInfo);

        verify(messageSender).sendMessage(captorUuid.capture(), captorMessage.capture());
        assertThat(captorUuid.getValue()).isEqualTo(TEST_USERUUID);
        assertThat(captorMessage.getValue()).isEqualTo("VICTORY:testUnit gina. testUnit wygrywaja pojedynek");
        verify(messageSender).closeConnection(TEST_USERUUID);
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
        return new BattleUnit("testUnit", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    }

    private BattleUnit getTestUnit(int speed){
        return new BattleUnit("testUnit", 1, 2, 3, 4, 5, 6, speed, 8, 9, 10);
    }
}
