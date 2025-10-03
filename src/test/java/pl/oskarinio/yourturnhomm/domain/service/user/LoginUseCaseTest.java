package pl.oskarinio.yourturnhomm.domain.service.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.oskarinio.yourturnhomm.domain.model.form.LoginForm;
import pl.oskarinio.yourturnhomm.domain.model.user.RefreshToken;
import pl.oskarinio.yourturnhomm.domain.model.user.User;
import pl.oskarinio.yourturnhomm.domain.model.user.UserServiceData;
import pl.oskarinio.yourturnhomm.domain.port.out.PasswordEncoderPort;
import pl.oskarinio.yourturnhomm.domain.port.user.Login;
import pl.oskarinio.yourturnhomm.domain.port.user.UserManagement;
import pl.oskarinio.yourturnhomm.domain.usecase.user.LoginUseCase;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static pl.oskarinio.yourturnhomm.domain.model.user.Role.ROLE_USER;

@ExtendWith(MockitoExtension.class)
public class LoginUseCaseTest {
    @Mock
    private UserManagement userManagement;
    @Mock
    private PasswordEncoderPort passwordEncoderPort;

    private static final Instant TEST_INSTANT = Instant.parse("2023-10-27T10:15:30.00Z");
    private final long REFRESH_TOKEN_SECONDS = 100000L;

    private LoginUseCase loginUseCase;

    @BeforeEach
    void SetUp(){
        loginUseCase = new LoginUseCase(userManagement, passwordEncoderPort);
    }

    @Test
    void loginUser_correctValues(){
        String username = "user";
        String password = "pwd";
        String hashedpassword = "hashedpwd";
        String accessTokenString = "accessToken";
        String refreshTokenString = "refreshToken";
        LoginForm loginForm = new LoginForm(username, password);

        User databaseUser = new User();
        databaseUser.setUsername(username);
        databaseUser.setPassword(hashedpassword);
        databaseUser.setRoles(Set.of(ROLE_USER));

        RefreshToken refreshToken = new RefreshToken(refreshTokenString, TEST_INSTANT, TEST_INSTANT.plus(REFRESH_TOKEN_SECONDS, ChronoUnit.SECONDS));

        when(userManagement.getRefreshToken(refreshTokenString)).thenReturn(refreshToken);
        when(userManagement.getUserByUsernameOrThrow(username)).thenReturn(databaseUser);
        when(passwordEncoderPort.matches(password, hashedpassword)).thenReturn(true);

        doAnswer(invocation -> {
            UserServiceData userServiceDataInvocation = invocation.getArgument(0);
           userServiceDataInvocation.setAccessToken(accessTokenString);
           userServiceDataInvocation.setRefreshToken(refreshTokenString);
           return null;
        }).when(userManagement).generateAndSetTokens(any(UserServiceData.class));

        UserServiceData userServiceData = loginUseCase.loginUser(loginForm);

        assertThat(userServiceData.getUsername()).isEqualTo(username);
        assertThat(userServiceData.getPassword()).isEqualTo(hashedpassword);
        assertThat(userServiceData.getAccessToken()).isEqualTo(accessTokenString);
        assertThat(userServiceData.getRefreshToken()).isEqualTo(refreshTokenString);
        assertThat(userServiceData.getRoles()).hasToString("[ROLE_USER]");
        verify(passwordEncoderPort).matches(password,hashedpassword);
        verify(userManagement).getUserByUsernameOrThrow(username);
        verify(userManagement).generateAndSetTokens(userServiceData);
        verify(userManagement).getRefreshToken(refreshTokenString);
        verify(userManagement).setRefreshToken(databaseUser, refreshToken);
    }
}
