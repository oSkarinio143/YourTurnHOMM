package pl.oskarinio.yourturnhomm.domain.usecase.battle;

import pl.oskarinio.yourturnhomm.domain.model.battle.RoundInfo;
import pl.oskarinio.yourturnhomm.domain.port.out.MessageSender;

public class  MessageCreatorUseCase {
    private final MessageSender messageSender;

    public MessageCreatorUseCase(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    public void sendRoundMess(RoundInfo roundInfo) {
        messageSender.sendMessage(roundInfo.getUserUUID(), "ROUND:Runda " + roundInfo.getRoundCounter());
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
        messageSender.sendMessage(roundInfo.getUserUUID(), "VICTORY:" + roundInfo.getLoserUnit().getName() + " gina. "
                + roundInfo.getWinnerUnit().getName() + " wygrywaja pojedynek");
        messageSender.closeConnection(roundInfo.getUserUUID());
    }

    private void createMessageAttackFasterKill(RoundInfo roundInfo){
        messageSender.sendMessage(roundInfo.getUserUUID(), "ATTACKF:Jednostka " + roundInfo.getFasterLastAttackUnits() + "x"
                + roundInfo.getFasterUnit().getName() + " atakuje pierwsza, zadajac " + roundInfo.getFasterDmg()
                + ".<br>Pokonuje " + roundInfo.getSlowerDeathUnits() + "x" + roundInfo.getSlowerUnit().getName());
    }

    private void createMessageAttackFasterBasic(RoundInfo roundInfo){
        messageSender.sendMessage(roundInfo.getUserUUID(), "ATTACKF:Jednostka " + roundInfo.getFasterLastAttackUnits() + "x"
                + roundInfo.getFasterUnit().getName() + " atakuje pierwsza, zadajac " + roundInfo.getFasterDmg() + ".");
    }

    private void createMessageAttackSlowerKill(RoundInfo roundInfo){
        messageSender.sendMessage(roundInfo.getUserUUID(), "ATTACKS:Jednostka " + roundInfo.getSlowerLiveUnits() + "x"
                + roundInfo.getSlowerUnit().getName() + " atakuje druga, zadajac " + roundInfo.getSlowerDmg()
                + ".<br>Pokonuje " + roundInfo.getFasterDeathUnits() + "x" + roundInfo.getFasterUnit().getName());
    }

    private void createMessageAttackSlowerBasic(RoundInfo roundInfo){
        messageSender.sendMessage(roundInfo.getUserUUID(), "ATTACKS:Jednostka " + roundInfo.getSlowerLiveUnits() + "x"
                + roundInfo.getSlowerUnit().getName() + " atakuje druga, zadajac " + roundInfo.getSlowerDmg() + ".");
    }
}
