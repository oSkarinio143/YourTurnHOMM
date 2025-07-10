package oskarinio143.heroes3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import oskarinio143.heroes3.model.entity.Unit;

public interface UnitRepository extends JpaRepository<Unit, String> { }
