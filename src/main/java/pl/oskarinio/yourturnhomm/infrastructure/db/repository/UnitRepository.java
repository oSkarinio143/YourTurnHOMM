package pl.oskarinio.yourturnhomm.infrastructure.adapter.out.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.oskarinio.yourturnhomm.infrastructure.adapter.in.model.entity.UnitEntity;

public interface UnitRepository extends JpaRepository<UnitEntity, String> { }
