package oskarinio143.heroes3;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoundInfo {
    private Unit slowerUnit;
    private Unit fasterUnit;
    private int slowerQuantity;
    private int fasterQuantity;
    private int slowerDmg;
    private int fasterDmg;
    private Unit atkUnit;
    private Unit defUnit;
    private Unit winnerUnit;
    private Unit loserUnit;
}
