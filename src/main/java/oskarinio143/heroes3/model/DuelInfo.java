package oskarinio143.heroes3.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DuelInfo {
    private Unit leftUnit;
    private Unit rightUnit;
    private int leftQuantity;
    private int rightQuantity;
    private int leftHeroAttack;
    private int leftHeroDefense;
    private int rightHeroAttack;
    private int rightHeroDefense;
}
