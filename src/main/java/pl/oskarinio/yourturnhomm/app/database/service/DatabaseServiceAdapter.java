package pl.oskarinio.yourturnhomm.app.database.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.oskarinio.yourturnhomm.domain.service.database.DatabaseService;
import pl.oskarinio.yourturnhomm.domain.model.entity.Unit;
import pl.oskarinio.yourturnhomm.app.database.port.out.UnitRepository;

import java.io.IOException;
import java.util.List;

@Service
public class DatabaseServiceAdapter implements pl.oskarinio.yourturnhomm.app.database.port.in.DatabaseUseCase {
    private final DatabaseService databaseService;

    public DatabaseServiceAdapter(UnitRepository unitRepository) {
        this.databaseService = new DatabaseService(unitRepository);
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
