package oskarinio143.yourturnhomm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import oskarinio143.yourturnhomm.model.entity.RefreshToken;

import java.time.Instant;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {

    @Modifying
    @Query("DELETE FROM RefreshToken t WHERE t.expirationDate < :date")
    void deleteExpiredToken(@Param("date") Instant date);
}
