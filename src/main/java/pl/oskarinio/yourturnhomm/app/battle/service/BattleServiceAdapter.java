package pl.oskarinio.yourturnhomm.app.battle.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import pl.oskarinio.yourturnhomm.domain.service.battle.BattleService;
import pl.oskarinio.yourturnhomm.app.battle.port.in.BattleUseCase;
import pl.oskarinio.yourturnhomm.app.battle.port.in.QueueUseCase;
import pl.oskarinio.yourturnhomm.infrastructure.adapter.in.model.form.DuelForm;

@Service
@EnableAsync
public class BattleServiceAdapter implements BattleUseCase {

    private final BattleService battleService;

    public BattleServiceAdapter(QueueUseCase queueUseCase,
                                @Value("${battle.rates.attack}") double atkRate,
                                @Value("${battle.rates.defense}") double defRate
    ){
        this.battleService = new BattleService(queueUseCase, atkRate, defRate);
    }

    @Async
    @Override
    public void prepareBattle(DuelForm duelForm) {
        battleService.prepareBattle(duelForm);
    }
}
