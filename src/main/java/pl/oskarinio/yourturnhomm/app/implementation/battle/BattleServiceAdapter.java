package pl.oskarinio.yourturnhomm.app.implementation.battle;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pl.oskarinio.yourturnhomm.domain.port.in.battle.BattleUseCase;
import pl.oskarinio.yourturnhomm.domain.port.in.battle.QueueUseCase;
import pl.oskarinio.yourturnhomm.domain.service.battle.BattleService;
import pl.oskarinio.yourturnhomm.infrastructure.adapter.in.model.DuelForm;

@Service
public class BattleServiceAdapter implements BattleUseCase {

    private final BattleService battleService;

    public BattleServiceAdapter(QueueUseCase queueUseCase,
                                @Value("${battle.rates.attack}") double atkRate,
                                @Value("${battle.rates.defense}") double defRate){
        this.battleService = new BattleService(queueUseCase, atkRate, defRate);
    }

    @Async
    @Override
    public void prepareBattle(DuelForm duelForm) {
        battleService.prepareBattle(duelForm);
    }
}
