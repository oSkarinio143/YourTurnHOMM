package pl.oskarinio.yourturnhomm.infrastructure.port.communication;

import pl.oskarinio.yourturnhomm.domain.model.battle.RoundInfo;

public interface MessageCreator {
    void sendRoundMess(RoundInfo roundInfo);
    void sendAttackFasterMess(RoundInfo roundInfo);
    void sendAttackSlowerMess(RoundInfo roundInfo);
    void sendWinnerMess(RoundInfo roundInfo);
}
