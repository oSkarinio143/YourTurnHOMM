package pl.oskarinio.yourturnhomm.infrastructure.adapter.out;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.oskarinio.yourturnhomm.domain.model.battle.Unit;
import pl.oskarinio.yourturnhomm.domain.port.out.UnitRepository;
import pl.oskarinio.yourturnhomm.infrastructure.db.entity.UnitEntity;
import pl.oskarinio.yourturnhomm.infrastructure.db.mapper.UnitMapper;
import pl.oskarinio.yourturnhomm.infrastructure.usecase.database.UnitRepositoryUseCase;

import java.util.List;

@Slf4j
@Service
public class UnitRepositoryService implements UnitRepository {
    private final UnitRepositoryUseCase unitRepositoryUseCase;

    public UnitRepositoryService(UnitRepositoryUseCase unitRepositoryUseCase) {
        this.unitRepositoryUseCase = unitRepositoryUseCase;
    }

    @Override
    public Unit getReferenceById(String name) {
        log.debug("Pobieram jednostke z bazy. Nazwa = {}", name);
        return UnitMapper.toDomain(unitRepositoryUseCase.getReferenceById(name));
    }

    @Override
    public long count() {
        long amountUnits = unitRepositoryUseCase.count();
        log.debug("Zliczam jednostki w bazie. Wynik = {}", amountUnits);
        return amountUnits;
    }

    @Override
    public void save(Unit unit) {
        log.debug("Zapisuje / Aktualizuje jednostke w bazie. Nazwa = {}", unit.getName());
        unitRepositoryUseCase.save(UnitMapper.toEntity(unit));
    }

    @Override
    public boolean existsById(String id) {
        boolean isExist = unitRepositoryUseCase.existsById(id);
        log.debug("Sprawdzam czy jednostka o tej nazwie istnieje w bazie. Nazwa = {}, wynik = {}", id, isExist);
        return isExist;
    }

    @Override
    public List<Unit> findAll() {
        log.trace("Zwracam wszystkie jednostki z bazy");
        List<UnitEntity> unitEntities = unitRepositoryUseCase.findAll();
        return unitEntities.stream().map(UnitMapper::toDomain).toList();
    }

    @Override
    public void delete(Unit unit) {
        log.debug("Usuwam jednostke z bazy. Nazwa = {}", unit.getName());
        unitRepositoryUseCase.delete(UnitMapper.toEntity(unit));
    }
}
