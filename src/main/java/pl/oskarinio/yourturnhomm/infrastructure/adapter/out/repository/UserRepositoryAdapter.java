package pl.oskarinio.yourturnhomm.infrastructure.adapter.out.repository;

import org.springframework.stereotype.Component;
import pl.oskarinio.yourturnhomm.domain.model.user.User;
import pl.oskarinio.yourturnhomm.domain.port.out.repository.UserRepositoryPort;
import pl.oskarinio.yourturnhomm.infrastructure.db.entity.UserEntity;
import pl.oskarinio.yourturnhomm.infrastructure.db.mapper.UserMapper;
import pl.oskarinio.yourturnhomm.infrastructure.db.repository.UserRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Component
public class UserRepositoryAdapter implements UserRepositoryPort {
    private UserRepository userRepository;

    public UserRepositoryAdapter(UserRepository userRepositoryJpa) {
        this.userRepository = userRepositoryJpa;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username).map(UserMapper::toDomain);
    }

    @Override
    public void removeRefreshTokenRelation(Instant date) {
        userRepository.removeRefreshTokenRelation(date);
    }

    @Override
    public Long count() {
        return userRepository.count();
    }

    @Override
    public void save(User user) {
        userRepository.save(UserMapper.toEntity(user));
    }

    @Override
    public void delete(User user) {
        userRepository.delete(UserMapper.toEntity(user));
    }

    @Override
    public List<User> findAll() {
        List<UserEntity> userEntities = userRepository.findAll();
        Function<UserEntity, User> userMapper = UserMapper::toDomain;
        return userEntities.stream().map(userMapper).toList();
    }
}
