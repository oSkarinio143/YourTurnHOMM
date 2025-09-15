package pl.oskarinio.yourturnhomm.infrastructure.db.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import pl.oskarinio.yourturnhomm.infrastructure.security.validation.damagerange.ValidDamageRange;

@Entity
@ValidDamageRange
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "unit")
public class UnitEntity {

    @Id
    @NonNull
    @NotNull
    private String name;

    @NotNull
    @NonNull
    @Min(1)
    @Max(99)
    private Integer attack;

    @NotNull
    @NonNull
    @Min(1)
    @Max(99)
    private Integer defense;

    @Min(1)
    private Integer shots;

    @NotNull
    @NonNull
    @Min(1)
    @Max(99)
    private Integer minDamage;

    @NotNull
    @NonNull
    @Min(1)
    @Max(99)
    private Integer maxDamage;

    @NotNull
    @NonNull
    @Min(1)
    @Max(9999)
    private Integer hp;

    @NotNull
    @NonNull
    @Min(1)
    @Max(30)
    private Integer speed;

    @NonNull
    private String description;

    @NonNull
    private String imagePath;
}
