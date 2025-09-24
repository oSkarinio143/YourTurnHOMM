package pl.oskarinio.yourturnhomm.infrastructure.adapter.out;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.oskarinio.yourturnhomm.domain.model.user.User;
import pl.oskarinio.yourturnhomm.domain.port.out.UserRepository;
import pl.oskarinio.yourturnhomm.infrastructure.db.entity.UserEntity;
import pl.oskarinio.yourturnhomm.infrastructure.db.mapper.UserMapper;
import pl.oskarinio.yourturnhomm.infrastructure.usecase.database.repository.UserRepositoryUseCase;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Slf4j
@Service
public class UserRepositoryService implements UserRepository {
    private final UserRepositoryUseCase userRepositoryUseCase;

    public UserRepositoryService(UserRepositoryUseCase userRepositoryUseCaseJpa) {
        this.userRepositoryUseCase = userRepositoryUseCaseJpa;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        log.debug("Szukam w bazie uzytkownika. Nazwa = {}", username);
        return userRepositoryUseCase.findByUsername(username).map(UserMapper::toDomain);
    }

    @Override
    public void removeRefreshTokenRelation(Instant date) {
        log.debug("Usuwam relacje wygasnietych refreshTokenow z Uzytkownikami");
        userRepositoryUseCase.removeRefreshTokenRelation(date);
    }

    @Override
    public Long count() {
        long amountUser = userRepositoryUseCase.count();
        log.debug("Licze uzytkownikow. Wynik = {}", amountUser);
        return amountUser;
    }

    @Override
    public void save(User user) {
        log.debug("Zapisuje / Aktualizuje uzytkownika w bazie. Nazwa = {}", user.getUsername());
        userRepositoryUseCase.save(UserMapper.toEntity(user));
    }

    @Override
    public void delete(User user) {
        log.debug("Usuwam uzytkownika z bazy. Nazwa = {}", user.getUsername());
        userRepositoryUseCase.delete(UserMapper.toEntity(user));
    }

    @Override
    public List<User> findAll() {
        log.trace("Zwracam wszystkich uzytkownikow z bazy");
        List<UserEntity> userEntities = userRepositoryUseCase.findAll();
        Function<UserEntity, User> userMapper = UserMapper::toDomain;
        return userEntities.stream().map(userMapper).toList();
    }
}
