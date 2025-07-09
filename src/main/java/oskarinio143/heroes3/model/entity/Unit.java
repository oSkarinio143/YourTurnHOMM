package oskarinio143.heroes3.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import oskarinio143.heroes3.validation.ValidDamageRange;

@ValidDamageRange
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Unit {

    @Id
    @NotNull
    private String name;

    @NotNull
    @Min(1)
    private int attack;

    @NotNull
    @Min(1)
    private int defense;

    @Min(0)
    private int shots;

    @NotNull
    @Min(1)
    private int minDamage;

    @NotNull
    @Min(1)
    private int maxDamage;

    @NotNull
    @Min(1)
    private int hp;

    private int hpLeft;

    @NotNull
    @Min(1)
    private int speed;

    private String description;

    private String imagePath;

    public Unit(String name, int attack, int defense, int shots, int minDamage, int maxDamage, int hp, int speed, String imagePath){
        this.name = name;
        this.attack = attack;
        this.defense = defense;
        this.shots = shots;
        this.minDamage = minDamage;
        this.maxDamage = maxDamage;
        this.hp = hp;
        this.hpLeft = hp;
        this.speed = speed;
        this.imagePath = imagePath;
    }

    public Unit(String name, int attack, int defense, int shots, int minDamage, int maxDamage, int speed, int hp, String description, String imagePath){
        this.name = name;
        this.attack = attack;
        this.defense = defense;
        this.shots = shots;
        this.minDamage = minDamage;
        this.maxDamage = maxDamage;
        this.speed = speed;
        this.hp = hp;
        this.hpLeft = hp;
        this.description = description;
        this.imagePath = imagePath;
    }
}
