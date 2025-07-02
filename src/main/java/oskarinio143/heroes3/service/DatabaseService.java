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

    @Transactional
    public void saveUnit(
            String name,
            int attack,
            int defense,
            int shots,
            int minDamage,
            int maxDamage,
            int hp,
            int speed,
            String description,
            String imagePath
    ){
        Unit unit = new Unit(name, attack, defense, shots, minDamage, maxDamage, hp, hp, speed, description, imagePath);
        unitRepository.save(unit);
    }

    @Transactional
    public void addUnit(String name,
                        int attack,
                        int defense,
                        int shots,
                        int minDamage,
                        int maxDamage,
                        int hp,
                        int speed,
                        String description,
                        MultipartFile image) throws IOException {

        if(unitRepository.existsById(name))
            throw new DuplicateUnitException("Jednostka o tej nazwie jest już w bazie");

        String fileName = name + ".png";
        Path path = Paths.get("unit-images", fileName);
        Files.createDirectories(path.getParent()); // jeśli folder nie istnieje
        Files.write(path, image.getBytes());
        String imagePath = "/" + path.toString();

        try {
            saveUnit(name, attack, defense, shots, minDamage, maxDamage, hp, speed, description, imagePath);
        }catch (TransactionSystemException ex){
            throw new TransactionSystemAddException("", ex.getCause());
        }
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

    @Transactional
    public void modifyUnit(String name, int attack, int defense, int shots, int minDamage, int maxDamage, int hp, int speed, Optional<String> descriptionOpt){
        Unit unit = unitRepository.getReferenceById(name);
        unit.setAttack(attack);
        unit.setDefense(defense);
        unit.setShots(shots);
        unit.setMinDamage(minDamage);
        unit.setMaxDamage(maxDamage);
        unit.setHp(hp);
        unit.setHpLeft(hp);
        unit.setSpeed(speed);
        descriptionOpt.ifPresent(unit::setDescription);
        try {
            unitRepository.save(unit);
        }catch (TransactionSystemException ex){
            throw new TransactionSystemModifyException(name, ex.getCause());
        }
    }

}
