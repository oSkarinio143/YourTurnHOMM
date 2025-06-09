package oskarinio143.heroes3.service;

import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import oskarinio143.heroes3.Unit;
import oskarinio143.heroes3.UnitRepository;

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
            String imageFile
    ){
        Unit unit = new Unit(name, attack, defense, shots, minDamage, maxDamage, hp, hp, speed, description, imageFile);
        unitRepository.save(unit);
    }
}
