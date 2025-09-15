package pl.oskarinio.yourturnhomm.app.technical.database;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.oskarinio.yourturnhomm.domain.port.out.UnitRepository;
import pl.oskarinio.yourturnhomm.infrastructure.usecase.database.DatabaseSeederUseCase;

@Component
class DatabaseSeeder implements CommandLineRunner {
    private final DatabaseSeederUseCase databaseSeederUseCase;

    public DatabaseSeeder(UnitRepository unitRepository) {
        this.databaseSeederUseCase = new DatabaseSeederUseCase(unitRepository);
    }

    @Override
    public void run(String... args) throws Exception {
        databaseSeederUseCase.seedUnits();
    }
}
