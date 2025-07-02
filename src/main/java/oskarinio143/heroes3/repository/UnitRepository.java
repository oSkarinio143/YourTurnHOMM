package oskarinio143.heroes3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import oskarinio143.heroes3.model.entity.Unit;

public interface UnitRepository extends JpaRepository<Unit, String> {
}
