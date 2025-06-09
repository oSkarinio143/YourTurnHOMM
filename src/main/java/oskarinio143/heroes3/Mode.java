package oskarinio143.heroes3;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum Mode {
    DATABASE(1), COMPARISON(2), BATTLE(3);
    private int number;

    Mode(int number){
        this.number = number;
    }
}
