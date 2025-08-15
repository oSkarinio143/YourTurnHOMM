package pl.oskarinio.yourturnhomm.infrastructure.adapter.out;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.oskarinio.yourturnhomm.domain.model.entity.User;

import java.time.Instant;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {

    @Query("SELECT u FROM User u WHERE u.username = :username")
    Optional<User> findByUsername(@Param("username") String username);

    @Query("SELECT u FROM User u WHERE u.username = :username")
    User findByUsernameOrThrow(@Param("username") String username);

    @Modifying
    @Query("UPDATE User u SET u.refreshToken = null WHERE u.refreshToken.expirationDate < :date")
    void removeRefreshTokenRelation(@Param("date") Instant date);
}
