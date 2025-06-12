package oskarinio143.heroes3.service;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import oskarinio143.heroes3.Unit;
import oskarinio143.heroes3.UnitRepository;
import oskarinio143.heroes3.exception.IncorrectUsername;

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
        throw new IncorrectUsername();
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

        saveUnit(name, attack, defense, shots, minDamage, maxDamage, hp, speed, description, imagePath);
    }

    public void viewUnits(Model model){
        List<Unit> units = getAllUnits();
        model.addAttribute("units", units);
    }

    public void viewSingleUnit(Model model, String name){
        Unit unit = getUnitByName(name);
        model.addAttribute("unit", unit);
    }

    public void removeUnit(String name){
        Unit unit = getUnitByName(name);
        unitRepository.delete(unit);
    }

    public void modifyUnit(String name, int attack, int defense, int shots, int minDamage, int maxDamage, int hp, int speed, Optional<String> descriptionOpt){
        Unit unitOld = getUnitByName(name);
        if(descriptionOpt.isPresent()) {
            Unit unitNew = new Unit(name, attack, defense, shots, minDamage, maxDamage, hp, hp, speed, descriptionOpt.get(), unitOld.getImagePath());
            unitRepository.save(unitNew);
        }
        else{
            Unit unitNew = new Unit(name, attack, defense, shots, minDamage, maxDamage, hp, hp, speed, "", unitOld.getImagePath());
            unitRepository.save(unitNew);
        }
    }

}
