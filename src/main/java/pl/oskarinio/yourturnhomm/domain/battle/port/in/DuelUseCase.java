package pl.oskarinio.yourturnhomm.domain.battle.port.in;

import pl.oskarinio.yourturnhomm.infrastructure.adapter.in.DuelForm;
import pl.oskarinio.yourturnhomm.domain.battle.model.Side;

public interface DuelUseCase {
    void closeEmitter(String userUUID);
    void loadUnit(DuelForm duelForm, Side side, String tempUnitName);
    String getUserUUID();
    void loadBattle(DuelForm duelForm);
}
