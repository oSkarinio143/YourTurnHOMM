package oskarinio143.heroes3.model;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DuelInfo {

    private String UserUUID;
    private String name;
    private String side;
    private Unit leftUnit;
    private Unit rightUnit;
    @Min(1)
    private int leftQuantity;
    @Min(1)
    private int rightQuantity;
    private int leftHeroAttack;
    private int leftHeroDefense;
    private int rightHeroAttack;
    private int rightHeroDefense;
}
