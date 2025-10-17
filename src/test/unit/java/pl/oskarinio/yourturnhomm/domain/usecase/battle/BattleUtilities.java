package pl.oskarinio.yourturnhomm.domain.usecase.battle;

import pl.oskarinio.yourturnhomm.domain.model.battle.BattleUnit;
import pl.oskarinio.yourturnhomm.domain.model.battle.RoundInfo;
import pl.oskarinio.yourturnhomm.domain.model.battle.Unit;
import pl.oskarinio.yourturnhomm.domain.model.form.DuelForm;

import java.util.UUID;
class BattleUtilities {

    private static final UUID USER_UUID = UUID.randomUUID();
    private static final String UNIT_NAME = "testUnit";
    private static final String WINNER_UNIT_NAME = "winnerUnit";
    private static final String LOSER_UNIT_NAME = "loserUnit";
    

    static UUID getUserUUID(){
        return USER_UUID;
    }

    static String getUnitName(){
        return UNIT_NAME;
    }

    static RoundInfo getRoundInfo(){
        BattleUnit winnerUnit = getTestBattleUnit(WINNER_UNIT_NAME, 10, 10, 10, 10, 50, 50, 20, 100,0);
        BattleUnit losingUnit = getTestBattleUnit(LOSER_UNIT_NAME, 1,1,1,1,1,1,3, 5,0);

        RoundInfo roundInfo = new RoundInfo(USER_UUID, 1, 1);
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
        duelForm.setUserUUID(USER_UUID);
        duelForm.setLeftUnit(getTestUnit(WINNER_UNIT_NAME,10,10,10,50,100,20,"Strong"));
        duelForm.setRightUnit(getTestUnit(LOSER_UNIT_NAME,1,1,1,1,5,3, "Weak"));
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
        duelForm.setUserUUID(USER_UUID);
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
        duelForm.setUserUUID(USER_UUID);
        duelForm.setLeftUnit(getTestUnit(WINNER_UNIT_NAME,10,10,50,50,100,20,"Strong"));
        duelForm.setRightUnit(getTestUnit(LOSER_UNIT_NAME,1,1,1,1,5,3, "Weak"));
        duelForm.setLeftQuantity(5);
        duelForm.setRightQuantity(10);
        duelForm.setLeftHeroAttack(null);
        duelForm.setRightHeroAttack(null);
        duelForm.setLeftHeroDefense(null);
        duelForm.setRightHeroDefense(null);
        return duelForm;
    }

    static Unit getTestUnit(){
        return new Unit(UNIT_NAME, 10, 10, 10, 10, 10, 10, "desc");
    }

    private static Unit getTestUnit(String name, int attack, int defense, int minDamage, int maxDamage, int hp, int speed, String description){
        return new Unit(name, attack, defense, minDamage, maxDamage, hp, speed, description);
    }

    private static BattleUnit getTestBattleUnit(String name, int attack, int heroAttack, int defense, int heroDefense, int minDamage, int maxDamage, int speed, int hp, int shots){
        return new BattleUnit(name, attack, heroAttack, defense, heroDefense, minDamage, maxDamage, speed, hp, hp, shots);
    }
}
