package pl.oskarinio.yourturnhomm.infrastructure.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.oskarinio.yourturnhomm.infrastructure.db.entity.UnitEntity;

public interface UnitRepository extends JpaRepository<UnitEntity, String> { }
