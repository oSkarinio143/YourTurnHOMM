package oskarinio143.heroes3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import oskarinio143.heroes3.model.entity.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
}
