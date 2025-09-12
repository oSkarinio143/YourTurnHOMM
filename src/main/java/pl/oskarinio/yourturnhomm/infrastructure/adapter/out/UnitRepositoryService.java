package pl.oskarinio.yourturnhomm.infrastructure.adapter.out.repository;

import org.springframework.stereotype.Component;
import pl.oskarinio.yourturnhomm.domain.model.battle.Unit;
import pl.oskarinio.yourturnhomm.domain.port.repository.UnitRepository;
import pl.oskarinio.yourturnhomm.domain.usecase.repository.UnitRepositoryUseCase;
import pl.oskarinio.yourturnhomm.infrastructure.db.entity.UnitEntity;
import pl.oskarinio.yourturnhomm.infrastructure.db.mapper.UnitMapper;

import java.util.List;

@Component
public class UnitRepositoryService implements UnitRepository {
    private final UnitRepositoryUseCase unitRepositoryUseCase;

    public UnitRepositoryService(UnitRepositoryUseCase unitRepositoryUseCase) {
        this.unitRepositoryUseCase = unitRepositoryUseCase;
    }

    @Override
    public Unit getReferenceById(String name) {
        return UnitMapper.toDomain(unitRepositoryUseCase.getReferenceById(name));
    }

    @Override
    public long count() {
        return unitRepositoryUseCase.count();
    }

    @Override
    public void save(Unit unit) {
        unitRepositoryUseCase.save(UnitMapper.toEntity(unit));
    }

    @Override
    public boolean existsById(String id) {
        return unitRepositoryUseCase.existsById(id);
    }

    @Override
    public List<Unit> findAll() {
        List<UnitEntity> unitEntities = unitRepositoryUseCase.findAll();
        return unitEntities.stream().map(UnitMapper::toDomain).toList();
    }

    @Override
    public void delete(Unit unit) {
        unitRepositoryUseCase.delete(UnitMapper.toEntity(unit));
    }
}
