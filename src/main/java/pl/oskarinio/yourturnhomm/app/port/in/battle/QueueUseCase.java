package pl.oskarinio.yourturnhomm.app.port.in.battle;

import pl.oskarinio.yourturnhomm.domain.model.battle.RoundInfo;

public interface QueueUseCase {
    void createQueue(RoundInfo roundInfo);
}
