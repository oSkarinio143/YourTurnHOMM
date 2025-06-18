package oskarinio143.heroes3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import oskarinio143.heroes3.BattleComunicator;
import oskarinio143.heroes3.RoundInfo;
import oskarinio143.heroes3.Unit;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@EnableAsync
@Service
public class BattleService {

    private final BattleComunicator battleComunicator;
    private final double ATK_RATE = 0.1;
    private final double DEF_RATE = 0.1;

    public BattleService(BattleComunicator battleComunicator) {
        this.battleComunicator = battleComunicator;
    }

    @Async
    public void startBattle(Unit leftUnit, Unit rightUnit, int leftQuantity, int rightQuantity){
        sleep(1000);
        RoundInfo roundInfo = setRoundInfo(leftUnit, rightUnit,leftQuantity,rightQuantity);
        findWinner(roundInfo);
        sendWinnerMess(roundInfo);

    }

    public RoundInfo setRoundInfo(Unit leftUnit, Unit rightUnit, int leftQuantity, int rightQuantity){
        RoundInfo roundInfo = new RoundInfo();
        roundInfo.setFasterUnit(findFaster(leftUnit, rightUnit));
        roundInfo.setSlowerUnit(findSlower(leftUnit, rightUnit));
        roundInfo.setFasterQuantity(findFasterQuantity(roundInfo.getFasterUnit(), leftUnit, leftQuantity, rightQuantity));
        roundInfo.setSlowerQuantity(findSlowerQuantity(roundInfo.getSlowerUnit(), leftUnit, leftQuantity, rightQuantity));
        return roundInfo;
    }

    public void findWinner(RoundInfo roundInfo){
        List<Integer> unitsQuantites = new ArrayList<>(List.of(roundInfo.getFasterQuantity(), roundInfo.getSlowerQuantity()));
        int counter = 0;
        while (unitsQuantites.get(0) != 0 && unitsQuantites.get(1) != 0){
            counter++;
            sendRoundMess(counter);
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
        sendAttackFasterMess(roundInfo.getFasterUnit().getName(), roundInfo.getSlowerUnit().getName(), fasterQuantity, roundInfo.getFasterDmg());

        roundInfo.setAtkUnit(roundInfo.getSlowerUnit());
        roundInfo.setDefUnit(roundInfo.getFasterUnit());
        fasterQuantity = countLiveUnitsAfterAttack(roundInfo, slowerQuantity, fasterQuantity);

        sendAttackSlowerMess(roundInfo.getSlowerUnit().getName(), roundInfo.getFasterUnit().getName(), slowerQuantity, roundInfo.getSlowerDmg());
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
        return countLiveUnits(roundInfo.getDefUnit(), attackingDmg, defendingQuantity);
    }

    public int countLiveUnits(Unit enemyUnit, int dmg, int enemyQuantity){
        int deathUnits;
        int leftHp = enemyUnit.getHpLeft();
        if(leftHp - dmg > 0)
            deathUnits = 0;
        else{
            int dmgLeft = dmg - leftHp;
            deathUnits = dmgLeft / enemyUnit.getHp() + 1;
        }
        setHpLeft(enemyUnit, dmg, deathUnits, enemyQuantity);
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
        if(roundInfo.getAtkUnit().equals(roundInfo.getFasterUnit()))
            roundInfo.setFasterDmg(dmg);
        if(roundInfo.getAtkUnit().equals(roundInfo.getSlowerUnit()))
            roundInfo.setSlowerDmg(dmg);
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

    private void sendRoundMess(int number){
        battleComunicator.sendMessage("ROUND:Runda " + number);
        sleep(2000);
    }

    private void sendAttackFasterMess(String unitName, String enemyName, int unitQuantity, int dmg){
        if(unitQuantity > 0)
            battleComunicator.sendMessage("ATTACKF:Jednostka " + unitQuantity + "x" + unitName + " atakuje pierwsza zadajac " + dmg);
        sleep(1000);
    }

    private void sendAttackSlowerMess(String unitName, String enemyName, int unitQuantity, int dmg){
        if(unitQuantity > 0)
            battleComunicator.sendMessage("ATTACKS:Jednostka " + unitQuantity + "x" + unitName + " atakuje druga zadajac " + dmg);
        sleep(1000);
    }

    private void sendWinnerMess(RoundInfo roundInfo){
        battleComunicator.sendMessage(roundInfo.getLoserUnit().getName() + " gina. " + roundInfo.getWinnerUnit().getName() + " wygrywaja pojedynek");
    }

    private void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {}
    }
}
