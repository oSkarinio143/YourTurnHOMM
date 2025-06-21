package oskarinio143.heroes3.service;

import org.springframework.stereotype.Component;
import oskarinio143.heroes3.model.AttackInfo;
import oskarinio143.heroes3.model.RoundInfo;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SingleRoundSimulator implements Runnable {
    ScheduledExecutorService localScheduler = Executors.newSingleThreadScheduledExecutor();
    private final MessageCreatorService messageCreatorService;
    private final BattleService battleService;
    private RoundInfo roundInfo;
    private final Queue<Runnable> messagesQueue = new LinkedList<>();
    private final int DELAY = 1;
    private final int INITIAL_DELAY = 0;


    public SingleRoundSimulator(MessageCreatorService messageCreatorService, BattleService battleService, RoundInfo roundInfo) {
        this.messageCreatorService = messageCreatorService;
        this.battleService = battleService;
        this.roundInfo = roundInfo;
    }

    @Override
    public void run() {
        messagesQueue.add(() -> messageCreatorService.sendRoundMess(roundInfo));

        if(roundInfo.getFasterLiveUnits() > 0)
            messagesQueue.add(() -> messageCreatorService.sendAttackFasterMess(roundInfo));

        if(roundInfo.getSlowerLiveUnits() > 0)
            messagesQueue.add(() -> messageCreatorService.sendAttackSlowerMess(roundInfo));

        messagesQueue.add(() -> {
            if (battleService.isWinner(roundInfo))
                messageCreatorService.sendWinnerMess(roundInfo);
        });

        localScheduler.scheduleWithFixedDelay(() -> {
            Runnable sendMessage = messagesQueue.poll();
            if (sendMessage != null) {
                sendMessage.run();
            } else {
                localScheduler.shutdown();
            }
        }, INITIAL_DELAY, DELAY, TimeUnit.SECONDS);
    }
}
