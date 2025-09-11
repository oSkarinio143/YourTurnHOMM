package pl.oskarinio.yourturnhomm.app.implementation.database;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.oskarinio.yourturnhomm.domain.port.out.repository.UnitRepositoryPort;
import pl.oskarinio.yourturnhomm.infrastructure.temp.DatabaseSeeder;

@Component
class DatabaseSeederAdapter implements CommandLineRunner {
    private final DatabaseSeeder databaseSeeder;

    public DatabaseSeederAdapter(UnitRepositoryPort unitRepositoryPort) {
        this.databaseSeeder = new DatabaseSeeder(unitRepositoryPort);
    }

    @Override
    public void run(String... args) throws Exception {
        databaseSeeder.seedUnits();
    }
}
