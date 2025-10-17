package pl.oskarinio.yourturnhomm.domain.usecase.battle;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.oskarinio.yourturnhomm.domain.model.battle.RoundInfo;
import pl.oskarinio.yourturnhomm.domain.port.out.MessageSender;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static pl.oskarinio.yourturnhomm.domain.usecase.battle.BattleUtilities.getRoundInfo;
import static pl.oskarinio.yourturnhomm.domain.usecase.battle.BattleUtilities.getUserUUID;

@ExtendWith(MockitoExtension.class)
class MessageCreatorUseCaseTest {
    @Mock
    private MessageSender messageSender;
    @Captor
    private ArgumentCaptor<UUID> captorUUID;
    @Captor
    private ArgumentCaptor<String> captorMessage;

    private static final UUID USER_UUID = getUserUUID();

    private RoundInfo roundInfo;

    private MessageCreatorUseCase messageCreatorUseCase;

    @BeforeEach
    void SetUp(){
        messageCreatorUseCase = new MessageCreatorUseCase(messageSender);
        roundInfo = getRoundInfo();
    }

    @Test
    @DisplayName("Poprawne roundInfo, wysyła wiadomość")
    void sendRoundMessage_correctRoundInfo_resultMessageRoundSend(){
        messageCreatorUseCase.sendRoundMess(roundInfo);

        verify(messageSender).sendMessage(captorUUID.capture(), captorMessage.capture());
        assertThat(captorUUID.getValue()).isEqualTo(USER_UUID);
        assertThat(captorMessage.getValue()).isEqualTo("ROUND:Runda 1");
    }

    @Test
    @DisplayName("")
    void sendAttackFasterMessage_fasterKill_resultMessageAttackFasterKillSend(){
        messageCreatorUseCase.sendAttackFasterMess(roundInfo);

        verify(messageSender).sendMessage(captorUUID.capture(), captorMessage.capture());
        assertThat(captorUUID.getValue()).isEqualTo(USER_UUID);
        assertThat(captorMessage.getValue()).isEqualTo("ATTACKF:Jednostka 30xwinnerUnit atakuje pierwsza, zadajac 200.<br>Pokonuje 1xloserUnit");
    }

    @Test
    void sendAttackFasterMessage_fasterNotKill_resultMessageAttackFasterNotKillSend(){
        roundInfo.setSlowerDeathUnits(0);

        messageCreatorUseCase.sendAttackFasterMess(roundInfo);

        verify(messageSender).sendMessage(captorUUID.capture(), captorMessage.capture());
        assertThat(captorUUID.getValue()).isEqualTo(USER_UUID);
        assertThat(captorMessage.getValue()).isEqualTo("ATTACKF:Jednostka 30xwinnerUnit atakuje pierwsza, zadajac 200.");
    }

    @Test
    void sendAttackSlowerMessage_slowerKill_resultMessageAttackSlowerKillSend(){
        messageCreatorUseCase.sendAttackSlowerMess(roundInfo);

        verify(messageSender).sendMessage(captorUUID.capture(), captorMessage.capture());
        assertThat(captorUUID.getValue()).isEqualTo(USER_UUID);
        assertThat(captorMessage.getValue()).isEqualTo("ATTACKS:Jednostka 10xloserUnit atakuje druga, zadajac 100.<br>Pokonuje 2xwinnerUnit");
    }

    @Test
    void sendAttackSlowerMessage_noKillUnit_resultMessageAttackSlowerNotKillSend(){
        roundInfo.setFasterDeathUnits(0);

        messageCreatorUseCase.sendAttackSlowerMess(roundInfo);

        verify(messageSender).sendMessage(captorUUID.capture(), captorMessage.capture());
        assertThat(captorUUID.getValue()).isEqualTo(USER_UUID);
        assertThat(captorMessage.getValue()).isEqualTo("ATTACKS:Jednostka 10xloserUnit atakuje druga, zadajac 100.");
    }

    @Test
    void sendWinnerMessage_correctRoundInfo_resultMessageWinnerSend(){
        messageCreatorUseCase.sendWinnerMess(roundInfo);

        verify(messageSender).sendMessage(captorUUID.capture(), captorMessage.capture());
        assertThat(captorUUID.getValue()).isEqualTo(USER_UUID);
        assertThat(captorMessage.getValue()).isEqualTo("VICTORY:loserUnit gina. winnerUnit wygrywaja pojedynek");
        verify(messageSender).closeConnection(USER_UUID);
    }
}
