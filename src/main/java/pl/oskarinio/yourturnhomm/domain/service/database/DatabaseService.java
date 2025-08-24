package pl.oskarinio.yourturnhomm.domain.service.database;

import jakarta.transaction.Transactional;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.multipart.MultipartFile;
import pl.oskarinio.yourturnhomm.app.port.out.repository.UnitRepositoryPort;
import pl.oskarinio.yourturnhomm.infrastructure.security.exception.DuplicateUnitException;
import pl.oskarinio.yourturnhomm.infrastructure.security.exception.TransactionSystemAddException;
import pl.oskarinio.yourturnhomm.domain.model.battle.Unit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class DatabaseService {

    private final UnitRepositoryPort unitRepositoryPort;

    public DatabaseService(UnitRepositoryPort unitRepositoryPort) {
        this.unitRepositoryPort = unitRepositoryPort;
    }

    public void addUnit(Unit unit, MultipartFile image) throws IOException {
        if(unitRepositoryPort.existsById(unit.getName())) {
            throw new DuplicateUnitException("Jednostka o tej nazwie jest już w bazie");
        }
        String imagePath = getImagePath(unit.getName(),image);
        unit.setImagePath(imagePath);
        try {
            unitRepositoryPort.save(unit);
        }catch (TransactionSystemException ex){
            throw new TransactionSystemAddException("", ex.getCause());
        }
    }

    public List<Unit> getAllUnits(){
        return unitRepositoryPort.findAll();
    }

    public Unit getSingleUnit(String name){
        return unitRepositoryPort.getReferenceById(name);
    }

    @Transactional
    public void removeUnit(String name){
        Unit unit = unitRepositoryPort.getReferenceById(name);
        unitRepositoryPort.delete(unit);
    }

    @Transactional
    public void modifyUnit(Unit unit){
        Unit oldUnit = unitRepositoryPort.getReferenceById(unit.getName());
        oldUnit.setAttack(unit.getAttack());
        oldUnit.setDefense(unit.getDefense());
        oldUnit.setShots(unit.getShots());
        oldUnit.setMinDamage(unit.getMinDamage());
        oldUnit.setMaxDamage(unit.getMaxDamage());
        oldUnit.setHp(unit.getHp());
        oldUnit.setSpeed(unit.getSpeed());
        oldUnit.setDescription(unit.getDescription());
        unitRepositoryPort.save(oldUnit);
    }

    private String getImagePath(String name, MultipartFile image) throws IOException {
        String fileName = name + ".png";
        Path path = Paths.get("unit-images", fileName);
        Files.createDirectories(path.getParent()); // jeśli folder nie istnieje
        Files.write(path, image.getBytes());
        return  "/" + path.toString();
    }

}
