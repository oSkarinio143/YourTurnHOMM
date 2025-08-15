package pl.oskarinio.yourturnhomm.domain.user.port.in;

import org.springframework.validation.ObjectError;
import pl.oskarinio.yourturnhomm.domain.user.model.UserServiceData;
import pl.oskarinio.yourturnhomm.domain.model.entity.RefreshToken;
import pl.oskarinio.yourturnhomm.domain.model.entity.User;

import java.util.List;

public interface UserUseCase {
    void generateAndSetTokens(UserServiceData userServiceData);
    RefreshToken getRefreshToken(String refreshTokenString);
    String prepareErrorMessage(List<ObjectError> errorsMessageList);
    User getUserByUsernameOrThrow(String username);
    void deleteToken(String username);
    void setRefreshToken(User user, RefreshToken refreshToken);
}
