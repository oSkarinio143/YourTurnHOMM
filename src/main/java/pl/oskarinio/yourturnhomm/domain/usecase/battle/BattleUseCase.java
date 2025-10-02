package pl.oskarinio.yourturnhomm.domain.usecase.battle;

import pl.oskarinio.yourturnhomm.domain.model.battle.AttackInfo;
import pl.oskarinio.yourturnhomm.domain.model.battle.BattleUnit;
import pl.oskarinio.yourturnhomm.domain.model.battle.RoundInfo;
import pl.oskarinio.yourturnhomm.domain.model.battle.Unit;
import pl.oskarinio.yourturnhomm.domain.model.form.DuelForm;
import pl.oskarinio.yourturnhomm.domain.port.battle.Queue;

import java.util.Random;
import java.util.stream.IntStream;

public class BattleUseCase {
    private final Queue queue;
    private final Random random;

    private final double ATK_RATE;
    private final double DEF_RATE;

    public BattleUseCase(Queue queue, double atkRate, double defRate, Random random) {
        this.queue = queue;
        this.ATK_RATE = atkRate;
        this.DEF_RATE = defRate;
        this.random = random;
    }

    public void prepareBattle(DuelForm duelForm){
        AttackInfo attackInfo = prepareAttackInfo(duelForm);
        startBattle(attackInfo);
    }

    private AttackInfo prepareAttackInfo(DuelForm duelForm){
        AttackInfo attackInfo = new AttackInfo(duelForm.getUserUUID());

        setHerosStatsIfNull(duelForm);
        BattleUnit leftBattleUnit = prepareBattleUnit(duelForm.getLeftUnit(), duelForm.getLeftHeroAttack(), duelForm.getLeftHeroDefense());
        BattleUnit rightBattleUnit = prepareBattleUnit(duelForm.getRightUnit(), duelForm.getRightHeroAttack(), duelForm.getRightHeroDefense());

        attackInfo.setFasterUnit(findFaster(leftBattleUnit, rightBattleUnit));
        attackInfo.setSlowerUnit(findSlower(attackInfo, leftBattleUnit, rightBattleUnit));

        attackInfo.setFasterQuantity(findFasterQuantity(attackInfo.getFasterUnit(), leftBattleUnit, duelForm.getLeftQuantity(), duelForm.getRightQuantity()));
        attackInfo.setSlowerQuantity(findSlowerQuantity(attackInfo.getSlowerUnit(), rightBattleUnit, duelForm.getRightQuantity(), duelForm.getLeftQuantity()));
        return attackInfo;
    }

    private void setHerosStatsIfNull(DuelForm duelFormValidation){
        if(duelFormValidation.getLeftHeroAttack() == null)
            duelFormValidation.setLeftHeroAttack(0);
        if(duelFormValidation.getLeftHeroDefense() == null)
            duelFormValidation.setLeftHeroDefense(0);
        if(duelFormValidation.getRightHeroAttack() == null)
            duelFormValidation.setRightHeroAttack(0);
        if(duelFormValidation.getRightHeroDefense() == null)
            duelFormValidation.setRightHeroDefense(0);
    }

    private BattleUnit prepareBattleUnit(Unit unit, int heroAtk, int heroDef){
        BattleUnit battleUnit = new BattleUnit.BattleUnitBuilder()
                .name(unit.getName())
                .basicAtk(unit.getAttack())
                .heroAtk(heroAtk)
                .basicDef(unit.getDefense())
                .heroDef(heroDef)
                .minDmg(unit.getMinDamage())
                .maxDmg(unit.getMaxDamage())
                .speed(unit.getSpeed())
                .hp(unit.getHp())
                .build();
        if (unit.getShots() != null)
            battleUnit.setShots(unit.getShots());
        return battleUnit;
    }

