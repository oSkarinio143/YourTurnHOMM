package pl.oskarinio.yourturnhomm.domain.port.unit;

import pl.oskarinio.yourturnhomm.domain.model.battle.Unit;

import java.util.List;

public interface UnitManagement {
    void addUnit(Unit unit);
    List<Unit> getAllUnits();
    Unit getSingleUnit(String name);
    void removeUnit(String name);
    void modifyUnit(Unit unit);
}
