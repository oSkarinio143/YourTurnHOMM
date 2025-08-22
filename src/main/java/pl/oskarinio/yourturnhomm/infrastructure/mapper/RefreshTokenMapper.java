package pl.oskarinio.yourturnhomm.infrastructure.mapper;

import pl.oskarinio.yourturnhomm.domain.model.user.RefreshToken;
import pl.oskarinio.yourturnhomm.infrastructure.adapter.in.model.entity.RefreshTokenEntity;

public class RefreshTokenMapper {
    public static RefreshToken toDomain(RefreshTokenEntity refreshTokenEntity){
        RefreshToken refreshToken = new RefreshToken(refreshTokenEntity.getTokenHash(),
                refreshTokenEntity.getCreationDate(),
                refreshTokenEntity.getExpirationDate());
        refreshToken.setId(refreshTokenEntity.getId());
        return refreshToken;
    }

    public static RefreshTokenEntity toEntity(RefreshToken refreshToken){
        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity(refreshToken.getTokenHash(),
                refreshToken.getCreationDate(),
                refreshToken.getExpirationDate());
        refreshTokenEntity.setId(refreshToken.getId());
        return refreshTokenEntity;
    }
}
