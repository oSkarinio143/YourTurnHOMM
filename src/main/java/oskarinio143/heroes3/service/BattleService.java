package oskarinio143.heroes3.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import oskarinio143.heroes3.model.AttackInfo;
import oskarinio143.heroes3.model.RoundInfo;
import oskarinio143.heroes3.model.Unit;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

@EnableAsync
@Service
public class BattleService {

    private final MessageCreatorService messageCreatorService;
    private final QueueService queueService;

    @Value("${battle.rates.attack}")
    private double ATK_RATE;
    @Value("${battle.rates.defense}")
    private double DEF_RATE;

    public BattleService(MessageCreatorService messageCreatorService, QueueService queueService) {
        this.messageCreatorService = messageCreatorService;
        this.queueService = queueService;
    }

    @Async
    public void prepareBattle(Unit leftUnit, Unit rightUnit, int leftQuantity, int rightQuantity, String userUUID){
        AttackInfo attackInfo = prepareAttackInfo(leftUnit, rightUnit, leftQuantity, rightQuantity, userUUID);
        startBattle(attackInfo);
    }

    public void startBattle(AttackInfo attackInfo){
        RoundInfo roundInfo = new RoundInfo(attackInfo.getUserUUID(), 1, 2);
        while (attackInfo.getFasterQuantity() != 0 && attackInfo.getSlowerQuantity() != 0){
            startRound(roundInfo, attackInfo);
            sendRoundMessages(roundInfo);
            setNextRoundInfo(roundInfo);
        }
    }

    public void startRound(RoundInfo roundInfo, AttackInfo attackInfo){
        startAttackFaster(roundInfo, attackInfo);
        startAttackSlower(roundInfo, attackInfo);
        isWinner(roundInfo);
    }

    public void sendRoundMessages(RoundInfo roundInfo){
        RoundInfo snapshotRoundInfo = roundInfo.clone();
        queueService.createQueue(snapshotRoundInfo);
    }

    public void setNextRoundInfo(RoundInfo roundInfo){
        roundInfo.setRoundCounter(roundInfo.getRoundCounter() + 1);
        roundInfo.setMessageDelay(roundInfo.getMessageDelay() + 4);
    }

    public void startAttackFaster(RoundInfo roundInfo, AttackInfo attackInfo){
        setRoundInfoAttack(attackInfo, attackInfo.getFasterUnit(), attackInfo.getSlowerUnit(), attackInfo.getFasterQuantity());
        attackInfo.setSlowerQuantity(countLiveUnitsAfterAttack(attackInfo, attackInfo.getFasterQuantity(), attackInfo.getSlowerQuantity()));
        setRoundInfoFaster(roundInfo, attackInfo);
    }

    public void startAttackSlower(RoundInfo roundInfo, AttackInfo attackInfo){
        setRoundInfoAttack(attackInfo, attackInfo.getSlowerUnit(), attackInfo.getFasterUnit(), attackInfo.getSlowerQuantity());
        attackInfo.setFasterQuantity(countLiveUnitsAfterAttack(attackInfo, attackInfo.getSlowerQuantity(), attackInfo.getFasterQuantity()));
        setRoundInfoSlower(roundInfo, attackInfo);
    }

    public void setRoundInfoAttack(AttackInfo attackInfo, Unit atkUnit, Unit defUnit, int attackingQuantity){
        attackInfo.setAtkUnit(atkUnit);
        attackInfo.setDefUnit(defUnit);
        attackInfo.setAttackingUnits(attackingQuantity);
    }

    public void setRoundInfoFaster(RoundInfo roundInfo, AttackInfo attackInfo){
        roundInfo.setFasterDmg(attackInfo.getAtkDmg());
        roundInfo.setFasterLiveUnits(attackInfo.getAttackingUnits());
        roundInfo.setSlowerDeathUnits(attackInfo.getDeathUnits());
        roundInfo.setFasterUnit(attackInfo.getFasterUnit());
    }

    public void setRoundInfoSlower(RoundInfo roundInfo, AttackInfo attackInfo){
        roundInfo.setSlowerDmg(attackInfo.getAtkDmg());
        roundInfo.setSlowerLiveUnits(attackInfo.getAttackingUnits());
        roundInfo.setFasterDeathUnits(attackInfo.getDeathUnits());
        roundInfo.setSlowerUnit(attackInfo.getSlowerUnit());
    }

    public AttackInfo prepareAttackInfo(Unit leftUnit, Unit rightUnit, int leftQuantity, int rightQuantity, String userUUID){
        AttackInfo attackInfo = new AttackInfo(userUUID);
        attackInfo.setFasterUnit(findFaster(leftUnit, rightUnit));
        attackInfo.setSlowerUnit(findSlower(leftUnit, rightUnit));
        attackInfo.setFasterQuantity(findFasterQuantity(attackInfo.getFasterUnit(), leftUnit, leftQuantity, rightQuantity));
        attackInfo.setSlowerQuantity(findSlowerQuantity(attackInfo.getSlowerUnit(), leftUnit, leftQuantity, rightQuantity));
        return attackInfo;
    }

