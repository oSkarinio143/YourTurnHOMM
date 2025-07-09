package oskarinio143.heroes3.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import oskarinio143.heroes3.exception.DuplicateUnitException;
import oskarinio143.heroes3.exception.TransactionSystemAddException;
import oskarinio143.heroes3.exception.TransactionSystemModifyException;
import oskarinio143.heroes3.model.entity.Unit;
import oskarinio143.heroes3.repository.UnitRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class DatabaseService {

    private final UnitRepository unitRepository;

    public DatabaseService(UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }

    public List<Unit> getAllUnits(){
        List<Unit> units = unitRepository.findAll();
        return units;
    }

    public void addUnit(Unit unit, MultipartFile image) throws IOException {
        if(unitRepository.existsById(unit.getName())) {
            throw new DuplicateUnitException("Jednostka o tej nazwie jest już w bazie");
        }
        String imagePath = getImagePath(unit.getName(),image);
        unit.setImagePath(imagePath);
        unit.setHpLeft(unit.getHp());
        try {
            unitRepository.save(unit);
        }catch (TransactionSystemException ex){
            throw new TransactionSystemAddException("", ex.getCause());
        }
    }

    public String getImagePath(String name, MultipartFile image) throws IOException {
        String fileName = name + ".png";
        Path path = Paths.get("unit-images", fileName);
        Files.createDirectories(path.getParent()); // jeśli folder nie istnieje
        Files.write(path, image.getBytes());
        return  "/" + path.toString();
    }

    public void viewUnits(Model model){
        List<Unit> units = getAllUnits();
        model.addAttribute("units", units);
    }

    public void viewSingleUnit(Model model, String name){
        Unit unit = unitRepository.getReferenceById(name);
        model.addAttribute("unit", unit);
    }

    @Transactional
    public void removeUnit(String name){
        Unit unit = unitRepository.getReferenceById(name);
        unitRepository.delete(unit);
    }

    public void modifyUnit(Unit unit) throws IOException {
        Unit oldUnit = unitRepository.getReferenceById(unit.getName());
        unit.setImagePath(oldUnit.getImagePath());
        try{
            unitRepository.save(unit);
        }catch (TransactionSystemException ex){
            throw new TransactionSystemModifyException(unit.getName(), ex.getCause());
        }
    }

}
