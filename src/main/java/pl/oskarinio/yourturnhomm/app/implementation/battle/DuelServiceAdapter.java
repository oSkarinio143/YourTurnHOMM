package pl.oskarinio.yourturnhomm.app.implementation.battle;

import org.springframework.stereotype.Service;
import pl.oskarinio.yourturnhomm.app.port.in.battle.BattleUseCase;
import pl.oskarinio.yourturnhomm.app.port.in.battle.CommunicationUseCase;
import pl.oskarinio.yourturnhomm.app.port.out.repository.UnitRepositoryPort;
import pl.oskarinio.yourturnhomm.infrastructure.adapter.in.model.DuelForm;
import pl.oskarinio.yourturnhomm.domain.model.battle.Side;
import pl.oskarinio.yourturnhomm.domain.service.battle.DuelService;
import pl.oskarinio.yourturnhomm.app.port.in.battle.DuelUseCase;

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

    @Override
    public String getUserUUID() {
        return duelService.getUserUUID();
    }

    @Override
    public void closeEmitter(String userUUID) {
        duelService.closeEmitter(userUUID);
    }
}
