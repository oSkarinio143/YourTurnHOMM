package oskarinio143.heroes3.service;

import org.springframework.stereotype.Service;
import oskarinio143.heroes3.Unit;
import oskarinio143.heroes3.UnitRepository;

import java.util.List;

@Service
public class DatabaseService {

    private final UnitRepository unitRepository;

    public DatabaseService(UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
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

    public List<Unit> getAllUnits(){
        List<Unit> units = unitRepository.findAll();
        return units;
    }

    public void addUnit(){}

    public void viewUnits(){

    }
}
