package pl.oskarinio.yourturnhomm.domain.battle.port.in;

import pl.oskarinio.yourturnhomm.infrastructure.adapter.in.DuelForm;

public interface BattleUseCase {
    void prepareBattle(DuelForm duelForm);
}
