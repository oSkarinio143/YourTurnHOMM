package pl.oskarinio.yourturnhomm.app.implementation.battle;

import org.springframework.stereotype.Service;
import pl.oskarinio.yourturnhomm.domain.model.battle.Side;
import pl.oskarinio.yourturnhomm.domain.port.in.battle.BattleUseCase;
import pl.oskarinio.yourturnhomm.domain.port.in.battle.DuelUseCase;
import pl.oskarinio.yourturnhomm.domain.port.out.repository.UnitRepositoryPort;
import pl.oskarinio.yourturnhomm.domain.service.battle.DuelService;
import pl.oskarinio.yourturnhomm.infrastructure.adapter.in.model.DuelForm;
import pl.oskarinio.yourturnhomm.infrastructure.temp.CommunicationUseCase;

@Service
public class DuelServiceAdapter implements DuelUseCase {

    private final DuelService duelService;

    public DuelServiceAdapter(UnitRepositoryPort unitRepositoryPort,
                              BattleUseCase battleUseCase,
                              CommunicationUseCase communicationUseCase){
        this.duelService = new DuelService(unitRepositoryPort, battleUseCase, communicationUseCase);
    }

    @Override
    public void loadUnit(DuelForm duelForm, Side side, String tempUnitName) {
        duelService.loadUnit(duelForm, side, tempUnitName);
    }

    @Override
    public void loadBattle(DuelForm duelForm) {
        duelService.loadBattle(duelForm);
    }
}
