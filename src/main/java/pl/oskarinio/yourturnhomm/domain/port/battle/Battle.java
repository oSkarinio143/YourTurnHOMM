package pl.oskarinio.yourturnhomm.domain.port.battle;

import pl.oskarinio.yourturnhomm.domain.model.form.DuelForm;

public interface Battle {
    void prepareBattle(DuelForm duelForm);
}
