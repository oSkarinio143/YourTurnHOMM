package pl.oskarinio.yourturnhomm.infrastructure.usecase.database;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.oskarinio.yourturnhomm.infrastructure.db.entity.UnitEntity;

public interface UnitRepositoryUseCase extends JpaRepository<UnitEntity, String> { }
