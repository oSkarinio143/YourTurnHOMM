package oskarinio143.yourturnhomm.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.multipart.MultipartFile;
import oskarinio143.yourturnhomm.exception.DuplicateUnitException;
import oskarinio143.yourturnhomm.exception.TransactionSystemAddException;
import oskarinio143.yourturnhomm.model.entity.Unit;
import oskarinio143.yourturnhomm.repository.UnitRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class DatabaseService {

    private final UnitRepository unitRepository;

    public DatabaseService(UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }

    public void addUnit(Unit unit, MultipartFile image) throws IOException {
        if(unitRepository.existsById(unit.getName())) {
            throw new DuplicateUnitException("Jednostka o tej nazwie jest już w bazie");
        }
        String imagePath = getImagePath(unit.getName(),image);
        unit.setImagePath(imagePath);
        try {
            unitRepository.save(unit);
        }catch (TransactionSystemException ex){
            throw new TransactionSystemAddException("", ex.getCause());
        }
    }

    public List<Unit> getAllUnits(){
        return unitRepository.findAll();
    }

    public Unit getSingleUnit(String name){
        return unitRepository.getReferenceById(name);
    }

    @Transactional
    public void removeUnit(String name){
        Unit unit = unitRepository.getReferenceById(name);
        unitRepository.delete(unit);
    }

    @Transactional
    public void modifyUnit(Unit unit){
        Unit oldUnit = unitRepository.getReferenceById(unit.getName());
        oldUnit.setAttack(unit.getAttack());
        oldUnit.setDefense(unit.getDefense());
        oldUnit.setShots(unit.getShots());
        oldUnit.setMinDamage(unit.getMinDamage());
        oldUnit.setMaxDamage(unit.getMaxDamage());
        oldUnit.setHp(unit.getHp());
        oldUnit.setSpeed(unit.getSpeed());
        oldUnit.setDescription(unit.getDescription());
        unitRepository.save(oldUnit);
    }

    public String getImagePath(String name, MultipartFile image) throws IOException {
        String fileName = name + ".png";
        Path path = Paths.get("unit-images", fileName);
        Files.createDirectories(path.getParent()); // jeśli folder nie istnieje
        Files.write(path, image.getBytes());
        return  "/" + path.toString();
    }

}
