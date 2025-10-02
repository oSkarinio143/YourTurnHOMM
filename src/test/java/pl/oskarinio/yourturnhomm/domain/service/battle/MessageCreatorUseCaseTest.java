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
import static pl.oskarinio.yourturnhomm.domain.service.battle.BattleTestHelper.getRoundInfo;
import static pl.oskarinio.yourturnhomm.domain.service.battle.BattleTestHelper.getUSERUUID;

@ExtendWith(MockitoExtension.class)
class MessageCreatorUseCaseTest {
    @Mock
    private MessageSender messageSender;
    @Captor
    private ArgumentCaptor<UUID> captorUuid;
    @Captor
    private ArgumentCaptor<String> captorMessage;

    private static final UUID TEST_USERUUID = getUSERUUID();

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
        assertThat(captorMessage.getValue()).isEqualTo("ATTACKF:Jednostka 30xwinnerUnit atakuje pierwsza, zadajac 200.<br>Pokonuje 1xloserUnit");
    }

    @Test
    void sendAttackFasterMessage_noKillUnit_resultMessageAttackFasterBasic(){
        RoundInfo roundInfo = getRoundInfo();
        roundInfo.setSlowerDeathUnits(0);

        messageCreatorUseCase.sendAttackFasterMess(roundInfo);

        verify(messageSender).sendMessage(captorUuid.capture(), captorMessage.capture());
        assertThat(captorUuid.getValue()).isEqualTo(TEST_USERUUID);
        assertThat(captorMessage.getValue()).isEqualTo("ATTACKF:Jednostka 30xwinnerUnit atakuje pierwsza, zadajac 200.");
    }

    @Test
    void sendAttackSlowerMessage_killUnit_resultMessageAttackSlowerKill(){
        RoundInfo roundInfo = getRoundInfo();

        messageCreatorUseCase.sendAttackSlowerMess(roundInfo);

        verify(messageSender).sendMessage(captorUuid.capture(), captorMessage.capture());
        assertThat(captorUuid.getValue()).isEqualTo(TEST_USERUUID);
        assertThat(captorMessage.getValue()).isEqualTo("ATTACKS:Jednostka 10xloserUnit atakuje druga, zadajac 100.<br>Pokonuje 2xwinnerUnit");
    }

    @Test
    void sendAttackSlowerMessage_noKillUnit_resultMessageAttackSlowerBasic(){
        RoundInfo roundInfo = getRoundInfo();
        roundInfo.setFasterDeathUnits(0);

        messageCreatorUseCase.sendAttackSlowerMess(roundInfo);

        verify(messageSender).sendMessage(captorUuid.capture(), captorMessage.capture());
        assertThat(captorUuid.getValue()).isEqualTo(TEST_USERUUID);
        assertThat(captorMessage.getValue()).isEqualTo("ATTACKS:Jednostka 10xloserUnit atakuje druga, zadajac 100.");
    }

    @Test
    void sendWinnerMessage_correctValues(){
        RoundInfo roundInfo = getRoundInfo();

        messageCreatorUseCase.sendWinnerMess(roundInfo);

        verify(messageSender).sendMessage(captorUuid.capture(), captorMessage.capture());
        assertThat(captorUuid.getValue()).isEqualTo(TEST_USERUUID);
        assertThat(captorMessage.getValue()).isEqualTo("VICTORY:loserUnit gina. winnerUnit wygrywaja pojedynek");
        verify(messageSender).closeConnection(TEST_USERUUID);
    }
}
