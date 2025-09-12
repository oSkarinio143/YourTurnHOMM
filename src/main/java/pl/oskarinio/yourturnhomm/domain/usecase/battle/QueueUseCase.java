package pl.oskarinio.yourturnhomm.domain.usecase.battle;

import pl.oskarinio.yourturnhomm.domain.model.battle.RoundInfo;
import pl.oskarinio.yourturnhomm.domain.port.battle.MessageCreator;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class QueueUseCase {
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final MessageCreator messageCreator;

    public QueueUseCase(MessageCreator messageCreator) {
        this.messageCreator = messageCreator;
    }

    public void createQueue(RoundInfo roundInfo){
        roundInfo.setTempDelay(0);
        sendRoundMess(roundInfo);
        sendAttackFasterMess(roundInfo);
        sendAttackSlowerMess(roundInfo);
        sendWinnerMess(roundInfo);
    }

    private void sendRoundMess(RoundInfo roundInfo){
        Runnable sendRound = () ->
            messageCreator.sendRoundMess(roundInfo);
        scheduler.schedule(sendRound ,roundInfo.getMessageDelay() + roundInfo.getTempDelay(), TimeUnit.SECONDS);
    }

    private void sendAttackFasterMess(RoundInfo roundInfo){
        roundInfo.setTempDelay(roundInfo.getTempDelay() + 1);
        Runnable sendFasterAttack = () ->
            messageCreator.sendAttackFasterMess(roundInfo);
        scheduler.schedule(sendFasterAttack, roundInfo.getMessageDelay() + roundInfo.getTempDelay(), TimeUnit.SECONDS);
    }

    private void sendAttackSlowerMess(RoundInfo roundInfo){
        if(roundInfo.getSlowerLiveUnits() > 0){
            roundInfo.setTempDelay(roundInfo.getTempDelay() + 1);
            Runnable sendSlowerAttack = () -> messageCreator.sendAttackSlowerMess(roundInfo);
            scheduler.schedule(sendSlowerAttack, roundInfo.getMessageDelay() + roundInfo.getTempDelay(), TimeUnit.SECONDS);
        }
    }

    private void sendWinnerMess(RoundInfo roundInfo){
        if(roundInfo.getWinnerUnit() != null){
            roundInfo.setTempDelay(roundInfo.getTempDelay() + 1);
            Runnable sendWinner = () -> messageCreator.sendWinnerMess(roundInfo);
            scheduler.schedule(sendWinner, roundInfo.getMessageDelay() + roundInfo.getTempDelay(), TimeUnit.SECONDS);
        }
    }
}