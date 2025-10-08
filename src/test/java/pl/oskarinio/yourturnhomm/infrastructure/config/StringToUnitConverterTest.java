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
    private final String TEST_UNIT = "testUnit";
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
        String source = TEST_UNIT;
        Unit testUnit = new Unit();
        testUnit.setName(TEST_UNIT);
        when(unitManagement.getSingleUnit(TEST_UNIT)).thenReturn(testUnit);

        Unit convertedUnit = stringToUnitConverter.convert(source);
        assertThat(convertedUnit.getName()).isEqualTo(testUnit.getName());
    }
}
