package pl.oskarinio.yourturnhomm.domain.usecase.unit;

import pl.oskarinio.yourturnhomm.domain.model.battle.Unit;
import pl.oskarinio.yourturnhomm.domain.model.exception.DuplicateUnitException;
import pl.oskarinio.yourturnhomm.domain.model.exception.TransactionSystemAddException;
import pl.oskarinio.yourturnhomm.domain.port.out.UnitRepository;

import java.util.List;

public class UnitManagementUseCase {

    private final UnitRepository unitRepository;


    public UnitManagementUseCase(UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }

    public void addUnit(Unit unit) {
        if (unitRepository.existsById(unit.getName())) {
            throw new DuplicateUnitException("Jednostka o tej nazwie jest już w bazie");
        }
        try {
            unitRepository.save(unit);
        } catch (RuntimeException ex) {
            throw new TransactionSystemAddException("", ex.getCause());
        }
    }

    public List<Unit> getAllUnits() {
        return unitRepository.findAll();
    }

    public Unit getSingleUnit(String name) {
        return unitRepository.getReferenceById(name);
    }

    public void removeUnit(String name) {
        Unit unit = unitRepository.getReferenceById(name);
        unitRepository.delete(unit);
    }

    public void modifyUnit(Unit unit) {
        unitRepository.save(unit);
    }
}
