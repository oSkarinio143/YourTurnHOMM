package pl.oskarinio.yourturnhomm.domain.usecase.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

import java.time.Clock;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RegisterUseCaseTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserManagement userManagement;
    @Mock
    private PasswordEncoderPort passwordEncoderPort;
    @Mock
    private Clock clock;
    @Captor
    private ArgumentCaptor<User> captorUser;
    @Captor
    private ArgumentCaptor<RefreshToken> captorRefreshToken;

    private static final Instant TEST_INSTANT = Instant.parse("2023-10-27T10:15:30.00Z");
    private static final String ACCESS_TOKEN = "accessToken";
    private static final String REFRESH_TOKEN = "refreshToken";
    private static final String USERNAME = "testUsername";
    private static final String PASSWORD = "testPassword";
    private static final String HASHED_PASSWORD = "testHashedPassword";
    private static final String CONFIRM_PASSWORD = "testConfirmPassword";
    private UserServiceData userServiceData;
    private RegisterForm registerForm;
    
    private RegisterUseCase registerUseCase;

    @BeforeEach
    void SetUp(){
        registerUseCase = new RegisterUseCase(userRepository, userManagement, passwordEncoderPort, clock, 100000L);
    }

    @Test
    @DisplayName("RegisterForm z ustawionymi poprawnymi wartościami, zwraca userServiceData z ustawionymi wartościami")
    void registerUser_correctValues_resultUserServiceData(){
        registerForm = new RegisterForm(USERNAME, PASSWORD, CONFIRM_PASSWORD);
        registerUser_act();
        registerUser_assert();
    }
    
    private void registerUser_act(){
        when(passwordEncoderPort.encode(registerForm.getPassword())).thenReturn(HASHED_PASSWORD);
        when(clock.instant()).thenReturn(TEST_INSTANT);
        
        doAnswer(invocation -> {
            UserServiceData tempUserServiceData = invocation.getArgument(0);
            tempUserServiceData.setAccessToken(ACCESS_TOKEN);
            tempUserServiceData.setRefreshToken(REFRESH_TOKEN);
            return null;
        }).when(userManagement).generateAndSetTokens(any(UserServiceData.class));

        userServiceData = registerUseCase.registerUser(registerForm);
    }
    
    private void registerUser_assert(){
        verify(passwordEncoderPort).encode(registerForm.getPassword());
        verify(userManagement).setRefreshToken(captorUser.capture(), captorRefreshToken.capture());
        verify(clock).instant();

        assertCaptured();
        assertUserServiceData();
    }
    
    private void assertUserServiceData(){
        assertThat(userServiceData.getUsername()).isEqualTo(USERNAME);
        assertThat(userServiceData.getAccessToken()).isEqualTo(ACCESS_TOKEN);
        assertThat(userServiceData.getRefreshToken()).isEqualTo(REFRESH_TOKEN);
        assertThat(userServiceData.getRoles()).hasToString("[ROLE_USER]");
    }

    private void assertCaptured(){
        User capturedUser = captorUser.getValue();
        RefreshToken capturdRefreshToken = captorRefreshToken.getValue();

        assertThat(capturedUser.getUsername()).isEqualTo(USERNAME);
        assertThat(capturedUser.getPassword()).isEqualTo(HASHED_PASSWORD);
        assertThat(capturdRefreshToken.getTokenHash()).isEqualTo(REFRESH_TOKEN);
        assertThat(capturdRefreshToken.getCreationDate()).isEqualTo(TEST_INSTANT);
    }

    @Test
    @DisplayName("RegisterForm z null Password, rzuca NullPointerException")
    void registerUser_nullPassword_resultNullPointerException(){
        registerForm = new RegisterForm(USERNAME, PASSWORD, CONFIRM_PASSWORD);
        when(passwordEncoderPort.encode(registerForm.getPassword())).thenReturn(null);

        assertThrows(NullPointerException.class, () -> registerUseCase.registerUser(registerForm));
        verify(clock, never()).instant();
    }

    @Test
    @DisplayName("RegisterForm jest null, rzuca NullPointerException")
    void RegisterUser_registerFormNull_resultNullPointerException(){
        registerForm = null;
        assertThrows(NullPointerException.class, () -> registerUseCase.registerUser(registerForm));
        verify(clock, never()).instant();
    }
}
