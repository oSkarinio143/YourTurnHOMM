package pl.oskarinio.yourturnhomm.app.business.unit;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.oskarinio.yourturnhomm.domain.model.battle.Unit;
import pl.oskarinio.yourturnhomm.domain.port.out.UnitRepository;
import pl.oskarinio.yourturnhomm.domain.port.unit.UnitManagement;
import pl.oskarinio.yourturnhomm.domain.usecase.unit.UnitManagementUseCase;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class UnitManagementService implements UnitManagement {
    private final UnitManagementUseCase unitManagementUseCase;

    public UnitManagementService(UnitRepository unitRepository) {
        this.unitManagementUseCase = new UnitManagementUseCase(unitRepository);
    }
    @Override
    public void addUnit(Unit unit) throws IOException {
        log.debug("Rozpoczynam proces dodawania jednostki. Nazwa = {}", unit.getName());
        unitManagementUseCase.addUnit(unit);
    }

    @Override
    public List<Unit> getAllUnits() {
        log.trace("Wysylam wszystkie jednostki");
        return unitManagementUseCase.getAllUnits();
    }

    @Override
    public Unit getSingleUnit(String name) {
        log.trace("Pobieranie jednostki. Nazwa = {}", name);
        return unitManagementUseCase.getSingleUnit(name);
    }

    @Transactional
    @Override
    public void removeUnit(String name) {
        log.debug("Rozpoczynam proces usuwania jednostki. Nazwa = {}", name);
        unitManagementUseCase.removeUnit(name);
    }

    @Transactional
    @Override
    public void modifyUnit(Unit unit) {
        log.debug("Rozpoczynam proces modyfikowania jednostki. Nazwa = {}", unit.getName());
        unitManagementUseCase.modifyUnit(unit);
    }
}
