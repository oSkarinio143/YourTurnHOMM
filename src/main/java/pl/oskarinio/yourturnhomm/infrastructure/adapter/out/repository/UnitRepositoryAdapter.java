package pl.oskarinio.yourturnhomm.infrastructure.adapter.out.repository;

import org.springframework.stereotype.Component;
import pl.oskarinio.yourturnhomm.app.port.out.repository.UnitRepositoryPort;
import pl.oskarinio.yourturnhomm.infrastructure.db.entity.UnitEntity;
import pl.oskarinio.yourturnhomm.infrastructure.db.mapper.UnitMapper;
import pl.oskarinio.yourturnhomm.infrastructure.db.repository.UnitRepository;
import pl.oskarinio.yourturnhomm.domain.model.battle.Unit;

import java.util.List;

@Component
public class UnitRepositoryAdapter implements UnitRepositoryPort {
    private final UnitRepository unitRepository;

    public UnitRepositoryAdapter(UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }

    @Override
    public Unit getReferenceById(String name) {
        return UnitMapper.toDomain(unitRepository.getReferenceById(name));
    }

    @Override
    public long count() {
        return unitRepository.count();
    }

    @Override
    public void save(Unit unit) {
        System.out.println(unit.getShots());
        unitRepository.save(UnitMapper.toEntity(unit));
    }

    @Override
    public boolean existsById(String id) {
        return unitRepository.existsById(id);
    }

    @Override
    public List<Unit> findAll() {
        List<UnitEntity> unitEntities = unitRepository.findAll();
//        unitEntities.forEach(v ->
//                System.out.println(v.getShots()));
        return unitEntities.stream().map(UnitMapper::toDomain).toList();
    }

    @Override
    public void delete(Unit unit) {
        unitRepository.delete(UnitMapper.toEntity(unit));
    }
}
