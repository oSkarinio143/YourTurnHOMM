package oskarinio143.yourturnhomm.service;

import org.springframework.stereotype.Service;
import oskarinio143.yourturnhomm.model.servicedto.RoundInfo;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class QueueService {
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final MessageCreatorService messageCreatorService;

    public QueueService(MessageCreatorService messageCreatorService) {
        this.messageCreatorService = messageCreatorService;
    }

    public void createQueue(RoundInfo roundInfo){
        roundInfo.setTempDelay(0);
        sendRoundMess(roundInfo);
        sendAttackFasterMess(roundInfo);
        sendAttackSlowerMess(roundInfo);
        sendWinnerMess(roundInfo);
    }

    public void sendRoundMess(RoundInfo roundInfo){
        Runnable sendRound = () ->
            messageCreatorService.sendRoundMess(roundInfo);
        scheduler.schedule(sendRound ,roundInfo.getMessageDelay() + roundInfo.getTempDelay(), TimeUnit.SECONDS);
    }

    public void sendAttackFasterMess(RoundInfo roundInfo){
        roundInfo.setTempDelay(roundInfo.getTempDelay() + 1);
        Runnable sendFasterAttack = () ->
            messageCreatorService.sendAttackFasterMess(roundInfo);
        scheduler.schedule(sendFasterAttack, roundInfo.getMessageDelay() + roundInfo.getTempDelay(), TimeUnit.SECONDS);
    }

    public void sendAttackSlowerMess(RoundInfo roundInfo){
        if(roundInfo.getSlowerLiveUnits() > 0){
            roundInfo.setTempDelay(roundInfo.getTempDelay() + 1);
            Runnable sendSlowerAttack = () -> messageCreatorService.sendAttackSlowerMess(roundInfo);
            scheduler.schedule(sendSlowerAttack, roundInfo.getMessageDelay() + roundInfo.getTempDelay(), TimeUnit.SECONDS);
        }
    }

    public void sendWinnerMess(RoundInfo roundInfo){
        if(roundInfo.getWinnerUnit() != null){
            roundInfo.setTempDelay(roundInfo.getTempDelay() + 1);
            Runnable sendWinner = () -> messageCreatorService.sendWinnerMess(roundInfo);
            scheduler.schedule(sendWinner, roundInfo.getMessageDelay() + roundInfo.getTempDelay(), TimeUnit.SECONDS);
        }
    }
}