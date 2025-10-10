package pl.oskarinio.yourturnhomm.infrastructure.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import pl.oskarinio.yourturnhomm.domain.model.battle.Unit;
import pl.oskarinio.yourturnhomm.domain.port.unit.UnitManagement;

//Automatycznie konwertuje typ źródłowy na docelowy
@Slf4j
@Component
class StringToUnitConverter implements Converter<String, Unit> {
    private final UnitManagement unitManagement;

    public StringToUnitConverter(UnitManagement unitManagement) {
        this.unitManagement = unitManagement;
    }

    @Override
    public Unit convert(String source) {
        if(source != null && !source.isBlank())
            log.debug("Wykonuje konwertowanie stringa na prawdziwa jednostke. zrodlo = {}", source);
        if (source == null || source.isBlank())
            return null;
        return unitManagement.getSingleUnit(source);
    }
}