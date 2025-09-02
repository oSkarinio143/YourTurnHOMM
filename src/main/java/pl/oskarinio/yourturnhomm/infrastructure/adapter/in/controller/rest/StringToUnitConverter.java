package pl.oskarinio.yourturnhomm.infrastructure.adapter.in.controller.rest;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import pl.oskarinio.yourturnhomm.app.port.in.database.DatabaseUseCase;
import pl.oskarinio.yourturnhomm.domain.model.battle.Unit;

@Component
class StringToUnitConverter implements Converter<String, Unit> {

    private final DatabaseUseCase databaseUseCase;

    public StringToUnitConverter(DatabaseUseCase databaseUseCase) {
        this.databaseUseCase = databaseUseCase;
    }

    @Override
    public Unit convert(String source) {
        if (source == null || source.isBlank()) return null;
        return databaseUseCase.getSingleUnit(source);
    }
}