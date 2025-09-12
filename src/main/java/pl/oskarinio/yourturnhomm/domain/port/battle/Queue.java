package pl.oskarinio.yourturnhomm.domain.port.battle;

import pl.oskarinio.yourturnhomm.domain.model.battle.RoundInfo;

public interface Queue {
    void createQueue(RoundInfo roundInfo);
}
