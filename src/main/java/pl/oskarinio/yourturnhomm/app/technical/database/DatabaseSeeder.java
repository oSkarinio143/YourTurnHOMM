package pl.oskarinio.yourturnhomm.app.technical;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.oskarinio.yourturnhomm.domain.port.repository.UnitRepositoryPort;
import pl.oskarinio.yourturnhomm.infrastructure.usecase.DatabaseSeederUseCase;

@Component
class DatabaseSeeder implements CommandLineRunner {
    private final DatabaseSeederUseCase databaseSeederUseCase;

    public DatabaseSeeder(UnitRepositoryPort unitRepositoryPort) {
        this.databaseSeederUseCase = new DatabaseSeederUseCase(unitRepositoryPort);
    }

    @Override
    public void run(String... args) throws Exception {
        databaseSeederUseCase.seedUnits();
    }
}
