package oskarinio143.heroes3.service;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import oskarinio143.heroes3.model.Unit;
import oskarinio143.heroes3.repository.UnitRepository;
import oskarinio143.heroes3.exception.UnitNotFoundException;

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

    public Unit getUnitByName(String name){
        Optional<Unit> unitOpt = unitRepository.findById(name);
        if(unitOpt.isPresent()){
            Unit unit = unitOpt.get();
            return unit;
        }
        throw new UnitNotFoundException();
    }

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

        String fileName = name + ".png";
        Path path = Paths.get("unit-images", fileName);
        Files.createDirectories(path.getParent()); // jeśli folder nie istnieje
        Files.write(path, image.getBytes());
        String imagePath = "/" + path.toString();
        System.out.println(path);
        saveUnit(name, attack, defense, shots, minDamage, maxDamage, hp, speed, description, imagePath);
    }

    public void viewUnits(Model model){
        List<Unit> units = getAllUnits();
        model.addAttribute("units", units);
    }

    public void viewSingleUnit(Model model, String name){
        Unit unit = getUnitByName(name);
        System.out.println(unit.getImagePath());
        model.addAttribute("unit", unit);
    }

    public void removeUnit(String name){
        Unit unit = getUnitByName(name);
        unitRepository.delete(unit);
    }

    public void modifyUnit(String name, int attack, int defense, int shots, int minDamage, int maxDamage, int hp, int speed, Optional<String> descriptionOpt){
        Unit unit = getUnitByName(name);
        unit.setAttack(attack);
        unit.setDefense(defense);
        unit.setShots(shots);
        unit.setMinDamage(minDamage);
        unit.setMaxDamage(maxDamage);
        unit.setHp(hp);
        unit.setHpLeft(hp);
        unit.setSpeed(speed);
        descriptionOpt.ifPresent(unit::setDescription);
        unitRepository.save(unit);
    }

}
