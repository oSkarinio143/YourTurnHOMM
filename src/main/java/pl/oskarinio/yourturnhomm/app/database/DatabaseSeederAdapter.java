package pl.oskarinio.yourturnhomm.app.database;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.oskarinio.yourturnhomm.domain.database.service.DatabaseSeeder;
import pl.oskarinio.yourturnhomm.infrastructure.adapter.out.UnitRepository;

@Component
public class DatabaseSeederAdapter implements CommandLineRunner {
    private final DatabaseSeeder databaseSeeder;

    public DatabaseSeederAdapter(UnitRepository unitRepository) {
        this.databaseSeeder = new DatabaseSeeder(unitRepository);
    }

    @Override
    public void run(String... args) throws Exception {
        databaseSeeder.seedUnits();
    }
}
