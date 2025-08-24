package pl.oskarinio.yourturnhomm.app.implementation.database;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.oskarinio.yourturnhomm.app.battle.port.out.UnitRepositoryPort;
import pl.oskarinio.yourturnhomm.domain.service.database.DatabaseSeeder;

@Component
public class DatabaseSeederAdapter implements CommandLineRunner {
    private final DatabaseSeeder databaseSeeder;

    public DatabaseSeederAdapter(UnitRepositoryPort unitRepositoryPort) {
        this.databaseSeeder = new DatabaseSeeder(unitRepositoryPort);
    }

    @Override
    public void run(String... args) throws Exception {
        databaseSeeder.seedUnits();
    }
}
