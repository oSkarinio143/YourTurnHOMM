package pl.oskarinio.yourturnhomm.infrastructure.adapter.out.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.oskarinio.yourturnhomm.infrastructure.adapter.in.model.entity.UserEntity;

import java.time.Instant;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer>, JpaSpecificationExecutor<UserEntity> {
    Optional<UserEntity> findByUsername(@Param("username") String username);

    @Modifying
    @Query("UPDATE UserEntity u SET u.refreshToken = null WHERE u.refreshToken.expirationDate < :date")
    void removeRefreshTokenRelation(@Param("date") Instant date);
}
