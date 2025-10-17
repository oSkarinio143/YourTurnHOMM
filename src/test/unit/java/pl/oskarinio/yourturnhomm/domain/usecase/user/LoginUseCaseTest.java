package pl.oskarinio.yourturnhomm.domain.usecase.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.oskarinio.yourturnhomm.domain.model.form.LoginForm;
import pl.oskarinio.yourturnhomm.domain.model.user.RefreshToken;
import pl.oskarinio.yourturnhomm.domain.model.user.User;
import pl.oskarinio.yourturnhomm.domain.model.user.UserServiceData;
import pl.oskarinio.yourturnhomm.domain.port.out.PasswordEncoderPort;
import pl.oskarinio.yourturnhomm.domain.port.user.UserManagement;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static pl.oskarinio.yourturnhomm.domain.model.user.Role.ROLE_USER;

@ExtendWith(MockitoExtension.class)
class LoginUseCaseTest {
    @Mock
    private UserManagement userManagement;
    @Mock
    private PasswordEncoderPort passwordEncoderPort;

    private static final long REFRESH_TOKEN_SECONDS = 100000L;
    private static final Instant INSTANT = Instant.parse("2023-10-27T10:15:30.00Z");
    private static final String ACCESS_TOKEN = "accessToken";
    private static final String REFRESH_TOKEN = "refreshToken";
    private static final String USERNAME = "testUsername";
    private static final String PASSWORD = "testPassword";
    private static final String HASHED_PASSWORD = "testHashedPassword";
    private LoginForm loginForm;
    private RefreshToken refreshToken;
    private UserServiceData userServiceData;
    private User databaseUser;

    private LoginUseCase loginUseCase;

    @BeforeEach
    void SetUp(){
        loginUseCase = new LoginUseCase(userManagement, passwordEncoderPort);
    }

    @Test
    @DisplayName("LoginForm z poprawnymi wartościami, zwraca userServiceData z ustawionymi wartościami")
    void loginUser_correctValues(){
        loginForm = new LoginForm(USERNAME, PASSWORD);
        refreshToken = new RefreshToken(REFRESH_TOKEN, INSTANT, INSTANT.plus(REFRESH_TOKEN_SECONDS, ChronoUnit.SECONDS));
        databaseUser = getUser();

        loginUser_act();

        loginUser_assert();
    }

    private User getUser(){
        User databaseUser = new User();
        databaseUser.setUsername(USERNAME);
        databaseUser.setPassword(HASHED_PASSWORD);
        databaseUser.setRoles(Set.of(ROLE_USER));
        return databaseUser;
    }

    private void loginUser_act(){
        when(userManagement.getRefreshToken(REFRESH_TOKEN)).thenReturn(refreshToken);
        when(userManagement.getUserByUsernameOrThrow(USERNAME)).thenReturn(databaseUser);
        when(passwordEncoderPort.matches(PASSWORD, HASHED_PASSWORD)).thenReturn(true);

        doAnswer(invocation -> {
            UserServiceData userServiceDataInvocation = invocation.getArgument(0);
            userServiceDataInvocation.setAccessToken(ACCESS_TOKEN);
            userServiceDataInvocation.setRefreshToken(REFRESH_TOKEN);
            return null;
        }).when(userManagement).generateAndSetTokens(any(UserServiceData.class));

        userServiceData = loginUseCase.loginUser(loginForm);
    }

    private void loginUser_assert(){
        assertValues();
        verifyValues();
    }

    private void assertValues(){
        assertThat(userServiceData.getUsername()).isEqualTo(USERNAME);
        assertThat(userServiceData.getPassword()).isEqualTo(HASHED_PASSWORD);
        assertThat(userServiceData.getAccessToken()).isEqualTo(ACCESS_TOKEN);
        assertThat(userServiceData.getRefreshToken()).isEqualTo(REFRESH_TOKEN);
        assertThat(userServiceData.getRoles()).hasToString("[ROLE_USER]");
    }

    private void verifyValues(){
        verify(passwordEncoderPort).matches(PASSWORD,HASHED_PASSWORD);
        verify(userManagement).getUserByUsernameOrThrow(USERNAME);
        verify(userManagement).generateAndSetTokens(userServiceData);
        verify(userManagement).getRefreshToken(REFRESH_TOKEN);
        verify(userManagement).setRefreshToken(databaseUser, refreshToken);
    }

    @Test
    @DisplayName("LoginForm z null username, rzuca NullPointerExcpetion")
    void loginUser_nullUsername_resultNullPointerException(){
        loginForm = new LoginForm(USERNAME, PASSWORD);
        when(userManagement.getUserByUsernameOrThrow(USERNAME)).thenReturn(null);

        assertThrows(NullPointerException.class, () -> loginUseCase.loginUser(loginForm));
        verify(userManagement, never()).getRefreshToken(REFRESH_TOKEN);
    }

    @Test
    @DisplayName("LoginForm jest null, rzuca NullPointerExcpetion")
    void loginUser_loginFormNull_resultNullPointerException(){
        loginForm = null;

        assertThrows(NullPointerException.class, () -> loginUseCase.loginUser(loginForm));
        verify(userManagement, never()).getRefreshToken(REFRESH_TOKEN);
    }
}
