package pl.oskarinio.yourturnhomm.domain.model;

import org.junit.jupiter.api.Test;
import pl.oskarinio.yourturnhomm.domain.model.battle.BattleUnit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class BattleUnitBuilderConstructorTest {
    @Test
    void builderSetsAllFieldsCorrectly() {
        BattleUnit unit = new BattleUnit.BattleUnitBuilder()
                .name("Archer")
                .basicAtk(5)
                .heroAtk(3)
                .basicDef(2)
                .heroDef(1)
                .minDmg(1)
                .maxDmg(2)
                .speed(4)
                .hp(10)
                .shoots(3)
                .build();

        assertThat(unit.getName()).isEqualTo("Archer");
        assertThat(unit.getBasicAtk()).isEqualTo(5);
        assertThat(unit.getHpLeft()).isEqualTo(10);
    }
}
