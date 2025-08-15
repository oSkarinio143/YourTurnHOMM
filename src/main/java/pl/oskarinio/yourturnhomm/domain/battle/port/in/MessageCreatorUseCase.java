package pl.oskarinio.yourturnhomm.domain.battle.port.in;

import pl.oskarinio.yourturnhomm.domain.battle.model.RoundInfo;

public interface MessageCreatorUseCase {
    void sendRoundMess(RoundInfo roundInfo);
    void sendAttackFasterMess(RoundInfo roundInfo);
    void sendAttackSlowerMess(RoundInfo roundInfo);
    void sendWinnerMess(RoundInfo roundInfo);
}
