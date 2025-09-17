package pl.oskarinio.yourturnhomm.infrastructure.adapter.out;

import org.springframework.stereotype.Service;
import pl.oskarinio.yourturnhomm.domain.model.battle.Unit;
import pl.oskarinio.yourturnhomm.domain.port.out.UnitRepository;
import pl.oskarinio.yourturnhomm.infrastructure.db.entity.UnitEntity;
import pl.oskarinio.yourturnhomm.infrastructure.db.mapper.UnitMapper;
import pl.oskarinio.yourturnhomm.infrastructure.usecase.database.UnitRepositoryUseCase;

import java.util.List;

@Service
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
