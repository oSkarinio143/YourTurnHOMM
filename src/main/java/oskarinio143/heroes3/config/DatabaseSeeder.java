package oskarinio143.heroes3.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import oskarinio143.heroes3.model.entity.Unit;
import oskarinio143.heroes3.repository.UnitRepository;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final UnitRepository unitRepository;

    public DatabaseSeeder(UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }

    @Override
    public void run(String... args) throws Exception {
//        seedUnits();
    }

//    public void seedUnits(){
//        if(unitRepository.count() == 0){
//            Unit Reptilioni = new Unit("Reptilioni", 5, 6, 12, 2, 3, 14, 14, 0)
//        }
//
//    }
}
