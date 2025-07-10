package oskarinio143.heroes3.model.servicedto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import oskarinio143.heroes3.model.entity.Unit;

@NoArgsConstructor
@Getter
@Setter
public class DuelInfo {

    private String UserUUID;

    @NotNull
    private Unit leftUnit;

    @NotNull
    private Unit rightUnit;

    @Min(1)
    @Max(99)
    @NotNull
    private Integer leftQuantity;

    @Min(1)
    @Max(99)
    @NotNull
    private Integer rightQuantity;

    @Min(0)
    @Max(99)
    private int leftHeroAttack;

    @Min(0)
    @Max(99)
    private int leftHeroDefense;

    @Min(0)
    @Max(99)
    private int rightHeroAttack;

    @Min(0)
    @Max(99)
    private int rightHeroDefense;
}
