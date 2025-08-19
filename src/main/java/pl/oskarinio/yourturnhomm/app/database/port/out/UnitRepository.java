package pl.oskarinio.yourturnhomm.app.database.port.out;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.oskarinio.yourturnhomm.domain.model.entity.Unit;

public interface UnitRepository extends JpaRepository<Unit, String> { }
