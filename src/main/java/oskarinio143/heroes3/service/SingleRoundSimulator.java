package oskarinio143.heroes3.service;

import org.springframework.stereotype.Component;
import oskarinio143.heroes3.model.AttackInfo;
import oskarinio143.heroes3.model.RoundInfo;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SingleRoundSimulator implements Runnable{
    ScheduledExecutorService localScheduler = Executors.newSingleThreadScheduledExecutor();
    private final MessageCreatorService messageCreatorService;
    private final BattleService battleService;
    private RoundInfo roundInfo;


    public SingleRoundSimulator(MessageCreatorService messageCreatorService, BattleService battleService, RoundInfo roundInfo) {
        this.messageCreatorService = messageCreatorService;
        this.battleService = battleService;
        this.roundInfo = roundInfo;
    }

    @Override
    public void run() {
        localScheduler.schedule(() -> messageCreatorService.sendRoundMess(roundInfo), 0, TimeUnit.SECONDS);
        localScheduler.schedule(() -> messageCreatorService.sendAttackFasterMess(roundInfo),1, TimeUnit.SECONDS);
        localScheduler.schedule(() -> messageCreatorService.sendAttackSlowerMess(roundInfo),2, TimeUnit.SECONDS);
        if(battleService.isWinner(roundInfo)){
            localScheduler.schedule(() -> messageCreatorService.sendWinnerMess(roundInfo),3,TimeUnit.SECONDS);
        }
    }
}
