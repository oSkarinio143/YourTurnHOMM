package pl.oskarinio.yourturnhomm.domain.model.battle;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class RoundInfo implements Cloneable{

    @NonNull
    private String userUUID;

    @NonNull
    private int roundCounter;

    @NonNull
    private int MessageDelay;
    private int fasterDmg;
    private int slowerDmg;
    private int fasterDeathUnits;
    private int slowerDeathUnits;
    private int fasterLiveUnits;
    private int slowerLiveUnits;
    private BattleUnit fasterUnit;
    private BattleUnit slowerUnit;
    private BattleUnit winnerUnit;
    private BattleUnit loserUnit;
    private boolean isWinner;
    private int fasterLastAttackUnits;
    private int tempDelay;


    @Override
    public RoundInfo clone() {
        try {
            RoundInfo copy = (RoundInfo) super.clone();
            return copy;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Clone Exception");
        }
    }
}
