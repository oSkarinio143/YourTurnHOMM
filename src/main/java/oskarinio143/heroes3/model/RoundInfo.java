package oskarinio143.heroes3.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Setter
@Getter
public class RoundInfo implements Cloneable{
    @Override
    public RoundInfo clone() {
        try {
            RoundInfo copy = (RoundInfo) super.clone();
            return copy;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Clone powinien być wspierany", e);
        }
    }

    private String userUUID;
    private int roundCounter;
    private int fasterDmg;
    private int slowerDmg;
    private int fasterDeathUnits;
    private int slowerDeathUnits;
    private int fasterLiveUnits;
    private int slowerLiveUnits;
    private Unit fasterUnit;
    private Unit slowerUnit;
    private Unit winnerUnit;
    private Unit loserUnit;
    private boolean isWinner;
}
