package oskarinio143.heroes3.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttackInfo {
    private String UserUUID;
    private Unit atkUnit;
    private Unit defUnit;
    private Unit fasterUnit;
    private Unit slowerUnit;
    private int attackingUnits;
    private int deathUnits;
    private int fasterQuantity;
    private int slowerQuantity;
    private int atkDmg;
    private Unit winnerUnit;
    private Unit loserUnit;
}
