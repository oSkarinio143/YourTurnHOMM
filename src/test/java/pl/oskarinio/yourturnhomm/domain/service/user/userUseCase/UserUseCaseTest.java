package pl.oskarinio.yourturnhomm.domain.service.user.userUseCase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.oskarinio.yourturnhomm.domain.model.user.RefreshToken;
import pl.oskarinio.yourturnhomm.domain.model.user.UserServiceData;
import pl.oskarinio.yourturnhomm.domain.port.out.Token;
import pl.oskarinio.yourturnhomm.domain.port.out.UserRepository;
import pl.oskarinio.yourturnhomm.domain.usecase.user.UserUseCase;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserUseCaseTest {

    @Mock
    private Token token;
    @Mock
    private Clock clock;
    @Mock
    private UserRepository userRepository;
    private static final long accessTokenSeconds = 1000L;
    private static final long refreshTokenSeconds = 100000L;

    private UserUseCase userUseCase;

    @BeforeEach
    void setUp(){
        userUseCase = new UserUseCase(userRepository, token, clock, accessTokenSeconds, refreshTokenSeconds);
    }

    @Test
    void generateAndSetTokensTest(){
        UserServiceData userData = new UserServiceData("user", "1234");
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";

        prepareTokenMocks(userData,accessToken,refreshToken);
        userUseCase.generateAndSetTokens(userData);

        assertThat(userData.getAccessToken()).isEqualTo(accessToken);
        assertThat(userData.getRefreshToken()).isEqualTo(refreshToken);
    }

    private void prepareTokenMocks(UserServiceData userData, String accessToken, String refreshToken){
        when(token.generateToken(userData, accessTokenSeconds)).thenReturn(accessToken);
        when(token.generateToken(userData, refreshTokenSeconds)).thenReturn(refreshToken);
    }

    @Test
    void getRefreshToken(){
        String refreshTokenString = "refreshToken";
        Instant anyTime = Instant.parse("2023-10-27T10:15:30.00Z");

        when(clock.instant()).thenReturn(anyTime);

        RefreshToken refreshToken = userUseCase.getRefreshToken(refreshTokenString);

        assertThat(refreshToken.getTokenHash()).isEqualTo(refreshTokenString);
        assertThat(refreshToken.getCreationDate()).isEqualTo(anyTime);
        assertThat(refreshToken.getExpirationDate()).isEqualTo(anyTime.plus(refreshTokenSeconds, ChronoUnit.SECONDS));
    }

    @Test
    void prepareMessageError(){
        List<String> errorMessageList = new ArrayList<>(List.of("error1", "error2"));
        String errorMessage = userUseCase.prepareErrorMessage(errorMessageList);
        assertThat(errorMessage).isEqualTo("error1<br>error2<br>");
    }

//    @Test
//    void getUserByUsernameOrThrow(){
//        String username = "user";
//        User user = new User();
//        user.setUsername(username);
//        when(userRepository.findByUsername(username)).thenReturn(Optional.of(new User()));
//        assertThrows(UsernameNotFoundException.class, () -> userUseCase.getUserByUsernameOrThrow(username));
//        userRepository.save(user);
//        assertThat(userUseCase.getUserByUsernameOrThrow(username).getUsername()).isEqualTo(username);
//    }
}


