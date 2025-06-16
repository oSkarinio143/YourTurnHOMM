package oskarinio143.heroes3.controller;

import org.springframework.stereotype.Service;
import oskarinio143.heroes3.Unit;

import java.util.ArrayList;
import java.util.List;

@Service
public class BattleService {
    private final double ATK_RATE = 0.1;
    private final double DEF_RATE = 0.1;

    public void startBattle(Unit leftUnit, Unit rightUnit, int leftQuantity, int rightQuantity){
        Unit fasterUnit = findFaster(leftUnit, rightUnit);
        Unit slowerUnit = findSlower(leftUnit, rightUnit);
        int fasterQuantity = findFasterQuantity(fasterUnit, leftUnit, leftQuantity, rightQuantity);
        int slowerQuantity = findSlowerQuantity(slowerUnit, leftUnit, leftQuantity, rightQuantity);

        Unit winnerUnit = findWinner(fasterUnit, slowerUnit, fasterQuantity, slowerQuantity);
        System.out.println(winnerUnit.getName());

    }
    public Unit findWinner(Unit fasterUnit, Unit slowerUnit, int fasterQuantity, int slowerQuantity){
        List<Integer> unitsQuantites = startRound(fasterUnit, slowerUnit, fasterQuantity, slowerQuantity);
        while (unitsQuantites.get(0) != 0){
            System.out.println("\nslower - " + unitsQuantites.get(1));
            System.out.println("faster - " + unitsQuantites.get(0));
            unitsQuantites = startRound(fasterUnit, slowerUnit, unitsQuantites.get(0), unitsQuantites.get(1));
            System.out.println("slower - " + unitsQuantites.get(1));
            System.out.println("faster - " + unitsQuantites.get(0));
            System.out.println("slowerLeftHp - " + slowerUnit.getHpLeft());
            System.out.println("fasterLeftHp - " + fasterUnit.getHpLeft());
            if(unitsQuantites.get(1) == 0)
                return fasterUnit;
        }
        return slowerUnit;
    }


    public List<Integer> startRound(Unit fasterUnit, Unit slowerUnit, int fasterQuantity, int slowerQuantity){
        slowerQuantity = countLiveUnitsAfterAttack(fasterUnit, slowerUnit, fasterQuantity, slowerQuantity);
        fasterQuantity = countLiveUnitsAfterAttack(slowerUnit, fasterUnit, slowerQuantity, fasterQuantity);
        System.out.println(fasterQuantity + " - " + slowerQuantity);
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

    public int countLiveUnitsAfterAttack(Unit attackingUnit, Unit defendingUnit, int attackingQuantity, int defendingQuantity){
        int attackingDmg = generateDmg(attackingUnit, defendingUnit, attackingQuantity);
        return countLiveUnits(defendingUnit, attackingDmg, defendingQuantity);
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
