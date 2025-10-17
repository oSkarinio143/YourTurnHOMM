package pl.oskarinio.yourturnhomm.domain.usecase.battle;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.oskarinio.yourturnhomm.domain.model.battle.Side;
import pl.oskarinio.yourturnhomm.domain.model.battle.Unit;
import pl.oskarinio.yourturnhomm.domain.model.form.DuelForm;
import pl.oskarinio.yourturnhomm.domain.port.battle.Battle;
import pl.oskarinio.yourturnhomm.domain.port.out.UnitRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static pl.oskarinio.yourturnhomm.domain.usecase.battle.BattleUtilities.*;

@ExtendWith(MockitoExtension.class)
class DuelUseCaseTest {
    @Mock
    private UnitRepository unitRepository;
    private Battle battle;

    private static final String UNIT_NAME = BattleUtilities.getUnitName();

    private DuelForm duelForm;

    private DuelUseCase duelUseCase;

    @BeforeEach
    void SetUp(){
        duelUseCase = new DuelUseCase(unitRepository, battle);

        duelForm = getDuelFormWithoutSetSides();
    }

    @ParameterizedTest
    @EnumSource(value = Side.class, names = {"LEFT","RIGHT"})
    @DisplayName("Strona left/right, ładuje jednostkę na stronie left/right")
    void loadUnit_sideCorrectValue_resultSetUnitThisSide(Side side){
        Unit testUnit = getTestUnit();
        when(unitRepository.getReferenceById(UNIT_NAME)).thenReturn(testUnit);

        duelUseCase.loadUnit(duelForm, side, UNIT_NAME);

        loadUnit_assert(testUnit,side);
    }

    private void loadUnit_assert(Unit testUnit, Side side){
        if(side.equals(Side.LEFT)) {
            assertThat(duelForm.getRightUnit()).isNull();
            assertThat(duelForm.getLeftUnit()).isEqualTo(testUnit);
        }

        if(side.equals(Side.RIGHT)){
            assertThat(duelForm.getRightUnit()).isEqualTo(testUnit);
            assertThat(duelForm.getLeftUnit()).isNull();
        }
    }

    @Test
    @DisplayName("Strona null, nic sie nie dzieje")
    void loadUnit_sideIsNull_resultNothingHappened(){
        duelUseCase.loadUnit(duelForm, null, UNIT_NAME);

        assertThat(duelForm.getLeftUnit()).isNull();
        assertThat(duelForm.getRightUnit()).isNull();
    }
}

