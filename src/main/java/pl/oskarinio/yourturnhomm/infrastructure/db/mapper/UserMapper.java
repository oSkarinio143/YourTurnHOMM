package pl.oskarinio.yourturnhomm.infrastructure.db.mapper;

import pl.oskarinio.yourturnhomm.domain.model.user.User;
import pl.oskarinio.yourturnhomm.infrastructure.db.entity.UserEntity;

public class UserMapper {
    private UserMapper(){}

    public static User toDomain(UserEntity userEntity){
        User user = new User(userEntity.getUsername(), userEntity.getPassword(), userEntity.getRegistrationDate());
        user.setId(userEntity.getId());
        user.setRoles(userEntity.getRoles());
        if(userEntity.getRefreshToken() != null) {
            user.setRefreshToken(RefreshTokenMapper.toDomain(userEntity.getRefreshToken()));
        }
        return user;
    }

    public static UserEntity toEntity(User user){
        UserEntity userEntity = new UserEntity(user.getUsername(), user.getPassword(), user.getRegistrationDate());
        userEntity.setId(user.getId());
        if(user.getRefreshToken() != null) {
            userEntity.setRefreshToken(RefreshTokenMapper.toEntity(user.getRefreshToken()));
        }
        if(!user.getRoles().isEmpty())
            userEntity.setRoles(user.getRoles());
        return userEntity;
    }
}