    private void startBattle(AttackInfo attackInfo){
        RoundInfo roundInfo = new RoundInfo(attackInfo.getUserUUID(), 1, 2);
        while (attackInfo.getFasterQuantity() != 0 && attackInfo.getSlowerQuantity() != 0){
            startRound(roundInfo, attackInfo);
            sendRoundMessages(roundInfo);
            setNextRoundInfo(roundInfo);
        }
    }

    private void startRound(RoundInfo roundInfo, AttackInfo attackInfo){
        startAttackFaster(roundInfo, attackInfo);
        startAttackSlower(roundInfo, attackInfo);
        isWinner(roundInfo);
    }

    private void sendRoundMessages(RoundInfo roundInfo){
        RoundInfo snapshotRoundInfo = roundInfo.clone();
        queue.createQueue(snapshotRoundInfo);
    }

    private void setNextRoundInfo(RoundInfo roundInfo){
        roundInfo.setRoundCounter(roundInfo.getRoundCounter() + 1);
        roundInfo.setMessageDelay(roundInfo.getMessageDelay() + 4);
    }

    private void startAttackFaster(RoundInfo roundInfo, AttackInfo attackInfo){

        setRoundInfoAttack(attackInfo, attackInfo.getFasterUnit(), attackInfo.getSlowerUnit(), attackInfo.getFasterQuantity());
        attackInfo.setSlowerQuantity(countLiveUnitsAfterAttack(attackInfo, attackInfo.getFasterQuantity(), attackInfo.getSlowerQuantity()));
        setRoundInfoFaster(roundInfo, attackInfo);

    }

    private void startAttackSlower(RoundInfo roundInfo, AttackInfo attackInfo){

        setRoundInfoAttack(attackInfo, attackInfo.getSlowerUnit(), attackInfo.getFasterUnit(), attackInfo.getSlowerQuantity());
        attackInfo.setFasterQuantity(countLiveUnitsAfterAttack(attackInfo, attackInfo.getSlowerQuantity(), attackInfo.getFasterQuantity()));
        setRoundInfoSlower(roundInfo, attackInfo);

    }

    private void setRoundInfoAttack(AttackInfo attackInfo, BattleUnit atkUnit, BattleUnit defUnit, int attackingQuantity){
        attackInfo.setAtkUnit(atkUnit);
        attackInfo.setDefUnit(defUnit);
        attackInfo.setAttackingUnits(attackingQuantity);
    }

    private void setRoundInfoFaster(RoundInfo roundInfo, AttackInfo attackInfo){
        roundInfo.setFasterDmg(attackInfo.getAtkDmg());
        roundInfo.setFasterLiveUnits(attackInfo.getAttackingUnits());
        roundInfo.setSlowerDeathUnits(attackInfo.getDeathUnits());
        roundInfo.setFasterUnit(attackInfo.getFasterUnit());
        roundInfo.setFasterLastAttackUnits(roundInfo.getFasterLiveUnits());
    }

    private void setRoundInfoSlower(RoundInfo roundInfo, AttackInfo attackInfo){
        roundInfo.setSlowerDmg(attackInfo.getAtkDmg());
        roundInfo.setSlowerLiveUnits(attackInfo.getAttackingUnits());
        roundInfo.setFasterDeathUnits(attackInfo.getDeathUnits());
        roundInfo.setSlowerUnit(attackInfo.getSlowerUnit());
        roundInfo.setFasterLiveUnits(roundInfo.getFasterLiveUnits() - roundInfo.getFasterDeathUnits());
    }

    private BattleUnit findFaster(BattleUnit leftUnit, BattleUnit rightUnit){
        if(leftUnit.getSpeed() > rightUnit.getSpeed())
            return  leftUnit;
        if(leftUnit.getSpeed() == rightUnit.getSpeed()){
            int drawNumb = (random.nextInt(2));
            if(drawNumb == 0)
                return leftUnit;
        }
        return rightUnit;
    }

    private BattleUnit findSlower(AttackInfo attackInfo, BattleUnit leftUnit, BattleUnit rightUnit){
        if(attackInfo.getFasterUnit() == leftUnit){
            return rightUnit;
        }
        return leftUnit;
    }

