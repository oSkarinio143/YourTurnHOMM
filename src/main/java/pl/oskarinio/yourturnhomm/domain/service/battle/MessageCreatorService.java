package pl.oskarinio.yourturnhomm.domain.service.battle;

import pl.oskarinio.yourturnhomm.app.battle.port.in.CommunicationUseCase;
import pl.oskarinio.yourturnhomm.domain.model.battle.RoundInfo;

public class MessageCreatorService {
    private final CommunicationUseCase communicationUseCase;

    public MessageCreatorService(CommunicationUseCase communicationUseCase) {
        this.communicationUseCase = communicationUseCase;
    }

    public void sendRoundMess(RoundInfo roundInfo) {
        communicationUseCase.sendMessage(roundInfo.getUserUUID(), "ROUND:Runda " + roundInfo.getRoundCounter());
    }

    public void sendAttackFasterMess(RoundInfo roundInfo) {
        if (roundInfo.getSlowerDeathUnits() > 0)
            createMessageAttackFasterKill(roundInfo);    
        else
            createMessageAttackFasterBasic(roundInfo);
    }

    public void sendAttackSlowerMess(RoundInfo roundInfo) {
        if (roundInfo.getFasterDeathUnits() > 0)
            createMessageAttackSlowerKill(roundInfo);
        else
            createMessageAttackSlowerBasic(roundInfo);
    }

    public void sendWinnerMess(RoundInfo roundInfo) {
        communicationUseCase.sendMessage(roundInfo.getUserUUID(), "VICTORY:" + roundInfo.getLoserUnit().getName() + " gina. "
                + roundInfo.getWinnerUnit().getName() + " wygrywaja pojedynek");
        communicationUseCase.closeConnection(roundInfo.getUserUUID());
    }

    public void createMessageAttackFasterKill(RoundInfo roundInfo){
        communicationUseCase.sendMessage(roundInfo.getUserUUID(), "ATTACKF:Jednostka " + roundInfo.getFasterLastAttackUnits() + "x"
                + roundInfo.getFasterUnit().getName() + " atakuje pierwsza, zadajac " + roundInfo.getFasterDmg()
                + ".<br>Pokonuje " + roundInfo.getSlowerDeathUnits() + "x" + roundInfo.getSlowerUnit().getName());
    }

    public void createMessageAttackFasterBasic(RoundInfo roundInfo){
        communicationUseCase.sendMessage(roundInfo.getUserUUID(), "ATTACKF:Jednostka " + roundInfo.getFasterLastAttackUnits() + "x"
                + roundInfo.getFasterUnit().getName() + " atakuje pierwsza, zadajac " + roundInfo.getFasterDmg() + ".");
    }

    public void createMessageAttackSlowerKill(RoundInfo roundInfo){
        communicationUseCase.sendMessage(roundInfo.getUserUUID(), "ATTACKS:Jednostka " + roundInfo.getSlowerLiveUnits() + "x"
                + roundInfo.getSlowerUnit().getName() + " atakuje druga, zadajac " + roundInfo.getSlowerDmg()
                + ".<br>Pokonuje " + roundInfo.getFasterDeathUnits() + "x" + roundInfo.getFasterUnit().getName());
    }

    public void createMessageAttackSlowerBasic(RoundInfo roundInfo){
        communicationUseCase.sendMessage(roundInfo.getUserUUID(), "ATTACKS:Jednostka " + roundInfo.getSlowerLiveUnits() + "x"
                + roundInfo.getSlowerUnit().getName() + " atakuje druga, zadajac " + roundInfo.getSlowerDmg() + ".");
    }
}
