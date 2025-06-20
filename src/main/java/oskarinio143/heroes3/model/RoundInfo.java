package oskarinio143.heroes3.model;

import lombok.Getter;
import lombok.Setter;

import javax.print.DocFlavor;

@Getter
@Setter
public class RoundInfo {
    private String UserUUID;
    private Unit slowerUnit;
    private Unit fasterUnit;
    private int slowerQuantity;
    private int fasterQuantity;
    private int atkDmg;
    private Unit atkUnit;
    private Unit defUnit;
    private Unit winnerUnit;
    private Unit loserUnit;
    private int deathUnits;
}
