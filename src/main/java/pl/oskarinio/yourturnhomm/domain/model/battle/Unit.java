package pl.oskarinio.yourturnhomm.domain.model.battle;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class Unit {
    @NonNull
    private String name;
    @NonNull
    private Integer attack;
    @NonNull
    private Integer defense;
    private Integer shots;
    @NonNull
    private Integer minDamage;
    @NonNull
    private Integer maxDamage;
    @NonNull
    private Integer hp;
    @NonNull
    private Integer speed;
    @NonNull
    private String description;
    @NonNull
    private String imagePath;

}
