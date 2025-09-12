package pl.oskarinio.yourturnhomm.domain.port.user;

import org.springframework.validation.ObjectError;
import pl.oskarinio.yourturnhomm.domain.model.user.RefreshToken;
import pl.oskarinio.yourturnhomm.domain.model.user.UserServiceData;

import java.util.List;

public interface User {
    void generateAndSetTokens(UserServiceData userServiceData);
    RefreshToken getRefreshToken(String refreshTokenString);
    String prepareErrorMessage(List<ObjectError> errorsMessageList);
    pl.oskarinio.yourturnhomm.domain.model.user.User getUserByUsernameOrThrow(String username);
    void deleteToken(String username);
    void setRefreshToken(pl.oskarinio.yourturnhomm.domain.model.user.User user, RefreshToken refreshToken);
}