    public Unit findFaster(Unit leftUnit, Unit rightUnit){
        //Jeśli speed jest równy to atakuje losowo wybrana
        if(leftUnit.getSpeed() > rightUnit.getSpeed())
            return  leftUnit;
        if(leftUnit.getSpeed() == rightUnit.getSpeed()){
            int drawNumb = (int) (Math.random()*2);
            if(drawNumb == 0)
                return leftUnit;
        }
        return rightUnit;
    }

    public Unit findSlower(Unit leftUnit, Unit rightUnit){
        if(findFaster(leftUnit, rightUnit) == leftUnit){
            return rightUnit;
        }
        return leftUnit;
    }

    public int findFasterQuantity(Unit fasterUnit, Unit leftUnit, int leftQuantity, int rightQuantity){
        if(fasterUnit == leftUnit)
            return leftQuantity;
        return rightQuantity;
    }

    public int findSlowerQuantity(Unit slowerUnit, Unit leftUnit, int leftQuantity, int rightQuantity){
        if(slowerUnit == leftUnit)
            return leftQuantity;
        return rightQuantity;
    }

    public int countLiveUnitsAfterAttack(AttackInfo attackInfo, int attackingQuantity, int defendingQuantity){
        int attackingDmg = generateDmg(attackInfo.getAtkUnit(), attackInfo.getDefUnit(), attackingQuantity);
        setDmg(attackInfo, attackingDmg);
        return countLiveUnits(attackInfo, attackingDmg, defendingQuantity);
    }

    public int countLiveUnits(AttackInfo attackInfo, int dmg, int enemyQuantity){
        int deathUnits;
        int leftHp = attackInfo.getDefUnit().getHpLeft();
        if(leftHp - dmg > 0)
            deathUnits = 0;
        else{
            int dmgLeft = dmg - leftHp;
            deathUnits = dmgLeft / attackInfo.getDefUnit().getHp() + 1;
        }
        if(deathUnits > enemyQuantity)
            deathUnits = enemyQuantity;
        attackInfo.setDeathUnits(deathUnits);
        setHpLeft(attackInfo.getDefUnit(), dmg, deathUnits, enemyQuantity);
        return Math.max(enemyQuantity - deathUnits, 0);

    }

    public void setHpLeft(Unit unit, int dmg, int deathUnits, int enemyQuantity){
        if(enemyQuantity - deathUnits < 1)
            unit.setHpLeft(0);
        else if(deathUnits == 0)
            unit.setHpLeft(unit.getHpLeft() - dmg);
        else
            unit.setHpLeft(unit.getHpLeft() - (dmg - unit.getHp() * deathUnits));
    }

    public void setDmg(AttackInfo roundInfo, int dmg){
        roundInfo.setAtkDmg(dmg);
    }

    public int generateDmg(Unit unit, Unit enemyUnit, int quantity){
        double unitAtk = countAtkRate(unit);
        double enemyDef = countDefRate(enemyUnit);
        int basicDmg = drawDmg(unit, quantity);
        return  (int) (basicDmg * unitAtk / enemyDef);
    }

    public int drawDmg(Unit unit, int quantity){
        int minDmg = unit.getMinDamage();
        int maxDmg = unit.getMaxDamage();
        int sumDmg = 0;
        for (int i = 0; i < quantity; i++) {
            int singDmg = drawSingDmg(minDmg, maxDmg);
            sumDmg += singDmg;
        }
        return sumDmg;
    }

    public int drawSingDmg(int minDmg, int maxDmg){
        return (int) (Math.random() * (maxDmg - minDmg + 1) + minDmg);
    }

    public double countAtkRate(Unit unit){
        //Statystyka ataku z każdym poziomem podnosi bazowy atk o 10%
        return ATK_RATE * unit.getAttack() + 1;
    }

    public double countDefRate(Unit unit) {
        //Statystyka defa z każdym poziomem podnosi bazowy def o 10%
        return DEF_RATE * unit.getDefense() + 1;
    }

    public boolean isWinner(RoundInfo roundInfo){
        if(roundInfo.getSlowerLiveUnits() == 0) {
            roundInfo.setWinnerUnit(roundInfo.getFasterUnit());
            roundInfo.setLoserUnit(roundInfo.getSlowerUnit());
            return true;
        }
        if(roundInfo.getFasterLiveUnits() == 0){
            roundInfo.setLoserUnit(roundInfo.getSlowerUnit());
            roundInfo.setWinnerUnit(roundInfo.getFasterUnit());
            return true;
        }
        return false;
    }
}
