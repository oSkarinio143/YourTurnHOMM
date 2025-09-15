package pl.oskarinio.yourturnhomm.domain.port.user;

import pl.oskarinio.yourturnhomm.domain.model.user.RefreshToken;
import pl.oskarinio.yourturnhomm.domain.model.user.User;
import pl.oskarinio.yourturnhomm.domain.model.user.UserServiceData;

import java.util.List;

public interface UserManagement {
    void generateAndSetTokens(UserServiceData userServiceData);
    RefreshToken getRefreshToken(String refreshTokenString);
    String prepareErrorMessage(List<String> errorsMessageList);
    User getUserByUsernameOrThrow(String username);
    void deleteToken(String username);
    void setRefreshToken(User user, RefreshToken refreshToken);
}
