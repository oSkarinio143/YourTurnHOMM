package pl.oskarinio.yourturnhomm.domain.service.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.oskarinio.yourturnhomm.domain.model.form.RegisterForm;
import pl.oskarinio.yourturnhomm.domain.model.user.RefreshToken;
import pl.oskarinio.yourturnhomm.domain.model.user.User;
import pl.oskarinio.yourturnhomm.domain.model.user.UserServiceData;
import pl.oskarinio.yourturnhomm.domain.port.out.PasswordEncoderPort;
import pl.oskarinio.yourturnhomm.domain.port.out.UserRepository;
import pl.oskarinio.yourturnhomm.domain.port.user.UserManagement;
import pl.oskarinio.yourturnhomm.domain.usecase.user.RegisterUseCase;

import java.time.Clock;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static pl.oskarinio.yourturnhomm.domain.model.user.Role.ROLE_USER;

@ExtendWith(MockitoExtension.class)
public class RegisterUseCaseTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserManagement userManagement;
    @Mock
    private PasswordEncoderPort passwordEncoderPort;
    @Mock
    private Clock clock;

    private final long REFRESH_TOKEN_SECONDS = 100000L;
    private static final Instant TEST_INSTANT = Instant.parse("2023-10-27T10:15:30.00Z");

    @Captor
    private ArgumentCaptor<User> captorUser;

    @Captor
    private ArgumentCaptor<RefreshToken> captorRefreshToken;

    private RegisterUseCase registerUseCase;

    @BeforeEach
    void SetUp(){
        registerUseCase = new RegisterUseCase(userRepository, userManagement, passwordEncoderPort, clock, REFRESH_TOKEN_SECONDS);
    }

    @Test
    public void registerUser_correctValues(){
        String username = "user";
        String password = "pwd";
        String confirmPasswrod = "pwd";
        String accessTokenString = "1234";
        String refreshTokenString = "4321";
        String hashedPassword = "hashedPassword";
        RegisterForm registerForm = new RegisterForm(username, password, confirmPasswrod);

        when(passwordEncoderPort.encode(registerForm.getPassword())).thenReturn(hashedPassword);
        when(clock.instant()).thenReturn(TEST_INSTANT);
        doAnswer(invocation -> {
            UserServiceData userServiceData = invocation.getArgument(0);
            userServiceData.setAccessToken(accessTokenString);
            userServiceData.setRefreshToken(refreshTokenString);
            return null;
        }).when(userManagement).generateAndSetTokens(any(UserServiceData.class));

        UserServiceData userServiceData = registerUseCase.registerUser(registerForm);

        verify(passwordEncoderPort).encode(registerForm.getPassword());
        verify(userManagement).setRefreshToken(captorUser.capture(), captorRefreshToken.capture());
        verify(clock).instant();
        User capturedUser = captorUser.getValue();
        RefreshToken capturdRefreshToken = captorRefreshToken.getValue();

        assertThat(capturedUser.getUsername()).isEqualTo(username);
        assertThat(capturedUser.getPassword()).isEqualTo(hashedPassword);
        assertThat(capturdRefreshToken.getTokenHash()).isEqualTo(refreshTokenString);
        assertThat(capturdRefreshToken.getCreationDate()).isEqualTo(TEST_INSTANT);

        assertThat(userServiceData.getUsername()).isEqualTo(username);
        assertThat(userServiceData.getAccessToken()).isEqualTo(accessTokenString);
        assertThat(userServiceData.getRefreshToken()).isEqualTo(refreshTokenString);
        assertThat(userServiceData.getRoles().toString()).isEqualTo("[ROLE_USER]");
    }

    @Test
    public void RegisterUser_RegisterFormNull_resultNullPointerException(){
        RegisterForm registerForm = null;
        assertThrows(NullPointerException.class, () -> registerUseCase.registerUser(registerForm));
    }
}
