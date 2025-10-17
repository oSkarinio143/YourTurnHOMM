package pl.oskarinio.yourturnhomm.infrastructure.usecase.database;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.oskarinio.yourturnhomm.domain.port.out.UnitRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DatabaseSeederUserCaseTest {
    @Mock
    private UnitRepository unitRepository;

    private DatabaseSeederUseCase databaseSeederUseCase;

    @BeforeEach
    void setUp(){
        databaseSeederUseCase = new DatabaseSeederUseCase(unitRepository);
    }

    @Test
    @DisplayName("W bazie 0 jednostek, metoda zapisze wszystkie 14 jednostek poczatkowych")
    void seedUnits_baseHasNoUnits_resultUnitsSaved(){
        when(unitRepository.count()).thenReturn(0L);

        databaseSeederUseCase.seedUnits();

        verify(unitRepository, times(14)).save(any());
    }

    @Test
    @DisplayName("W bazie >0 jednostek, metoda nie modyfikuje bazy, nic nie robi")
    void seedUnits_baseHasSomeUnits_resultNothingChanged(){
        when(unitRepository.count()).thenReturn(1L);

        databaseSeederUseCase.seedUnits();

        verify(unitRepository, never()).save(any());
    }
}
