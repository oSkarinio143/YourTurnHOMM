package pl.oskarinio.yourturnhomm.app.technology.database;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.oskarinio.yourturnhomm.domain.port.out.UnitRepository;
import pl.oskarinio.yourturnhomm.infrastructure.usecase.database.DatabaseSeederUseCase;

@Slf4j
@Component
class DatabaseSeeder implements CommandLineRunner {
    private final DatabaseSeederUseCase databaseSeederUseCase;

    public DatabaseSeeder(UnitRepository unitRepository) {
        this.databaseSeederUseCase = new DatabaseSeederUseCase(unitRepository);
    }

    @Override
    public void run(String... args) {
        log.info("Uruchamiam seeding jednostek do bazy");
        databaseSeederUseCase.seedUnits();
    }
}
