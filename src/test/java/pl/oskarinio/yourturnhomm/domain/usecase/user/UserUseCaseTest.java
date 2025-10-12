package pl.oskarinio.yourturnhomm.domain.usecase.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
    private static final Instant INSTANT = Instant.parse("2023-10-27T10:15:30.00Z");
    private static final String USERNAME = "testUsername";
    private static final String ACCESS_TOKEN = "accessToken";
    private static final String REFRESH_TOKEN = "refreshToken";

    private UserUseCase userUseCase;

    @BeforeEach
    void setUp(){
        userUseCase = new UserUseCase(userRepository, token, clock, ACCESS_TOKEN_SECONDS, REFRESH_TOKEN_SECONDS);
    }

    @Test
    @DisplayName("Poprawny użytkownik, tworzy i ustawia tokeny")
    void generateAndSetTokens_correctUser_resultTokensSet(){
        UserServiceData userData = new UserServiceData(USERNAME, "testPassword");

        when(token.generateToken(userData, ACCESS_TOKEN_SECONDS)).thenReturn(ACCESS_TOKEN);
        when(token.generateToken(userData, REFRESH_TOKEN_SECONDS)).thenReturn(REFRESH_TOKEN);
        userUseCase.generateAndSetTokens(userData);

        generateAndSetTokens_assert(userData);
    }

    private void generateAndSetTokens_assert(UserServiceData userData){
        assertThat(userData.getAccessToken()).isEqualTo(ACCESS_TOKEN);
        assertThat(userData.getRefreshToken()).isEqualTo(REFRESH_TOKEN);
        verify(token).generateToken(userData, ACCESS_TOKEN_SECONDS);
        verify(token).generateToken(userData, REFRESH_TOKEN_SECONDS);
    }

    @Test
    @DisplayName("Poprawna nazwa RefreshToken, zwraca refreshToken ")
    void getRefreshToken_correctRefreshTokenName_resultRefreshTokenReturned(){
        when(clock.instant()).thenReturn(INSTANT);

        RefreshToken refreshToken = userUseCase.getRefreshToken(REFRESH_TOKEN);

        assertThat(refreshToken.getTokenHash()).isEqualTo(REFRESH_TOKEN);
        assertThat(refreshToken.getCreationDate()).isEqualTo(INSTANT);
        assertThat(refreshToken.getExpirationDate()).isEqualTo(INSTANT.plus(REFRESH_TOKEN_SECONDS, ChronoUnit.SECONDS));
    }

    @Test
    @DisplayName("Poprawne komunikaty błędów, zwraca przygotowaną wiadomość")
    void prepareMessageError_basicErrorsMessages_resultMessagePrepared(){
        List<String> errorMessageList = new ArrayList<>(List.of("error1", "error2"));
        String errorMessage = userUseCase.prepareErrorMessage(errorMessageList);
        assertThat(errorMessage).isEqualTo("error1<br>error2<br>");
    }

    @Test
    @DisplayName("Puste komunikaty błędów, zwraca pustą wiadomość")
    void prepareErrorMessage_emptyList_resultEmptyList() {
        List<String> emptyErrorList = new ArrayList<>();
        String errorMessage = userUseCase.prepareErrorMessage(emptyErrorList);
        assertThat(errorMessage).isEmpty();
    }

    @Test
    @DisplayName("Null, puste, poprawne komuniakty błędów, zwraca przygotowaną wiadomość")
    void prepareErrorMessage_nullEmptyCorrectWordsInList_resultMessagePrepared() {
        List<String> errors = prepareNullEmptyCorrectMessagesList();
        String errorMessage = userUseCase.prepareErrorMessage(errors);
        assertThat(errorMessage).isEqualTo("error1<br>null<br><br>error2<br>");
    }

    private List<String> prepareNullEmptyCorrectMessagesList(){
        List<String> errors = new ArrayList<>();
        errors.add("error1");
        errors.add(null);
        errors.add("");
        errors.add("error2");

        return errors;
    }

    @Test
    @DisplayName("Poprawny username, zwraca użytkownika")
    void getUserByUsername_correctUsername_resultUserReturned(){
        User user = getUser();
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));

        User foundUser = userUseCase.getUserByUsernameOrThrow(USERNAME);

        assertThat(foundUser).isEqualTo(user);
        verify(userRepository).findByUsername(USERNAME);
    }

    @Test
    @DisplayName("Niepoprawny username, rzuca UsernameNotFoundException")
    void getUserByUsername_incorrectUsername_resultUsernameNotFoundException(){
        String falseUsername = "falseUsername";
        when(userRepository.findByUsername(falseUsername)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userUseCase.getUserByUsernameOrThrow(falseUsername));
        verify(userRepository).findByUsername(falseUsername);
    }

    @Test
    @DisplayName("Null username, rzuca UsernameNotFoundException")
    void getUserByUsername_nullUsername_resultUsernameNotFoundException() {
        String nullUsername = null;
        when(userRepository.findByUsername(nullUsername)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userUseCase.getUserByUsernameOrThrow(nullUsername));
        verify(userRepository).findByUsername(nullUsername);
    }

    @Test
    @DisplayName("Poprawny user, usuwa token")
    void deleteToken_correctUser_resultTokenDeleted(){
        User user = getUserWithUsername(USERNAME);
        UserUseCase spiedUserUseCase = spy(userUseCase);

        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        spiedUserUseCase.deleteToken(USERNAME);

        assertThat(user.getRefreshToken()).isNull();
        verify(userRepository).findByUsername(USERNAME);
        verify(spiedUserUseCase).setRefreshToken(user,null);
    }

    @Test
    @DisplayName("Poprawny user, refreshToken, token ustawiony")
    void setRefreshToken_correctUser_resultTokenSet(){
        User user = getUser();
        when(clock.instant()).thenReturn(INSTANT);
        RefreshToken refreshToken = userUseCase.getRefreshToken("refreshToken");

        userUseCase.setRefreshToken(user, refreshToken);

        assertThat(user.getRefreshToken()).isEqualTo(refreshToken);
    }

    @Test()
    @DisplayName("Poprawny user, refreshToken null, token zostaje null")
    void setRefreshToken_setNullRefreshToken_resultNullRefreshToken(){
        User user = getUser();
        userUseCase.setRefreshToken(user, null);
        assertThat(user.getRefreshToken()).isNull();
    }

    private User getUserWithUsername(String username){
        User user = new User();
        user.setUsername(username);
        return user;
    }

    private User getUser(){
        return getUserWithUsername(USERNAME);
    }
}



