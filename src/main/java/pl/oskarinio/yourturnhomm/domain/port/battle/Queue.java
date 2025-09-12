package pl.oskarinio.yourturnhomm.domain.port.in.battle;

import pl.oskarinio.yourturnhomm.domain.model.battle.RoundInfo;

public interface QueueUseCase {
    void createQueue(RoundInfo roundInfo);
}
