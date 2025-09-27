package pl.oskarinio.yourturnhomm.domain.model;

import org.junit.jupiter.api.Test;
import pl.oskarinio.yourturnhomm.domain.model.battle.RoundInfo;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RoundInfoConstructorTest {
    @Test
    void constructorThrowsUserUUIDNull() {
        assertThrows(NullPointerException.class, () -> new RoundInfo(null, 1, 10));
    }

    @Test
    void constructorAcceptsBasicValues() {
        RoundInfo r = new RoundInfo("u-1", 1, 15);
        assertThat(r.getUserUUID()).isEqualTo("u-1");
        assertThat(r.getRoundCounter()).isEqualTo(1);
        assertThat(r.getMessageDelay()).isEqualTo(15);
    }
}



