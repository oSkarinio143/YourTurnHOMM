package pl.oskarinio.yourturnhomm.domain.usecase.battle;

import lombok.Getter;
import pl.oskarinio.yourturnhomm.domain.model.battle.BattleUnit;
import pl.oskarinio.yourturnhomm.domain.model.battle.RoundInfo;
import pl.oskarinio.yourturnhomm.domain.model.battle.Unit;
import pl.oskarinio.yourturnhomm.domain.model.form.DuelForm;

import java.util.UUID;
@Getter
class BattleUtilities {

    private static final UUID TEST_USERUUID = UUID.randomUUID();
    private static final String TEST_UNIT = "testUnit";

    static UUID getUserUUID(){
        return TEST_USERUUID;
    }

    static String getUnitName(){
        return TEST_UNIT;
    }

    static RoundInfo getRoundInfo(){
        BattleUnit winnerUnit = getTestBattleUnit("winnerUnit", 10, 10, 10, 10, 50, 50, 20, 100,0);
        BattleUnit losingUnit = getTestBattleUnit("loserUnit", 1,1,1,1,1,1,3, 5,0);

        RoundInfo roundInfo = new RoundInfo(TEST_USERUUID, 1, 1);
        roundInfo.setFasterDmg(200);
        roundInfo.setSlowerDmg(100);
        roundInfo.setFasterDeathUnits(2);
        roundInfo.setSlowerDeathUnits(1);
        roundInfo.setFasterLiveUnits(20);
        roundInfo.setSlowerLiveUnits(10);
        roundInfo.setFasterUnit(winnerUnit);
        roundInfo.setSlowerUnit(losingUnit);
        roundInfo.setWinnerUnit(winnerUnit);
        roundInfo.setLoserUnit(losingUnit);
        roundInfo.setWinner(true);
        roundInfo.setFasterLastAttackUnits(30);
        roundInfo.setTempDelay(1);
        return roundInfo;
    }

    static DuelForm getDuelFormLeftUnitFasterWinner(){
        DuelForm duelForm = new DuelForm();
        duelForm.setUserUUID(TEST_USERUUID);
        duelForm.setLeftUnit(getTestUnit("winningUnit",10,10,10,50,100,20,"Strong"));
        duelForm.setRightUnit(getTestUnit("losingUnit",1,1,1,1,5,3, "Weak"));
        duelForm.setLeftQuantity(5);
        duelForm.setRightQuantity(10);
        duelForm.setLeftHeroAttack(5);
        duelForm.setRightHeroAttack(15);
        duelForm.setLeftHeroDefense(8);
        duelForm.setRightHeroDefense(18);
        return duelForm;
    }

    static DuelForm getDuelFormWithoutSetSides(){
        DuelForm duelForm = new DuelForm();
        duelForm.setUserUUID(TEST_USERUUID);
        duelForm.setLeftQuantity(5);
        duelForm.setRightQuantity(10);
        duelForm.setLeftHeroAttack(5);
        duelForm.setRightHeroAttack(15);
        duelForm.setLeftHeroDefense(8);
        duelForm.setRightHeroDefense(18);
        return duelForm;
    }


    static DuelForm getDuelFormWithHeroZeroStats(){
        DuelForm duelForm = new DuelForm();
        duelForm.setUserUUID(TEST_USERUUID);
        duelForm.setLeftUnit(getTestUnit("winningUnit",10,10,50,50,100,20,"Strong"));
        duelForm.setRightUnit(getTestUnit("losingUnit",1,1,1,1,5,3, "Weak"));
        duelForm.setLeftQuantity(5);
        duelForm.setRightQuantity(10);
        duelForm.setLeftHeroAttack(null);
        duelForm.setRightHeroAttack(null);
        duelForm.setLeftHeroDefense(null);
        duelForm.setRightHeroDefense(null);
        return duelForm;
    }

    static Unit getTestUnit(String name){
        return new Unit(name, 10, 10, 10, 10, 10, 10, TEST_UNIT);
    }

    private static Unit getTestUnit(String name, int attack, int defense, int minDamage, int maxDamage, int hp, int speed, String description){
        return new Unit(name, attack, defense, minDamage, maxDamage, hp, speed, description);
    }

    private static BattleUnit getTestBattleUnit(String name, int attack, int heroAttack, int defense, int heroDefense, int minDamage, int maxDamage, int speed, int hp, int shots){
        return new BattleUnit(name, attack, heroAttack, defense, heroDefense, minDamage, maxDamage, speed, hp, hp, shots);
    }
}
