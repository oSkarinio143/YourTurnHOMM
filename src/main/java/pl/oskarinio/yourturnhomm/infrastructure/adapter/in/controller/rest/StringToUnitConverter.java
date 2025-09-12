package pl.oskarinio.yourturnhomm.infrastructure.adapter.in.controller.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import pl.oskarinio.yourturnhomm.domain.model.battle.Unit;
import pl.oskarinio.yourturnhomm.domain.port.unit.UnitManagement;

@Slf4j
@Component
class StringToUnitConverter implements Converter<String, Unit> {
    //Uzycie — Sam dostarcza unit do DuelService, podczas ladowania jednostek po wybraniu ich
    private final UnitManagement databaseUseCase;

    public StringToUnitConverter(UnitManagement databaseUseCase) {
        this.databaseUseCase = databaseUseCase;
    }

    @Override
    public Unit convert(String source) {
        log.debug("Konwertowanie zostalo wykonane. zrodlo = {}", source);
        if (source == null || source.isBlank()) return null;
        return databaseUseCase.getSingleUnit(source);
    }
}