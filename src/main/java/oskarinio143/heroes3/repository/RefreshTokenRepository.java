package oskarinio143.heroes3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import oskarinio143.heroes3.model.entity.RefreshToken;

import java.time.Instant;
import java.util.Date;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
    void deleteById(@Param("id") int id);

    @Modifying
    @Query("DELETE FROM RefreshToken t WHERE t.expirationDate < :date")
    void deleteExpiredToken(@Param("date") Instant date);
}
