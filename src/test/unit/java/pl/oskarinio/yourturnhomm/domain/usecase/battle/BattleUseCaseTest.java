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
import pl.oskarinio.yourturnhomm.domain.model.form.DuelForm;
import pl.oskarinio.yourturnhomm.domain.port.battle.Queue;

import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static pl.oskarinio.yourturnhomm.domain.usecase.battle.BattleUtilities.*;

@ExtendWith(MockitoExtension.class)
class BattleUseCaseTest {
    @Mock
    private Queue queue;
    @Mock
    private Random random;

    private final double ATTACK_RATE = 0.05;
    private final double DEFENSE_RATE = 0.025;
    private static final UUID USER_UUID = getUserUUID();

    private DuelForm duelForm;

    private BattleUseCase battleUseCase;

    @Captor
    ArgumentCaptor<RoundInfo> captorRoundInfo;

    @BeforeEach
    void SetUp(){
        battleUseCase = new BattleUseCase(queue, ATTACK_RATE, DEFENSE_RATE, random);
        duelForm = getDuelFormLeftUnitFasterWinner();
    }

    @Test
    @DisplayName("Poprawne UUID, duelForm, startuje kolejkę")
    void prepareBattle_correctUUID_resultStartQueue(){
        mockRandomDamage(duelForm,101,11);

        battleUseCase.prepareBattle(duelForm);

        verify(queue, atLeastOnce()).createQueue(captorRoundInfo.capture());
        RoundInfo capturedRoundInfo = captorRoundInfo.getValue();
        assertThat(capturedRoundInfo.getUserUUID()).isEqualTo(USER_UUID);
    }

    @Test
    @DisplayName("Statystyki bohaterów null, ustawia statystyki bohaterów na 0")
    void prepareBattle_heroStatsAreNull_resultHeroStatsSet0(){
        duelForm = getDuelFormWithHeroZeroStats();
        battleUseCase.prepareBattle(duelForm);
        prepareBattle_setHeroStats_assert();
    }

    private void prepareBattle_setHeroStats_assert(){
        assertThat(duelForm.getLeftHeroAttack()).isZero();
        assertThat(duelForm.getLeftHeroDefense()).isZero();
        assertThat(duelForm.getRightHeroAttack()).isZero();
        assertThat(duelForm.getRightHeroDefense()).isZero();
    }

    @Test
    @DisplayName("Lewa jednostka większy speed, lewa jednostka ustawiona jako szybsza")
    void prepareBattle_leftUnitHasMoreSpeed_resultLeftUnitSetFaster(){
        battleUseCase.prepareBattle(duelForm);
        prepareBattle_setLeftUnitFaster_assert();
    }

    private void prepareBattle_setLeftUnitFaster_assert(){
        verify(queue, atLeastOnce()).createQueue(captorRoundInfo.capture());
        RoundInfo capturedRoundInfo = captorRoundInfo.getValue();

        assertThat(duelForm.getLeftUnit().getSpeed()).isGreaterThan(duelForm.getRightUnit().getSpeed());
        assertThat(capturedRoundInfo.getFasterUnit().getName()).isEqualTo(duelForm.getLeftUnit().getName());
    }

    @Test
    @DisplayName("Lewa jednostka zwyciężą, prawej jednostce pozostaje 0 żywych")
    void prepareBattle_leftUnitWin_resultRightUnitHas0LiveUnits(){
        mockRandomDamage(duelForm,101,11);

        battleUseCase.prepareBattle(duelForm);

        verify(queue, atLeastOnce()).createQueue(captorRoundInfo.capture());
        RoundInfo capturedRoundInfo = captorRoundInfo.getValue();
        assertThat(capturedRoundInfo.getSlowerLiveUnits()).isZero();
    }

    @Test
    @DisplayName("Prawa jednostka nie ma żywych, wolniejsza, prawa jednostka zadaje 0 dmg")
    void prepareBattle_rightUnitNoLiveUnitsAndSlower_resultRightUnitDealNoDmg(){
        mockRandomDamage(duelForm,101,11);
        battleUseCase.prepareBattle(duelForm);
        prepareBattle_rightUnitDealNoDmg_assert();
    }

    private void prepareBattle_rightUnitDealNoDmg_assert(){
        verify(queue, atLeastOnce()).createQueue(captorRoundInfo.capture());
        RoundInfo capturedRoundInfo = captorRoundInfo.getValue();
        assertThat(capturedRoundInfo.getSlowerDmg()).isZero();
    }

    @Test
    @DisplayName("Lewa jednostka, nie ma żywych, szybsza, lewa jednostka zadaje >0 dmg")
    void prepareBattle_rightUnitNoLiveUnitsFaster_RightUnitDealDmg(){
        duelForm.getRightUnit().setSpeed(50);
        mockRandomDamage(duelForm,101,11);

        battleUseCase.prepareBattle(duelForm);

        prepareBattle_leftUnitDealDmg_assert();
    }

    private void prepareBattle_leftUnitDealDmg_assert(){
        verify(queue, atLeastOnce()).createQueue(captorRoundInfo.capture());
        RoundInfo capturedRoundInfo = captorRoundInfo.getValue();

        assertThat(capturedRoundInfo.getFasterLiveUnits()).isZero();
        assertThat(capturedRoundInfo.getFasterDmg()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Lewa jednostka 0 jednostek, kolejka nie startuje")
    void prepareBattle_leftUnitQuantityIs0_queueNotStart(){
        duelForm.setLeftQuantity(0);
        battleUseCase.prepareBattle(duelForm);
        verify(queue, never()).createQueue(any());
    }

    void mockRandomDamage(DuelForm duelForm, int leftDamage, int rightDamage){
        lenient().when(random.nextInt(duelForm.getLeftUnit().getMaxDamage() - duelForm.getLeftUnit().getMinDamage() + 1)).thenReturn(leftDamage);
        lenient().when(random.nextInt(duelForm.getRightUnit().getMaxDamage() - duelForm.getRightUnit().getMinDamage() + 1)).thenReturn(rightDamage);
    }
}
