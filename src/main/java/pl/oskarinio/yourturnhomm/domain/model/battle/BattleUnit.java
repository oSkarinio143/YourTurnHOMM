package pl.oskarinio.yourturnhomm.domain.model.battle;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter

public class BattleUnit {
    private String name;
    private int basicAtk;
    private int heroAtk;
    private int basicDef;
    private int heroDef;
    private int minDmg;
    private int maxDmg;
    private int speed;
    private int hp;
    private int hpLeft;
    private int shots;

    private BattleUnit(BattleUnitBuilder battleUnit){
        this.name = battleUnit.name;
        this.basicAtk = battleUnit.basicAtk;
        this.heroAtk = battleUnit.heroAtk;
        this.basicDef = battleUnit.basicDef;
        this.heroDef = battleUnit.heroDef;
        this.minDmg = battleUnit.minDmg;
        this.maxDmg = battleUnit.maxDmg;
        this.speed = battleUnit.speed;
        this.hp = battleUnit.hp;
        this.hpLeft = battleUnit.hp;
        this.shots = battleUnit.shots;
    }

    public static class BattleUnitBuilder {
        private String name;
        private int basicAtk;
        private int heroAtk;
        private int basicDef;
        private int heroDef;
        private int minDmg;
        private int maxDmg;
        private int speed;
        private int hp;
        private int shots;

        public BattleUnitBuilder name(String name){
            this.name = name;
            return this;
        }

        public BattleUnitBuilder basicAtk(int basicAtk) {
            this.basicAtk = basicAtk;
            return this;
        }

        public BattleUnitBuilder heroAtk(int heroAtk) {
            this.heroAtk = heroAtk;
            return this;
        }

        public BattleUnitBuilder basicDef(int basicDef) {
            this.basicDef = basicDef;
            return this;
        }

        public BattleUnitBuilder heroDef(int heroDef) {
            this.heroDef = heroDef;
            return this;
        }

        public BattleUnitBuilder minDmg(int minDmg){
            this.minDmg = minDmg;
            return this;
        }

        public BattleUnitBuilder maxDmg(int maxDmg){
            this.maxDmg = maxDmg;
            return this;
        }

        public BattleUnitBuilder speed(int speed) {
            this.speed = speed;
            return this;
        }

        public BattleUnitBuilder hp(int hp) {
            this.hp = hp;
            return this;
        }

        public BattleUnitBuilder shoots(int shots) {
            this.shots = shots;
            return this;
        }

        public BattleUnit build(){
            return new BattleUnit(this);
        }
    }
}
