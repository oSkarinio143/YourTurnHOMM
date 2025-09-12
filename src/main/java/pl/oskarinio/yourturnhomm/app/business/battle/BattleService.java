package pl.oskarinio.yourturnhomm.app.implementation.battle;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pl.oskarinio.yourturnhomm.domain.port.battle.Battle;
import pl.oskarinio.yourturnhomm.domain.port.battle.Queue;
import pl.oskarinio.yourturnhomm.domain.usecase.battle.BattleUseCase;
import pl.oskarinio.yourturnhomm.infrastructure.adapter.in.model.DuelForm;

@Service
public class BattleService implements Battle {

    private final BattleUseCase battleUseCase;

    public BattleService(Queue queue,
                         @Value("${battle.rates.attack}") double atkRate,
                         @Value("${battle.rates.defense}") double defRate){
        this.battleUseCase = new BattleUseCase(queue, atkRate, defRate);
    }

    @Async
    @Override
    public void prepareBattle(DuelForm duelForm) {
        battleUseCase.prepareBattle(duelForm);
    }
}
