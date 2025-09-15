package pl.oskarinio.yourturnhomm.app.business.unit;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pl.oskarinio.yourturnhomm.domain.model.battle.Unit;
import pl.oskarinio.yourturnhomm.domain.port.out.UnitRepository;
import pl.oskarinio.yourturnhomm.domain.port.unit.UnitManagement;
import pl.oskarinio.yourturnhomm.domain.usecase.unit.UnitManagementUseCase;

import java.io.IOException;
import java.util.List;

@Service
public class UnitManagementService implements UnitManagement {
    private final UnitManagementUseCase unitManagementUseCase;

    public UnitManagementService(UnitRepository unitRepository) {
        this.unitManagementUseCase = new UnitManagementUseCase(unitRepository);
    }
    @Override
    public void addUnit(Unit unit, String imagePath) throws IOException {
        unitManagementUseCase.addUnit(unit, imagePath);
    }

    @Override
    public List<Unit> getAllUnits() {
        return unitManagementUseCase.getAllUnits();
    }

    @Override
    public Unit getSingleUnit(String name) {
        return unitManagementUseCase.getSingleUnit(name);
    }

    @Transactional
    @Override
    public void removeUnit(String name) {
        unitManagementUseCase.removeUnit(name);
    }

    @Transactional
    @Override
    public void modifyUnit(Unit unit) {
        unitManagementUseCase.modifyUnit(unit);
    }
}
