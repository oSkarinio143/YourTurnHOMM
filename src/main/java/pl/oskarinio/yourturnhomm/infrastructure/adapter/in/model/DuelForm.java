package pl.oskarinio.yourturnhomm.infrastructure.adapter.in.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.oskarinio.yourturnhomm.domain.model.entity.Unit;

@NoArgsConstructor
@Getter
@Setter
public class DuelForm {

    private String userUUID;

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
    private Integer leftHeroAttack;

    @Min(0)
    @Max(99)
    private Integer leftHeroDefense;

    @Min(0)
    @Max(99)
    private Integer rightHeroAttack;

    @Min(0)
    @Max(99)
    private Integer rightHeroDefense;
}