    private int findFasterQuantity(BattleUnit fasterUnit, BattleUnit leftUnit, int leftQuantity, int rightQuantity){
        if(fasterUnit == leftUnit)
            return leftQuantity;
        return rightQuantity;
    }

    private int findSlowerQuantity(BattleUnit slowerUnit, BattleUnit leftUnit, int leftQuantity, int rightQuantity){
        if(slowerUnit == leftUnit)
            return leftQuantity;
        return rightQuantity;
    }

    private int countLiveUnitsAfterAttack(AttackInfo attackInfo, int attackingQuantity, int defendingQuantity){
        int attackingDmg = generateDmg(attackInfo.getAtkUnit(), attackInfo.getDefUnit(), attackingQuantity);
        setDmg(attackInfo, attackingDmg);
        return countLiveUnits(attackInfo, attackingDmg, defendingQuantity);
    }

    private int countLiveUnits(AttackInfo attackInfo, int dmg, int enemyQuantity){
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

    private void setHpLeft(BattleUnit unit, int dmg, int deathUnits, int enemyQuantity){
        if(enemyQuantity - deathUnits < 1)
            unit.setHpLeft(0);
        else if(deathUnits == 0)
            unit.setHpLeft(unit.getHpLeft() - dmg);
        else
            unit.setHpLeft(unit.getHpLeft() - (dmg - unit.getHp() * deathUnits));
    }

    private void setDmg(AttackInfo roundInfo, int dmg){
        roundInfo.setAtkDmg(dmg);
    }

    private int generateDmg(BattleUnit battleUnit, BattleUnit enemyUnit, int quantity){
        int basicDmg = drawDmg(battleUnit, quantity);
        double unitAtk = countBonusAtkRate(battleUnit, enemyUnit);
        double enemyUnitDef = countBonusDefRate(battleUnit, enemyUnit);
        return  (int) (basicDmg * unitAtk * enemyUnitDef);
    }

    private int drawDmg(BattleUnit battleUnit, int quantity){
        int minDmg = battleUnit.getMinDmg();
        int maxDmg = battleUnit.getMaxDmg();
        return IntStream.range(0, quantity)
                .map(i -> drawSingDmg(minDmg, maxDmg))
                .sum();
    }

    private int drawSingDmg(int minDmg, int maxDmg){
        return random.nextInt((maxDmg - minDmg + 1)) + minDmg;
    }

    private double countBonusAtkRate(BattleUnit unit, BattleUnit enemyUnit){
        //Różnica statystyk ataku i obrony jednostek zwiększa atak za każdy punkt o 5%
        double unitAtk = 1 + (ATK_RATE * ((unit.getBasicAtk() + unit.getHeroAtk()) - (enemyUnit.getBasicDef() + enemyUnit.getHeroDef())));
        return Math.max(1, Math.min(unitAtk, 4));
    }

    private double countBonusDefRate(BattleUnit unit, BattleUnit enemyUnit){
        //Różnica statystyk ataku i obrony jednostek zwiększa obronę za każdy punkt o 2.5%
        double enemyDef = 1 - (DEF_RATE * ((unit.getBasicDef() + unit.getHeroDef()) - (enemyUnit.getBasicAtk() + enemyUnit.getHeroAtk())));
        return Math.min(1, Math.max(enemyDef, 0.7));
    }

    private boolean isWinner(RoundInfo roundInfo){
        if(roundInfo.getSlowerLiveUnits() == 0) {
            roundInfo.setWinnerUnit(roundInfo.getFasterUnit());
            roundInfo.setLoserUnit(roundInfo.getSlowerUnit());
            return true;
        }
        if(roundInfo.getFasterLiveUnits() == 0){

            roundInfo.setWinnerUnit(roundInfo.getSlowerUnit());
            roundInfo.setLoserUnit(roundInfo.getFasterUnit());
            return true;
        }
        return false;
    }
}
