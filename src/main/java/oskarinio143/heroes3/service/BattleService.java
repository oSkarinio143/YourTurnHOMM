package oskarinio143.heroes3.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import oskarinio143.heroes3.model.entity.Unit;
import oskarinio143.heroes3.model.servicedto.AttackInfo;
import oskarinio143.heroes3.model.servicedto.BattleUnit;
import oskarinio143.heroes3.model.servicedto.DuelInfo;
import oskarinio143.heroes3.model.servicedto.RoundInfo;

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
    public void prepareBattle(DuelInfo duelInfo){
        AttackInfo attackInfo = prepareAttackInfo(duelInfo);
        startBattle(attackInfo);
    }

    public AttackInfo prepareAttackInfo(DuelInfo duelInfo){
        AttackInfo attackInfo = new AttackInfo(duelInfo.getUserUUID());

        BattleUnit leftBattleUnit = prepareBattleUnit(duelInfo.getLeftUnit(), duelInfo.getLeftHeroAttack(), duelInfo.getLeftHeroDefense());
        BattleUnit rightBattleUnit = prepareBattleUnit(duelInfo.getRightUnit(), duelInfo.getRightHeroAttack(), duelInfo.getRightHeroDefense());

        attackInfo.setFasterUnit(findFaster(leftBattleUnit, rightBattleUnit));
        attackInfo.setSlowerUnit(findSlower(attackInfo, leftBattleUnit, rightBattleUnit));

        attackInfo.setFasterQuantity(findFasterQuantity(attackInfo.getFasterUnit(), leftBattleUnit, duelInfo.getLeftQuantity(), duelInfo.getRightQuantity()));
        attackInfo.setSlowerQuantity(findSlowerQuantity(attackInfo.getSlowerUnit(), rightBattleUnit, duelInfo.getRightQuantity(), duelInfo.getLeftQuantity()));
        return attackInfo;
    }

    public BattleUnit prepareBattleUnit(Unit unit, int heroAtk, int heroDef){
        return new BattleUnit.BattleUnitBuilder()
                .name(unit.getName())
                .basicAtk(unit.getAttack())
                .heroAtk(heroAtk)
                .basicDef(unit.getDefense())
                .heroDef(heroDef)
                .minDmg(unit.getMinDamage())
                .maxDmg(unit.getMaxDamage())
                .speed(unit.getSpeed())
                .hp(unit.getHp())
                .leftHp(unit.getHp())
                .shoots(unit.getShots())
                .build();
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

    public void setRoundInfoAttack(AttackInfo attackInfo, BattleUnit atkUnit, BattleUnit defUnit, int attackingQuantity){
        attackInfo.setAtkUnit(atkUnit);
        attackInfo.setDefUnit(defUnit);
        attackInfo.setAttackingUnits(attackingQuantity);
    }

    public void setRoundInfoFaster(RoundInfo roundInfo, AttackInfo attackInfo){
        roundInfo.setFasterDmg(attackInfo.getAtkDmg());
        roundInfo.setFasterLiveUnits(attackInfo.getAttackingUnits());
        roundInfo.setSlowerDeathUnits(attackInfo.getDeathUnits());
        roundInfo.setFasterUnit(attackInfo.getFasterUnit());
        roundInfo.setFasterLastAttackUnits(roundInfo.getFasterLiveUnits());
    }

    public void setRoundInfoSlower(RoundInfo roundInfo, AttackInfo attackInfo){
        roundInfo.setSlowerDmg(attackInfo.getAtkDmg());
        roundInfo.setSlowerLiveUnits(attackInfo.getAttackingUnits());
        roundInfo.setFasterDeathUnits(attackInfo.getDeathUnits());
        roundInfo.setSlowerUnit(attackInfo.getSlowerUnit());
        roundInfo.setFasterLiveUnits(roundInfo.getFasterLiveUnits() - roundInfo.getFasterDeathUnits());
    }

    public BattleUnit findFaster(BattleUnit leftUnit, BattleUnit rightUnit){
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

    public BattleUnit findSlower(AttackInfo attackInfo, BattleUnit leftUnit, BattleUnit rightUnit){
        if(attackInfo.getFasterUnit() == leftUnit){
            return rightUnit;
        }
        return leftUnit;
    }

    public int findFasterQuantity(BattleUnit fasterUnit, BattleUnit leftUnit, int leftQuantity, int rightQuantity){
        if(fasterUnit == leftUnit)
            return leftQuantity;
        return rightQuantity;
    }

    public int findSlowerQuantity(BattleUnit slowerUnit, BattleUnit leftUnit, int leftQuantity, int rightQuantity){
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

    public void setHpLeft(BattleUnit unit, int dmg, int deathUnits, int enemyQuantity){
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

    public int generateDmg(BattleUnit battleUnit, BattleUnit enemyUnit, int quantity){
        int basicDmg = drawDmg(battleUnit, quantity);
        double unitAtk = countBonusAtkRate(battleUnit, enemyUnit);
        double enemyUnitDef = countBonusDefRate(battleUnit, enemyUnit);
        return  (int) (basicDmg * unitAtk * enemyUnitDef);
    }

    public int drawDmg(BattleUnit battleUnit, int quantity){
        int minDmg = battleUnit.getMinDmg();
        int maxDmg = battleUnit.getMaxDmg();
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

    public double countBonusAtkRate(BattleUnit unit, BattleUnit enemyUnit){
        //Różnica statystyk ataku i obrony jednostek zwiększa atak za każdy punkt o 5%
        double unitAtk = 1 + (ATK_RATE * ((unit.getBasicAtk() + unit.getHeroAtk()) - (enemyUnit.getBasicDef() + enemyUnit.getHeroDef())));
        return Math.max(1, Math.min(unitAtk, 4));
    }

    public double countBonusDefRate(BattleUnit unit, BattleUnit enemyUnit){
        //Różnica statystyk ataku i obrony jednostek zwiększa obronę za każdy punkt o 2.5%
        double enemyDef = 1 - (DEF_RATE * ((unit.getBasicDef() + unit.getHeroDef()) - (enemyUnit.getBasicAtk() + enemyUnit.getHeroAtk())));
        return Math.min(1, Math.max(enemyDef, 0.7));
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
