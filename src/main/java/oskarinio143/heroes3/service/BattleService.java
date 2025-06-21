package oskarinio143.heroes3.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import oskarinio143.heroes3.model.AttackInfo;
import oskarinio143.heroes3.model.RoundInfo;
import oskarinio143.heroes3.model.Unit;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

@EnableAsync
@Service
public class BattleService {

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final CommunicationService communicationService;
    private final MessageCreatorService messageCreatorService;

    @Value("${battle.rates.attack}")
    private double ATK_RATE;
    @Value("${battle.rates.defense}")
    private double DEF_RATE;

    public BattleService(CommunicationService communicationService, MessageCreatorService messageCreatorService) {
        this.communicationService = communicationService;
        this.messageCreatorService = messageCreatorService;
    }

    @Async
    public void startBattle(Unit leftUnit, Unit rightUnit, int leftQuantity, int rightQuantity, String userUUID){
        AttackInfo roundInfo = setAttackInfo(leftUnit, rightUnit, leftQuantity, rightQuantity, userUUID);
        findWinner(roundInfo);
        //messageCreatorService.sendWinnerMess(roundInfo);
    }

    public void findWinner(AttackInfo attackInfo){
        RoundInfo roundInfo = new RoundInfo();
        List<Integer> unitsQuantites = new ArrayList<>(List.of(attackInfo.getFasterQuantity(), attackInfo.getSlowerQuantity()));
        int counter = 0;
        int delay = -2;
        while (unitsQuantites.get(0) != 0 && unitsQuantites.get(1) != 0){
            counter++;
            delay += 4;
            roundInfo.setRoundCounter(counter);
            unitsQuantites = startRound(roundInfo, attackInfo, unitsQuantites.get(0), unitsQuantites.get(1));
            RoundInfo snapshotRoundInfo = roundInfo.clone();
            SingleRoundSimulator singleRoundSimulator = new SingleRoundSimulator(messageCreatorService, this, snapshotRoundInfo);
            scheduler.schedule(singleRoundSimulator, delay, TimeUnit.SECONDS);
        }
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

    public void setRoundInfoFaster(RoundInfo roundInfo, AttackInfo attackInfo){
        roundInfo.setFasterDmg(attackInfo.getAtkDmg());
        roundInfo.setFasterLiveUnits(attackInfo.getAttackingUnits());
        roundInfo.setSlowerDeathUnits(attackInfo.getDeathUnits());
        roundInfo.setFasterUnit(attackInfo.getFasterUnit());
        roundInfo.setUserUUID(attackInfo.getUserUUID());
    }

    public void setRoundInfoSlower(RoundInfo roundInfo, AttackInfo attackInfo){
        roundInfo.setSlowerDmg(attackInfo.getAtkDmg());
        roundInfo.setSlowerLiveUnits(attackInfo.getAttackingUnits());
        roundInfo.setFasterDeathUnits(attackInfo.getDeathUnits());
        roundInfo.setSlowerUnit(attackInfo.getSlowerUnit());
    }

    public AttackInfo setAttackInfo(Unit leftUnit, Unit rightUnit, int leftQuantity, int rightQuantity, String userUUID){
        AttackInfo roundInfo = new AttackInfo();
        roundInfo.setFasterUnit(findFaster(leftUnit, rightUnit));
        roundInfo.setSlowerUnit(findSlower(leftUnit, rightUnit));
        roundInfo.setFasterQuantity(findFasterQuantity(roundInfo.getFasterUnit(), leftUnit, leftQuantity, rightQuantity));
        roundInfo.setSlowerQuantity(findSlowerQuantity(roundInfo.getSlowerUnit(), leftUnit, leftQuantity, rightQuantity));
        roundInfo.setUserUUID(userUUID);
        return roundInfo;
    }

    public List<Integer> startRound(RoundInfo roundInfo, AttackInfo attackInfo, int fasterQuantity, int slowerQuantity){
        attackInfo.setAtkUnit(attackInfo.getFasterUnit());
        attackInfo.setDefUnit(attackInfo.getSlowerUnit());
        slowerQuantity = countLiveUnitsAfterAttack(attackInfo, fasterQuantity, slowerQuantity);
        attackInfo.setAttackingUnits(fasterQuantity);
        //messageCreatorService.sendAttackMess(attackInfo, "pierwsza", "ATTACKF");
        setRoundInfoFaster(roundInfo, attackInfo);

        attackInfo.setAtkUnit(attackInfo.getSlowerUnit());
        attackInfo.setDefUnit(attackInfo.getFasterUnit());
        fasterQuantity = countLiveUnitsAfterAttack(attackInfo, slowerQuantity, fasterQuantity);
        attackInfo.setAttackingUnits(slowerQuantity);
        //messageCreatorService.sendAttackMess(attackInfo, "druga", "ATTACKS");
        setRoundInfoSlower(roundInfo, attackInfo);

        return new ArrayList<>(List.of(fasterQuantity, slowerQuantity));
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

    public int countLiveUnitsAfterAttack(AttackInfo roundInfo, int attackingQuantity, int defendingQuantity){
        int attackingDmg = generateDmg(roundInfo.getAtkUnit(), roundInfo.getDefUnit(), attackingQuantity);
        setDmg(roundInfo, attackingDmg);
        return countLiveUnits(roundInfo, attackingDmg, defendingQuantity);
    }

    public int countLiveUnits(AttackInfo roundInfo, int dmg, int enemyQuantity){
        int deathUnits;
        int leftHp = roundInfo.getDefUnit().getHpLeft();
        if(leftHp - dmg > 0)
            deathUnits = 0;
        else{
            int dmgLeft = dmg - leftHp;
            deathUnits = dmgLeft / roundInfo.getDefUnit().getHp() + 1;
        }
        if(deathUnits > enemyQuantity)
            deathUnits = enemyQuantity;
        roundInfo.setDeathUnits(deathUnits);
        setHpLeft(roundInfo.getDefUnit(), dmg, deathUnits, enemyQuantity);
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
}
