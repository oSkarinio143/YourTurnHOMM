package pl.oskarinio.yourturnhomm.domain.model;

import org.junit.jupiter.api.Test;
import pl.oskarinio.yourturnhomm.domain.model.battle.BattleUnit;
import pl.oskarinio.yourturnhomm.domain.model.battle.RoundInfo;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
class RoundInfoCloneTest {

    @Test
    void clone_correctValues() {
        BattleUnit bu = new BattleUnit.BattleUnitBuilder()
                .name("X")
                .hp(100)
                .build();

        UUID uuid = UUID.randomUUID();
        RoundInfo original = new RoundInfo(uuid, 1, 50);
        original.setFasterUnit(bu);
        original.setFasterDmg(10);
        original.setWinnerUnit(bu);
        original.setWinner(true);

        RoundInfo cloned = original.clone();

        assertThat(cloned).isNotSameAs(original);
        assertThat(cloned.getRoundCounter()).isEqualTo(original.getRoundCounter());
        assertThat(cloned.isWinner()).isEqualTo(original.isWinner());
        assertThat(cloned.getFasterUnit()).isSameAs(original.getFasterUnit());
        assertThat(cloned.getWinnerUnit()).isSameAs(original.getWinnerUnit());

        cloned.setRoundCounter(99);
        assertThat(original.getRoundCounter()).isEqualTo(1);

        cloned.getFasterUnit().setHpLeft(1);
        assertThat(original.getFasterUnit().getHpLeft()).isEqualTo(1);
    }
}