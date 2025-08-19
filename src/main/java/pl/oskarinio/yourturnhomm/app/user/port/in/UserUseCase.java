package pl.oskarinio.yourturnhomm.app.user.port.in;

import org.springframework.validation.ObjectError;
import pl.oskarinio.yourturnhomm.domain.model.user.UserServiceData;
import pl.oskarinio.yourturnhomm.domain.model.entity.RefreshTokenEntity;
import pl.oskarinio.yourturnhomm.domain.model.entity.User;

import java.util.List;

public interface UserUseCase {
    void generateAndSetTokens(UserServiceData userServiceData);
    RefreshTokenEntity getRefreshToken(String refreshTokenString);
    String prepareErrorMessage(List<ObjectError> errorsMessageList);
    User getUserByUsernameOrThrow(String username);
    void deleteToken(String username);
    void setRefreshToken(User user, RefreshTokenEntity refreshToken);
}
