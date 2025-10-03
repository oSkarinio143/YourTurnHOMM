package pl.oskarinio.yourturnhomm.domain.usecase.battle;

import org.junit.jupiter.api.BeforeEach;
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

    private DuelUseCase duelUseCase;

    @BeforeEach
    void SetUp(){
        duelUseCase = new DuelUseCase(unitRepository, battle);
    }


    @ParameterizedTest
    @EnumSource(value = Side.class, names = {"LEFT","RIGHT"})
    void loadUnit_sideHasValue_resultSetUnitThisSide(Side side){
        DuelForm duelForm = getDuelFormWithoutSetSides();
        Unit testUnit = getTestUnit(getUnitName());
        when(unitRepository.getReferenceById(getUnitName())).thenReturn(testUnit);

        duelUseCase.loadUnit(duelForm, side, getUnitName());

        if(side.equals(Side.LEFT)){
            assertThat(duelForm.getLeftUnit()).isEqualTo(testUnit);
            assertThat(duelForm.getRightUnit()).isNull();
        }
        if(side.equals(Side.RIGHT)){
            assertThat(duelForm.getRightUnit()).isEqualTo(testUnit);
            assertThat(duelForm.getLeftUnit()).isNull();
        }
    }

    @Test
    void loadUnit_sideIsNull_resultNothingHappened(){
        DuelForm duelForm = getDuelFormWithoutSetSides();
        String tempUnitName = "testUnit";
        Side side = null;

        duelUseCase.loadUnit(duelForm, side, tempUnitName);

        assertThat(duelForm.getLeftUnit()).isNull();
        assertThat(duelForm.getRightUnit()).isNull();
    }
}

