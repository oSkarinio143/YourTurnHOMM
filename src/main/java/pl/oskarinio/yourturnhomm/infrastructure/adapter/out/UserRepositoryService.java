package pl.oskarinio.yourturnhomm.infrastructure.adapter.out;

import org.springframework.stereotype.Component;
import pl.oskarinio.yourturnhomm.domain.model.user.User;
import pl.oskarinio.yourturnhomm.domain.port.out.UserRepository;
import pl.oskarinio.yourturnhomm.infrastructure.db.entity.UserEntity;
import pl.oskarinio.yourturnhomm.infrastructure.db.mapper.UserMapper;
import pl.oskarinio.yourturnhomm.infrastructure.usecase.database.UserRepositoryUseCase;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Component
public class UserRepositoryService implements UserRepository {
    private UserRepositoryUseCase userRepositoryUseCase;

    public UserRepositoryService(UserRepositoryUseCase userRepositoryUseCaseJpa) {
        this.userRepositoryUseCase = userRepositoryUseCaseJpa;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepositoryUseCase.findByUsername(username).map(UserMapper::toDomain);
    }

    @Override
    public void removeRefreshTokenRelation(Instant date) {
        userRepositoryUseCase.removeRefreshTokenRelation(date);
    }

    @Override
    public Long count() {
        return userRepositoryUseCase.count();
    }

    @Override
    public void save(User user) {
        userRepositoryUseCase.save(UserMapper.toEntity(user));
    }

    @Override
    public void delete(User user) {
        userRepositoryUseCase.delete(UserMapper.toEntity(user));
    }

    @Override
    public List<User> findAll() {
        List<UserEntity> userEntities = userRepositoryUseCase.findAll();
        Function<UserEntity, User> userMapper = UserMapper::toDomain;
        return userEntities.stream().map(userMapper).toList();
    }
}
