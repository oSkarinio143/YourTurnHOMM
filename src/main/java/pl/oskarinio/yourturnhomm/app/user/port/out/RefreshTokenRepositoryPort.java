package pl.oskarinio.yourturnhomm.app.user.port.out;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.oskarinio.yourturnhomm.domain.model.entity.RefreshTokenEntity;

import java.time.Instant;

public interface RefreshTokenRepositoryPort extends JpaRepository<RefreshTokenEntity, Integer> {
    void deleteExpiredToken(Instant date);
}
