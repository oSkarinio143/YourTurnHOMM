package oskarinio143.heroes3.service;

import org.springframework.stereotype.Service;
import oskarinio143.heroes3.model.AttackInfo;
import oskarinio143.heroes3.model.RoundInfo;

@Service
public class MessageCreatorService {
    private final CommunicationService communicationService;

    public MessageCreatorService(CommunicationService communicationService) {
        this.communicationService = communicationService;
    }

    public void sendRoundMess(RoundInfo roundInfo){
        communicationService.sendMessage(roundInfo.getUserUUID(), "ROUND:Runda " + roundInfo.getRoundCounter());
    }

    public void sendAttackFasterMess(RoundInfo roundInfo) {
        if (roundInfo.getFasterLiveUnits() > 0) {
            if (roundInfo.getSlowerDeathUnits() > 0) {
                communicationService.sendMessage(roundInfo.getUserUUID(), "ATTACKF:Jednostka " + roundInfo.getFasterLiveUnits() + "x"
                        + roundInfo.getFasterUnit().getName() + " atakuje pierwsza, zadajac " + roundInfo.getFasterDmg()
                        + ".<br>Pokonuje " + roundInfo.getSlowerDeathUnits() + "x" + roundInfo.getSlowerUnit().getName());
            }
            else {
                communicationService.sendMessage(roundInfo.getUserUUID(), "ATTACKF:Jednostka " + roundInfo.getFasterLiveUnits() + "x"
                        + roundInfo.getFasterUnit().getName() + " atakuje pierwsza, zadajac " + roundInfo.getFasterDmg() + ".");
            }
        }
    }

    public void sendAttackSlowerMess(RoundInfo roundInfo) {
        if (roundInfo.getSlowerLiveUnits() > 0) {
            if (roundInfo.getFasterDeathUnits() > 0) {
                communicationService.sendMessage(roundInfo.getUserUUID(), "ATTACKS:Jednostka " + roundInfo.getSlowerLiveUnits() + "x"
                        + roundInfo.getSlowerUnit().getName() + " atakuje druga, zadajac " + roundInfo.getSlowerDmg()
                        + ".<br>Pokonuje " + roundInfo.getFasterDeathUnits() + "x" + roundInfo.getFasterUnit().getName());
            } else {
                communicationService.sendMessage(roundInfo.getUserUUID(), "ATTACKS:Jednostka " + roundInfo.getSlowerLiveUnits() + "x"
                        + roundInfo.getSlowerUnit().getName() + " atakuje druga, zadajac " + roundInfo.getSlowerDmg() + ".");
            }
        }
    }

    public void sendWinnerMess(RoundInfo roundInfo){
        communicationService.sendMessage(roundInfo.getUserUUID(), "VICTORY:" + roundInfo.getLoserUnit().getName() + " gina. "
                + roundInfo.getWinnerUnit().getName() + " wygrywaja pojedynek");
        communicationService.closeConnection(roundInfo.getUserUUID());
    }
}
