package pl.oskarinio.yourturnhomm.app.battle.port.in;

import pl.oskarinio.yourturnhomm.domain.model.battle.RoundInfo;

public interface QueueUseCase {
    void createQueue(RoundInfo roundInfo);
}
