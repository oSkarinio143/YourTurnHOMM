package pl.oskarinio.yourturnhomm.app.business.battle;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.oskarinio.yourturnhomm.domain.model.battle.Side;
import pl.oskarinio.yourturnhomm.domain.model.form.DuelForm;
import pl.oskarinio.yourturnhomm.domain.port.battle.Battle;
import pl.oskarinio.yourturnhomm.domain.port.battle.Duel;
import pl.oskarinio.yourturnhomm.domain.port.out.UnitRepository;
import pl.oskarinio.yourturnhomm.domain.usecase.battle.DuelUseCase;

@Slf4j
@Service
public class DuelService implements Duel {

    private final DuelUseCase duelUseCase;

    public DuelService(UnitRepository unitRepository,
                       Battle battle){
        this.duelUseCase = new DuelUseCase(unitRepository, battle);
    }

    @Override
    public void loadUnit(DuelForm duelForm, Side side, String tempUnitName) {
        log.debug("Ładuję jednostkę do pojedynku. Strona = {}, Nazwa = {}", side, tempUnitName);
        duelUseCase.loadUnit(duelForm, side, tempUnitName);
    }

    @Override
    public void loadBattle(DuelForm duelForm) {
        log.debug("Rozpoczynam ładowanie bitwy.");
        duelUseCase.loadBattle(duelForm);
    }
}
