package pl.oskarinio.yourturnhomm.domain.port.in.unit;

import org.springframework.web.multipart.MultipartFile;
import pl.oskarinio.yourturnhomm.domain.model.battle.Unit;

import java.io.IOException;
import java.util.List;

public interface UnitManagerPort {
    void addUnit(Unit unit, MultipartFile image) throws IOException;
    List<Unit> getAllUnits();
    Unit getSingleUnit(String name);
    void removeUnit(String name);
    void modifyUnit(Unit unit);
}
