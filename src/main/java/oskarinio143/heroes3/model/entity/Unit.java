package oskarinio143.heroes3.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import oskarinio143.heroes3.validation.ValidDamageRange;

@ValidDamageRange
@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Unit {

    @Id
    @NotNull
    private String name;

    @NotNull
    @Min(1)
    private Integer attack;

    @NotNull
    @Min(1)
    private Integer defense;

    @Min(0)
    private Integer shots;

    @NotNull
    @Min(1)
    private Integer minDamage;

    @NotNull
    @Min(1)
    private Integer maxDamage;

    @NotNull
    @Min(1)
    private Integer hp;

    @NotNull
    private Integer hpLeft;

    @NotNull
    @Min(1)
    private Integer speed;

    private String description;

    @NotNull
    private String imagePath;
}
