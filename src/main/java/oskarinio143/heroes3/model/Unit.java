package oskarinio143.heroes3.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Unit {

    @Id
    @NonNull
    private String name;
    @NonNull
    private int attack;
    @NonNull
    private int defense;

    private int shots;
    @NonNull
    private int minDamage;
    @NonNull
    private int maxDamage;
    @NonNull
    private int hp;
    @NonNull
    private int hpLeft;
    @NonNull
    private int speed;

    private String description;
    @NonNull
    private String imagePath;
}
