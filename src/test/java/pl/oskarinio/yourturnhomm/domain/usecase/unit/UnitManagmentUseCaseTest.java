package pl.oskarinio.yourturnhomm.domain.usecase.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.oskarinio.yourturnhomm.domain.model.battle.Unit;
import pl.oskarinio.yourturnhomm.domain.model.exception.DuplicateUnitException;
import pl.oskarinio.yourturnhomm.domain.model.exception.TransactionSystemAddException;
import pl.oskarinio.yourturnhomm.domain.port.out.UnitRepository;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UnitManagmentUseCaseTest {
    @Mock
    private UnitRepository unitRepository;

    private static final String UNIT_NAME = "testUnit";
    private Unit unit;

    private UnitManagementUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new UnitManagementUseCase(unitRepository);
        unit = new Unit(UNIT_NAME, 2, 2, 2, 2, 2, 2, "");
    }

    @Test
    @DisplayName("Poprawna jednostka, jednostka dodana")
    void addUnit_correctUnit_resultUnitAdded(){
        when(unitRepository.existsById(UNIT_NAME)).thenReturn(false);
        useCase.addUnit(unit);
        verify(unitRepository).save(unit);
    }

    @Test
    @DisplayName("Null jednostka, rzuca NullPointerException")
    void addUnit_unitNull_resultNullPointerException(){
        assertThrows(NullPointerException.class, () -> useCase.addUnit(null));
    }

    @Test
    @DisplayName("Jednostka istniejaca w bazie, rzuca DuplicateUnitException")
    void addUnit_unitExistInDb_resultDuplicateUnitException(){
        when(unitRepository.existsById(unit.getName())).thenReturn(true);
        assertThrows(DuplicateUnitException.class, () -> useCase.addUnit(unit));
    }

    @Test
    @DisplayName("RuntimeException, łapie, rzuca TransactionAddException")
    void addUnit_runtimeException_resultTransactionSystemAddException(){
        when(unitRepository.existsById(unit.getName())).thenReturn(false);
        doThrow(new RuntimeException()).when(unitRepository).save(unit);
        assertThrows(TransactionSystemAddException.class, () -> useCase.addUnit(unit));
    }
}
