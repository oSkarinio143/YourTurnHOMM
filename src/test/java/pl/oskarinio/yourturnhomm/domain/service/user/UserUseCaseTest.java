package pl.oskarinio.yourturnhomm.domain.service.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.oskarinio.yourturnhomm.domain.model.exception.UsernameNotFoundException;
import pl.oskarinio.yourturnhomm.domain.model.user.RefreshToken;
import pl.oskarinio.yourturnhomm.domain.model.user.User;
import pl.oskarinio.yourturnhomm.domain.model.user.UserServiceData;
import pl.oskarinio.yourturnhomm.domain.port.out.Token;
import pl.oskarinio.yourturnhomm.domain.port.out.UserRepository;
import pl.oskarinio.yourturnhomm.domain.usecase.user.UserUseCase;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserUseCaseTest {
    @Mock
    private Token token;
    @Mock
    private Clock clock;
    @Mock
    private UserRepository userRepository;

    private static final long ACCESS_TOKEN_SECONDS = 1000L;
    private static final long REFRESH_TOKEN_SECONDS = 100000L;
    private static final String TEST_USERNAME = "user";
    private static final Instant TEST_INSTANT = Instant.parse("2023-10-27T10:15:30.00Z");

    private UserUseCase userUseCase;

    @BeforeEach
    void setUp(){
        userUseCase = new UserUseCase(userRepository, token, clock, ACCESS_TOKEN_SECONDS, REFRESH_TOKEN_SECONDS);
    }

    @Test
    void generateAndSetTokensTest_correctValues(){
        UserServiceData userData = new UserServiceData(TEST_USERNAME, "1234");
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";

        when(token.generateToken(userData, ACCESS_TOKEN_SECONDS)).thenReturn(accessToken);
        when(token.generateToken(userData, REFRESH_TOKEN_SECONDS)).thenReturn(refreshToken);
        userUseCase.generateAndSetTokens(userData);

        assertThat(userData.getAccessToken()).isEqualTo(accessToken);
        assertThat(userData.getRefreshToken()).isEqualTo(refreshToken);
        verify(token).generateToken(userData, ACCESS_TOKEN_SECONDS);
        verify(token).generateToken(userData, REFRESH_TOKEN_SECONDS);
    }

    @Test
    void getRefreshToken_correctValues(){
        String refreshTokenString = "refreshToken";

        when(clock.instant()).thenReturn(TEST_INSTANT);

        RefreshToken refreshToken = userUseCase.getRefreshToken(refreshTokenString);

        assertThat(refreshToken.getTokenHash()).isEqualTo(refreshTokenString);
        assertThat(refreshToken.getCreationDate()).isEqualTo(TEST_INSTANT);
        assertThat(refreshToken.getExpirationDate()).isEqualTo(TEST_INSTANT.plus(REFRESH_TOKEN_SECONDS, ChronoUnit.SECONDS));
        verify(clock).instant();
    }

    @Test
    void prepareMessageError_correctValues(){
        List<String> errorMessageList = new ArrayList<>(List.of("error1", "error2"));
        String errorMessage = userUseCase.prepareErrorMessage(errorMessageList);
        assertThat(errorMessage).isEqualTo("error1<br>error2<br>");
    }

    @Test
    void prepareErrorMessage_setEmptyList_resultEmptyList() {
        List<String> emptyErrorList = new ArrayList<>();

        String errorMessage = userUseCase.prepareErrorMessage(emptyErrorList);

        assertThat(errorMessage).isEmpty();
    }

    @Test
    void prepareErrorMessage_nullAndEmptyWordsInList_resultCorrectString() {
        List<String> errorsWithNulls = new ArrayList<>();
        errorsWithNulls.add("Błąd numer 1");
        errorsWithNulls.add(null);
        errorsWithNulls.add("");
        errorsWithNulls.add("Błąd numer 4");

        String errorMessage = userUseCase.prepareErrorMessage(errorsWithNulls);

        assertThat(errorMessage).isEqualTo("Błąd numer 1<br>null<br><br>Błąd numer 4<br>");
    }

    @Test
    void getUserByUsername_correctValues(){
        User user = getTestUser();
        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.of(user));
        User foundUser = userUseCase.getUserByUsernameOrThrow(TEST_USERNAME);
        assertThat(foundUser).isEqualTo(user);
        verify(userRepository).findByUsername(TEST_USERNAME);
    }

    @Test
    void getUserByUsername_nonExistenceUser_resultUsernameNotFoundException(){
        String nonExistentUsername = "ghostUser";
        when(userRepository.findByUsername(nonExistentUsername)).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> userUseCase.getUserByUsernameOrThrow(nonExistentUsername));
        verify(userRepository).findByUsername(nonExistentUsername);
    }

    @Test
    void getUserByUsername_nullUsername_resultUsernameNotFoundException() {
        String nullUsername = null;
        when(userRepository.findByUsername(nullUsername)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userUseCase.getUserByUsernameOrThrow(nullUsername));
        verify(userRepository).findByUsername(nullUsername);
    }

    @Test
    void deleteToken_correctValues(){
        User user = getUserWithUsername(TEST_USERNAME);
        UserUseCase spiedUserUseCase = spy(userUseCase);

        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.of(user));
        spiedUserUseCase.deleteToken(TEST_USERNAME);

        assertThat(user.getRefreshToken()).isNull();
        verify(userRepository).findByUsername(TEST_USERNAME);
        verify(spiedUserUseCase).setRefreshToken(user,null);
    }

    @Test
    public void setRefreshToken_correctValues(){
        User user = getTestUser();

        when(clock.instant()).thenReturn(TEST_INSTANT);
        RefreshToken refreshToken = userUseCase.getRefreshToken("refreshToken");

        userUseCase.setRefreshToken(user, refreshToken);
        assertThat(user.getRefreshToken()).isEqualTo(refreshToken);
    }

    @Test
    public void setRefreshToken_setNullRefreshToken_resultNullRefreshToken(){
        User user = getTestUser();
        user.setRefreshToken(mock(RefreshToken.class));

        userUseCase.setRefreshToken(user, null);
        assertThat(user.getRefreshToken()).isNull();
    }

    private User getUserWithUsername(String username){
        User user = new User();
        user.setUsername(username);
        return user;
    }

    private User getTestUser(){
        return getUserWithUsername(TEST_USERNAME);
    }
}



