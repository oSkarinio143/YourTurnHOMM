package pl.oskarinio.yourturnhomm.domain.service.battle;

import lombok.Getter;
import pl.oskarinio.yourturnhomm.domain.model.battle.BattleUnit;
import pl.oskarinio.yourturnhomm.domain.model.battle.RoundInfo;

import java.util.UUID;

class BattleTestHelper {

    private static final UUID TEST_USERUUID = UUID.randomUUID();

    static UUID getUSERUUID(){
        return TEST_USERUUID;
    }

    static RoundInfo getRoundInfo(){
        RoundInfo roundInfo = new RoundInfo(TEST_USERUUID, 1, 1);
        roundInfo.setFasterDmg(200);
        roundInfo.setSlowerDmg(100);
        roundInfo.setFasterDeathUnits(2);
        roundInfo.setSlowerDeathUnits(1);
        roundInfo.setFasterLiveUnits(20);
        roundInfo.setSlowerLiveUnits(10);
        roundInfo.setFasterUnit(getTestUnit(10));
        roundInfo.setSlowerUnit(getTestUnit(1));
        roundInfo.setWinnerUnit(getTestUnit());
        roundInfo.setLoserUnit(getTestUnit());
        roundInfo.setWinner(true);
        roundInfo.setFasterLastAttackUnits(30);
        roundInfo.setTempDelay(1);
        return roundInfo;
    }

    private static BattleUnit getTestUnit(){
        return new BattleUnit("testUnit", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    }

    private static BattleUnit getTestUnit(int speed){
        return new BattleUnit("testUnit", 1, 2, 3, 4, 5, 6, speed, 8, 9, 10);
    }
}
