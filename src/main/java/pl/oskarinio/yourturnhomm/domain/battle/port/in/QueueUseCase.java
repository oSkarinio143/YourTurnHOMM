package pl.oskarinio.yourturnhomm.domain.battle.port.in;

import pl.oskarinio.yourturnhomm.domain.battle.model.RoundInfo;

public interface QueueUseCase {
    void createQueue(RoundInfo roundInfo);
}
