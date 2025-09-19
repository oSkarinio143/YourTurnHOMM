package pl.oskarinio.yourturnhomm.infrastructure.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import pl.oskarinio.yourturnhomm.domain.model.battle.Unit;
import pl.oskarinio.yourturnhomm.domain.port.unit.UnitManagement;

@Slf4j
@Component
class StringToUnitConverter implements Converter<String, Unit> {
    //Automatycznie konwertuje typ źródłowy na docelowy
    private final UnitManagement unitUseCase;

    public StringToUnitConverter(UnitManagement unitUseCase) {
        this.unitUseCase = unitUseCase;
    }

    @Override
    public Unit convert(String source) {
        if(source != null && !source.isBlank()){
            log.debug("Wykonuje konwertowanie stringa na prawdziwa jednostke. zrodlo = {}", source);
            }
        if (source == null || source.isBlank()) return null;
        return unitUseCase.getSingleUnit(source);
    }
}