package pl.oskarinio.yourturnhomm.infrastructure.db.mapper;

import pl.oskarinio.yourturnhomm.domain.model.battle.Unit;
import pl.oskarinio.yourturnhomm.infrastructure.db.entity.UnitEntity;

public class UnitMapper {
    private UnitMapper(){}
    public static UnitEntity toEntity(Unit unit){
        UnitEntity unitEnt = new UnitEntity(unit.getName(),
                unit.getAttack(),
                unit.getDefense(),
                unit.getMinDamage(),
                unit.getMaxDamage(),
                unit.getHp(),
                unit.getSpeed(),
                unit.getDescription(),
                unit.getImagePath());
        if(unit.getShots() != null)
            unitEnt.setShots(unit.getShots());
        return unitEnt;
    }

    public static Unit toDomain(UnitEntity unitEntity){
        Unit unit = new Unit(unitEntity.getName(),
                unitEntity.getAttack(),
                unitEntity.getDefense(),
                unitEntity.getMinDamage(),
                unitEntity.getMaxDamage(),
                unitEntity.getHp(),
                unitEntity.getSpeed(),
                unitEntity.getDescription());
        if(unitEntity.getImagePath() != null)
            unit.setImagePath(unitEntity.getImagePath());
        if(unitEntity.getShots() != null) {
            unit.setShots(unitEntity.getShots());
        }
        return unit;
    }
}
