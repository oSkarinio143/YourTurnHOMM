package pl.oskarinio.yourturnhomm.domain.service.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.oskarinio.yourturnhomm.domain.model.battle.Unit;
import pl.oskarinio.yourturnhomm.domain.model.exception.DuplicateUnitException;
import pl.oskarinio.yourturnhomm.domain.model.exception.TransactionSystemAddException;
import pl.oskarinio.yourturnhomm.domain.port.out.UnitRepository;
import pl.oskarinio.yourturnhomm.domain.usecase.unit.UnitManagementUseCase;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UnitManagmentAddTest {

    @Mock
    private UnitRepository unitRepository;

    private UnitManagementUseCase useCase;



    @BeforeEach
    void setUp() {
        useCase = new UnitManagementUseCase(unitRepository);
    }

    @Test
    void unitExistsInDatabase(){
        Unit unit = new Unit("Pirat", 2, 2, 2, 2, 2, 2, "");
        when(unitRepository.existsById(unit.getName())).thenReturn(true);

        assertThrows(DuplicateUnitException.class, () -> useCase.addUnit(unit));
    }

    @Test
    void throwTransactionSystemAddException(){
        Unit unit = new Unit("Pirat", 2, 2, 2, 2, 2, 2, "");
        doThrow(new RuntimeException()).when(unitRepository).save(unit);
        assertThrows(TransactionSystemAddException.class, () -> useCase.addUnit(unit));
    }
}
