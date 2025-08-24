package pl.oskarinio.yourturnhomm.app.port.in.battle;

import pl.oskarinio.yourturnhomm.domain.model.battle.RoundInfo;

public interface MessageCreatorUseCase {
    void sendRoundMess(RoundInfo roundInfo);
    void sendAttackFasterMess(RoundInfo roundInfo);
    void sendAttackSlowerMess(RoundInfo roundInfo);
    void sendWinnerMess(RoundInfo roundInfo);
}
