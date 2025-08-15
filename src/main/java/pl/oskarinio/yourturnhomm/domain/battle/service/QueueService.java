package pl.oskarinio.yourturnhomm.domain.battle.service;

import pl.oskarinio.yourturnhomm.domain.battle.port.in.MessageCreatorUseCase;
import pl.oskarinio.yourturnhomm.domain.battle.model.RoundInfo;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class QueueService {
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final MessageCreatorUseCase messageCreatorUseCase;

    public QueueService(MessageCreatorUseCase messageCreatorUseCase) {
        this.messageCreatorUseCase = messageCreatorUseCase;
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
            messageCreatorUseCase.sendRoundMess(roundInfo);
        scheduler.schedule(sendRound ,roundInfo.getMessageDelay() + roundInfo.getTempDelay(), TimeUnit.SECONDS);
    }

    public void sendAttackFasterMess(RoundInfo roundInfo){
        roundInfo.setTempDelay(roundInfo.getTempDelay() + 1);
        Runnable sendFasterAttack = () ->
            messageCreatorUseCase.sendAttackFasterMess(roundInfo);
        scheduler.schedule(sendFasterAttack, roundInfo.getMessageDelay() + roundInfo.getTempDelay(), TimeUnit.SECONDS);
    }

    public void sendAttackSlowerMess(RoundInfo roundInfo){
        if(roundInfo.getSlowerLiveUnits() > 0){
            roundInfo.setTempDelay(roundInfo.getTempDelay() + 1);
            Runnable sendSlowerAttack = () -> messageCreatorUseCase.sendAttackSlowerMess(roundInfo);
            scheduler.schedule(sendSlowerAttack, roundInfo.getMessageDelay() + roundInfo.getTempDelay(), TimeUnit.SECONDS);
        }
    }

    public void sendWinnerMess(RoundInfo roundInfo){
        if(roundInfo.getWinnerUnit() != null){
            roundInfo.setTempDelay(roundInfo.getTempDelay() + 1);
            Runnable sendWinner = () -> messageCreatorUseCase.sendWinnerMess(roundInfo);
            scheduler.schedule(sendWinner, roundInfo.getMessageDelay() + roundInfo.getTempDelay(), TimeUnit.SECONDS);
        }
    }
}