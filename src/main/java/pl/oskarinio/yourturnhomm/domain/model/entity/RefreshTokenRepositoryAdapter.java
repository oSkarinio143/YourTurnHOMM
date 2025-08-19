package pl.oskarinio.yourturnhomm.domain.model.entity;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import pl.oskarinio.yourturnhomm.app.user.port.out.RefreshTokenRepositoryPort;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class RefreshTokenRepositoryAdapter implements RefreshTokenRepositoryPort {
    @Override
    public void deleteExpiredToken(Instant date) {

    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends RefreshTokenEntity> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends RefreshTokenEntity> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<RefreshTokenEntity> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Integer> integers) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public RefreshTokenEntity getOne(Integer integer) {
        return null;
    }

    @Override
    public RefreshTokenEntity getById(Integer integer) {
        return null;
    }

    @Override
    public RefreshTokenEntity getReferenceById(Integer integer) {
        return null;
    }

    @Override
    public <S extends RefreshTokenEntity> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends RefreshTokenEntity> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends RefreshTokenEntity> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends RefreshTokenEntity> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends RefreshTokenEntity> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends RefreshTokenEntity> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends RefreshTokenEntity, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends RefreshTokenEntity> S save(S entity) {
        return null;
    }

    @Override
    public <S extends RefreshTokenEntity> List<S> saveAll(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public Optional<RefreshTokenEntity> findById(Integer integer) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Integer integer) {
        return false;
    }

    @Override
    public List<RefreshTokenEntity> findAll() {
        return List.of();
    }

    @Override
    public List<RefreshTokenEntity> findAllById(Iterable<Integer> integers) {
        return List.of();
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Integer integer) {

    }

    @Override
    public void delete(RefreshTokenEntity entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Integer> integers) {

    }

    @Override
    public void deleteAll(Iterable<? extends RefreshTokenEntity> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<RefreshTokenEntity> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<RefreshTokenEntity> findAll(Pageable pageable) {
        return null;
    }
}
