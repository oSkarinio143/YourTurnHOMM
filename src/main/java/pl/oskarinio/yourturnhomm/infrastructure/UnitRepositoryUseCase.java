package pl.oskarinio.yourturnhomm.domain.usecase.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.oskarinio.yourturnhomm.infrastructure.db.entity.UnitEntity;

public interface UnitRepositoryUseCase extends JpaRepository<UnitEntity, String> { }
