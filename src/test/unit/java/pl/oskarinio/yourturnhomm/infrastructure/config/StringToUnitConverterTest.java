package pl.oskarinio.yourturnhomm.infrastructure.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.oskarinio.yourturnhomm.domain.model.battle.Unit;
import pl.oskarinio.yourturnhomm.domain.port.unit.UnitManagement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StringToUnitConverterTest {
    @Mock
    private UnitManagement unitManagement;

    private static final String UNIT_NAME = "testUnit";

    private StringToUnitConverter stringToUnitConverter;

    @BeforeEach
    void setUp(){
        stringToUnitConverter = new StringToUnitConverter(unitManagement);
    }

    @Test
    @DisplayName("Zrodlo jest null, zwraca null")
    void convert_sourceNull_returnNull(){
        Unit returnedUnit = stringToUnitConverter.convert(null);
        assertThat(returnedUnit).isNull();

        verifyNoInteractions(unitManagement);
    }

    @Test
    @DisplayName("zrodlo jest poprawne, zwraca jednostke")
    void convert_sourceCorrect_returnUnit(){
        Unit testUnit = getUnit();
        when(unitManagement.getSingleUnit(UNIT_NAME)).thenReturn(testUnit);

        Unit convertedUnit = stringToUnitConverter.convert(UNIT_NAME);
        assertThat(convertedUnit.getName()).isEqualTo(testUnit.getName());
    }

    private Unit getUnit(){
        Unit testUnit = new Unit();
        testUnit.setName(UNIT_NAME);
        return testUnit;
    }
}
