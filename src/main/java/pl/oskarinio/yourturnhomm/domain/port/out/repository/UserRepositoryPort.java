package pl.oskarinio.yourturnhomm.domain.port.out.repository;

import pl.oskarinio.yourturnhomm.domain.model.user.User;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface UserRepositoryPort{
    Optional<User> findByUsername(String username);
    void removeRefreshTokenRelation(Instant date);
    Long count();
    void save(User user);
    void delete(User user);
    List<User> findAll();
}
