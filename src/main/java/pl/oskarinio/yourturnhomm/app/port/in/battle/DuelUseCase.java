package pl.oskarinio.yourturnhomm.app.port.in.battle;

import pl.oskarinio.yourturnhomm.infrastructure.adapter.in.model.DuelForm;
import pl.oskarinio.yourturnhomm.domain.model.battle.Side;

public interface DuelUseCase {
    void closeEmitter(String userUUID);
    void loadUnit(DuelForm duelForm, Side side, String tempUnitName);
    String getUserUUID();
    void loadBattle(DuelForm duelForm);
}
