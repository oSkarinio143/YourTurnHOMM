package pl.oskarinio.yourturnhomm.infrastructure.usecase.database.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.oskarinio.yourturnhomm.infrastructure.db.entity.UserEntity;

import java.time.Instant;
import java.util.Optional;

public interface UserRepositoryUseCase extends JpaRepository<UserEntity, Integer>, JpaSpecificationExecutor<UserEntity> {
    Optional<UserEntity> findByUsername(@Param("username") String username);

    @Modifying
    @Transactional
    @Query("UPDATE UserEntity u SET u.refreshToken = null WHERE u.refreshToken.expirationDate < :expirationDate")
    void removeRefreshTokenRelation(@Param("expirationDate") Instant expirationDate);
}
