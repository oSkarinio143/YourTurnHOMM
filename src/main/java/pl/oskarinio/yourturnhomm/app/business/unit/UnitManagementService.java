package pl.oskarinio.yourturnhomm.app.business.unit;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.oskarinio.yourturnhomm.domain.model.battle.Unit;
import pl.oskarinio.yourturnhomm.domain.port.repository.UnitRepository;
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
    public void addUnit(Unit unit, MultipartFile image) throws IOException {
        unitManagementUseCase.addUnit(unit, image);
    }

    @Override
    public List<Unit> getAllUnits() {
        return unitManagementUseCase.getAllUnits();
    }

    @Override
    public Unit getSingleUnit(String name) {
        return unitManagementUseCase.getSingleUnit(name);
    }

    @Override
    public void removeUnit(String name) {
        unitManagementUseCase.removeUnit(name);
    }

    @Override
    public void modifyUnit(Unit unit) {
        unitManagementUseCase.modifyUnit(unit);
    }
}
