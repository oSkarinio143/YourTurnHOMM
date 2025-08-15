package pl.oskarinio.yourturnhomm.app.database;

import org.springframework.web.multipart.MultipartFile;
import pl.oskarinio.yourturnhomm.domain.database.service.DatabaseUseCase;
import pl.oskarinio.yourturnhomm.domain.model.entity.Unit;
import pl.oskarinio.yourturnhomm.infrastructure.adapter.out.UnitRepository;

import java.io.IOException;
import java.util.List;

public class DatabaseServiceAdapter implements pl.oskarinio.yourturnhomm.domain.database.port.in.DatabaseUseCase {
    private final DatabaseUseCase databaseService;

    public DatabaseServiceAdapter(UnitRepository unitRepository) {
        this.databaseService = new DatabaseUseCase(unitRepository);
    }
    @Override
    public void addUnit(Unit unit, MultipartFile image) throws IOException {
        databaseService.addUnit(unit, image);
    }

    @Override
    public List<Unit> getAllUnits() {
        return databaseService.getAllUnits();
    }

    @Override
    public Unit getSingleUnit(String name) {
        return databaseService.getSingleUnit(name);
    }

    @Override
    public void removeUnit(String name) {
        databaseService.removeUnit(name);
    }

    @Override
    public void modifyUnit(Unit unit) {
        databaseService.modifyUnit(unit);
    }
}
