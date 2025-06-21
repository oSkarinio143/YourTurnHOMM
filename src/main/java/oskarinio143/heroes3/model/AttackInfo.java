package oskarinio143.heroes3.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class AttackInfo {
    @NonNull
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
