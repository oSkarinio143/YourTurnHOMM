package pl.oskarinio.yourturnhomm.domain.service.battle;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.oskarinio.yourturnhomm.domain.model.battle.RoundInfo;
import pl.oskarinio.yourturnhomm.domain.model.form.DuelForm;
import pl.oskarinio.yourturnhomm.domain.port.battle.Queue;
import pl.oskarinio.yourturnhomm.domain.usecase.battle.BattleUseCase;

import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static pl.oskarinio.yourturnhomm.domain.service.battle.BattleUtilities.*;

@ExtendWith(MockitoExtension.class)
public class BattleUseCaseTest {
    @Mock
    private Queue queue;
    @Mock
    private Random random;

    private final double ATK_RATE = 0.05;
    private final double DEF_RATE = 0.025;
    private static final UUID TEST_USERUUID = getUserUUID();

    private BattleUseCase battleUseCase;

    @Captor
    ArgumentCaptor<RoundInfo> captorRoundInfo;

    @BeforeEach
    void SetUp(){
        battleUseCase = new BattleUseCase(queue, ATK_RATE, DEF_RATE, random);
    }

    @Test
    public void prepareBattle_startQueueWithCorrectUUID(){
        DuelForm duelForm = getDuelFormLeftUnitFasterWinner();
        mockRandomDamage(duelForm,101,11);

        battleUseCase.prepareBattle(duelForm);
        verify(queue, atLeastOnce()).createQueue(captorRoundInfo.capture());
        RoundInfo capturedRoundInfo = captorRoundInfo.getValue();

        verify(random, atLeastOnce()).nextInt(anyInt());
        assertThat(capturedRoundInfo.getUserUUID()).isEqualTo(TEST_USERUUID);
    }

    @Test
    public void prepareBattle_heroStatsAreNull_resultHeroStatsSet0(){
        DuelForm duelForm = getDuelFormWithHeroZeroStats();

        battleUseCase.prepareBattle(duelForm);

        verify(queue, atLeastOnce()).createQueue(any(RoundInfo.class));
        assertThat(duelForm.getLeftHeroAttack()).isEqualTo(0);
        assertThat(duelForm.getLeftHeroDefense()).isEqualTo(0);
        assertThat(duelForm.getRightHeroAttack()).isEqualTo(0);
        assertThat(duelForm.getRightHeroDefense()).isEqualTo(0);
    }

    @Test
    public void prepareBattle_leftUnitHasMoreSpeed_resultLeftUnitSetFaster(){
        DuelForm duelForm = getDuelFormLeftUnitFasterWinner();

        battleUseCase.prepareBattle(duelForm);
        verify(queue, atLeastOnce()).createQueue(captorRoundInfo.capture());
        RoundInfo capturedRoundInfo = captorRoundInfo.getValue();

        assertThat(duelForm.getLeftUnit().getSpeed()).isGreaterThan(duelForm.getRightUnit().getSpeed());
        assertThat(capturedRoundInfo.getFasterUnit().getName()).isEqualTo(duelForm.getLeftUnit().getName());
        assertThat(capturedRoundInfo.getSlowerUnit().getName()).isEqualTo(duelForm.getRightUnit().getName());
    }

    @Test
    public void prepareBattle_leftUnitWin_resultRightUnitHas0LiveUnits(){
        DuelForm duelForm = getDuelFormLeftUnitFasterWinner();
        mockRandomDamage(duelForm,101,11);

        battleUseCase.prepareBattle(duelForm);
        verify(queue, atLeastOnce()).createQueue(captorRoundInfo.capture());
        RoundInfo capturedRoundInfo = captorRoundInfo.getValue();

        verify(random, atLeastOnce()).nextInt(anyInt());
        assertThat(capturedRoundInfo.getWinnerUnit().getName()).isEqualTo(duelForm.getLeftUnit().getName()).isEqualTo(capturedRoundInfo.getFasterUnit().getName());
        assertThat(capturedRoundInfo.getLoserUnit().getName()).isEqualTo(duelForm.getRightUnit().getName()).isEqualTo(capturedRoundInfo.getSlowerUnit().getName());
        assertThat(capturedRoundInfo.getFasterLiveUnits()).isGreaterThan(0);
        assertThat(capturedRoundInfo.getSlowerLiveUnits()).isZero();
    }

    @Test
    public void prepareBattle_rightUnitHas0LiveUnitsAndIsSlower_RightUnitDeal0Dmg(){
        DuelForm duelForm = getDuelFormLeftUnitFasterWinner();
        mockRandomDamage(duelForm,101,11);

        battleUseCase.prepareBattle(duelForm);
        verify(queue, atLeastOnce()).createQueue(captorRoundInfo.capture());
        RoundInfo capturedRoundInfo = captorRoundInfo.getValue();

        verify(random, atLeastOnce()).nextInt(anyInt());
        assertThat(capturedRoundInfo.getSlowerLiveUnits()).isZero();
        assertThat(capturedRoundInfo.getSlowerDmg()).isZero();
    }

    @Test
    public void prepareBattle_rightUnitHas0LiveUnitsAndIsFaster_RightUnitDealDmg(){
        DuelForm duelForm = getDuelFormLeftUnitFasterWinner();
        duelForm.getRightUnit().setSpeed(50);
        mockRandomDamage(duelForm,101,11);

        battleUseCase.prepareBattle(duelForm);
        verify(queue, atLeastOnce()).createQueue(captorRoundInfo.capture());
        RoundInfo capturedRoundInfo = captorRoundInfo.getValue();

        verify(random, atLeastOnce()).nextInt(anyInt());
        assertThat(capturedRoundInfo.getFasterLiveUnits()).isZero();
        assertThat(capturedRoundInfo.getFasterDmg()).isGreaterThan(0);
    }

    @Test
    public void prepareBattle_rightUnitNotKill_LeftUnitLeftHpIsNotFull(){
        DuelForm duelForm = getDuelFormLeftUnitFasterWinner();
        duelForm.getRightUnit().setSpeed(50);
        mockRandomDamage(duelForm,101,11);

        battleUseCase.prepareBattle(duelForm);
        verify(queue, atLeastOnce()).createQueue(captorRoundInfo.capture());
        RoundInfo capturedRoundInfo = captorRoundInfo.getValue();

        verify(random, atLeastOnce()).nextInt(anyInt());
        assertThat(capturedRoundInfo.getWinnerUnit().getHpLeft()).isGreaterThan(0);
        assertThat(capturedRoundInfo.getWinnerUnit().getHpLeft()).isLessThan(capturedRoundInfo.getWinnerUnit().getHp());
    }

    @Test
    public void prepareBattle_leftUnitQuantityIs0_queueNotStart(){
        DuelForm duelForm = getDuelFormLeftUnitFasterWinner();
        duelForm.setLeftQuantity(0);

        battleUseCase.prepareBattle(duelForm);

        verify(queue, times(0)).createQueue(any());
    }

    private void mockRandomDamage(DuelForm duelForm, int leftDamage, int rightDamage){
        lenient().when(random.nextInt(duelForm.getLeftUnit().getMaxDamage() - duelForm.getLeftUnit().getMinDamage() + 1)).thenReturn(leftDamage);
        lenient().when(random.nextInt(duelForm.getRightUnit().getMaxDamage() - duelForm.getRightUnit().getMinDamage() + 1)).thenReturn(rightDamage);
    }
}
