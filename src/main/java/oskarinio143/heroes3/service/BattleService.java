package oskarinio143.heroes3.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import oskarinio143.heroes3.model.RoundInfo;
import oskarinio143.heroes3.model.Unit;
import oskarinio143.heroes3.controller.BattleComunicator;

import java.util.ArrayList;
import java.util.List;

@EnableAsync
@Service
public class BattleService {

    private final CommunicationService communicationService;
    @Value("${battle.rates.attack}")
    private double ATK_RATE;
    @Value("${battle.rates.defense}")
    private double DEF_RATE;

    public BattleService(CommunicationService communicationService) {
        this.communicationService = communicationService;
    }

    @Async
    public void startBattle(Unit leftUnit, Unit rightUnit, int leftQuantity, int rightQuantity, String userUUID){
        sleep(1000);
        RoundInfo roundInfo = setRoundInfo(leftUnit, rightUnit, leftQuantity, rightQuantity, userUUID);
        findWinner(roundInfo);
        sendWinnerMess(roundInfo);
    }

    public RoundInfo setRoundInfo(Unit leftUnit, Unit rightUnit, int leftQuantity, int rightQuantity, String userUUID){
        RoundInfo roundInfo = new RoundInfo();
        roundInfo.setFasterUnit(findFaster(leftUnit, rightUnit));
        roundInfo.setSlowerUnit(findSlower(leftUnit, rightUnit));
        roundInfo.setFasterQuantity(findFasterQuantity(roundInfo.getFasterUnit(), leftUnit, leftQuantity, rightQuantity));
        roundInfo.setSlowerQuantity(findSlowerQuantity(roundInfo.getSlowerUnit(), leftUnit, leftQuantity, rightQuantity));
        roundInfo.setUserUUID(userUUID);
        return roundInfo;
    }

    public void findWinner(RoundInfo roundInfo){
        List<Integer> unitsQuantites = new ArrayList<>(List.of(roundInfo.getFasterQuantity(), roundInfo.getSlowerQuantity()));
        int counter = 0;
        while (unitsQuantites.get(0) != 0 && unitsQuantites.get(1) != 0){
            counter++;
            sendRoundMess(roundInfo, counter);
            unitsQuantites = startRound(roundInfo, unitsQuantites.get(0), unitsQuantites.get(1));
        }
        setWinner(roundInfo, unitsQuantites);
    }

    public void setWinner(RoundInfo roundInfo, List<Integer> unitsQuantites){
        if(unitsQuantites.get(0) == 0) {
            roundInfo.setWinnerUnit(roundInfo.getSlowerUnit());
            roundInfo.setLoserUnit(roundInfo.getFasterUnit());
        }
        else if (unitsQuantites.get(1) == 0) {
            roundInfo.setWinnerUnit(roundInfo.getFasterUnit());
            roundInfo.setLoserUnit(roundInfo.getSlowerUnit());
        }
    }

    public List<Integer> startRound(RoundInfo roundInfo, int fasterQuantity, int slowerQuantity){
        roundInfo.setAtkUnit(roundInfo.getFasterUnit());
        roundInfo.setDefUnit(roundInfo.getSlowerUnit());
        slowerQuantity = countLiveUnitsAfterAttack(roundInfo, fasterQuantity, slowerQuantity);
        sendAttackMess(roundInfo, fasterQuantity, "pierwsza", "ATTACKF");

        roundInfo.setAtkUnit(roundInfo.getSlowerUnit());
        roundInfo.setDefUnit(roundInfo.getFasterUnit());
        fasterQuantity = countLiveUnitsAfterAttack(roundInfo, slowerQuantity, fasterQuantity);
        sendAttackMess(roundInfo, slowerQuantity, "druga", "ATTACKS");

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

    public int countLiveUnitsAfterAttack(RoundInfo roundInfo, int attackingQuantity, int defendingQuantity){
        int attackingDmg = generateDmg(roundInfo.getAtkUnit(), roundInfo.getDefUnit(), attackingQuantity);
        setDmg(roundInfo, attackingDmg);
        return countLiveUnits(roundInfo, attackingDmg, defendingQuantity);
    }

    public int countLiveUnits(RoundInfo roundInfo, int dmg, int enemyQuantity){
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

    public void setDmg(RoundInfo roundInfo, int dmg){
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

    private void sendRoundMess(RoundInfo roundInfo, int number){
        communicationService.sendMessage(roundInfo.getUserUUID(), "ROUND:Runda " + number);
        sleep(2000);
    }

    private void sendAttackMess(RoundInfo roundInfo, int unitQuantity, String firOrSec, String attackType){
        if(unitQuantity > 0) {
            if (roundInfo.getDeathUnits() > 0)
                communicationService.sendMessage(roundInfo.getUserUUID(), attackType + ":Jednostka " + unitQuantity + "x"
                        + roundInfo.getAtkUnit().getName() + " atakuje " + firOrSec + " zadajac " + roundInfo.getAtkDmg()
                        + ".<br>Pokonuje " + roundInfo.getDeathUnits() + "x" + roundInfo.getDefUnit().getName());
            else
                communicationService.sendMessage(roundInfo.getUserUUID(), attackType + ":Jednostka " + unitQuantity + "x"
                        + roundInfo.getAtkUnit().getName() + " atakuje " + firOrSec + " zadajac " + roundInfo.getAtkDmg());
        }
        sleep(1000);
    }

    private void sendWinnerMess(RoundInfo roundInfo){
        communicationService.sendMessage(roundInfo.getUserUUID(), "VICTORY:" + roundInfo.getLoserUnit().getName() + " gina. "
                + roundInfo.getWinnerUnit().getName() + " wygrywaja pojedynek");
        communicationService.closeConnection(roundInfo.getUserUUID());
    }

    private void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {}
    }
}
