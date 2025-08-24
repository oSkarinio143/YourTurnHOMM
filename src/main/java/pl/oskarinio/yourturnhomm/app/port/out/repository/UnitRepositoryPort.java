package pl.oskarinio.yourturnhomm.app.port.out;

import pl.oskarinio.yourturnhomm.domain.model.battle.Unit;

import java.util.List;

public interface UnitRepositoryPort {
    Unit getReferenceById(String id);
    long count();
    void save(Unit unit);
    boolean existsById(String id);
    List<Unit> findAll();
    void delete(Unit unit);
}
