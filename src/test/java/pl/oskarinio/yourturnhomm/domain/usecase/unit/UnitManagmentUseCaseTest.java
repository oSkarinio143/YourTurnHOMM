package pl.oskarinio.yourturnhomm.domain.usecase.unit;

import org.junit.jupiter.api.BeforeEach;
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

    private String TEST_UNIT_NAME = "unit";

    private UnitManagementUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new UnitManagementUseCase(unitRepository);
    }

    @Test
    void addUnit_correctValues(){
        Unit unit = new Unit(TEST_UNIT_NAME, 2, 2, 2, 2, 2, 2, "");

        when(unitRepository.existsById(TEST_UNIT_NAME)).thenReturn(false);
        useCase.addUnit(unit);

        verify(unitRepository).save(unit);
    }

    @Test
    void addUnit_unitExist_resultDuplicateUnitException(){
        Unit unit = new Unit(TEST_UNIT_NAME, 2, 2, 2, 2, 2, 2, "");

        when(unitRepository.existsById(unit.getName())).thenReturn(true);

        assertThrows(DuplicateUnitException.class, () -> useCase.addUnit(unit));
    }

    @Test
    void addUnit_runtimeException_resultTransactionSystemAddException(){
        Unit unit = new Unit(TEST_UNIT_NAME, 2, 2, 2, 2, 2, 2, "");

        when(unitRepository.existsById(unit.getName())).thenReturn(false);
        doThrow(new RuntimeException()).when(unitRepository).save(unit);

        assertThrows(TransactionSystemAddException.class, () -> useCase.addUnit(unit));
        verify(unitRepository).existsById(unit.getName());
        verify(unitRepository).save(unit);
    }
}
