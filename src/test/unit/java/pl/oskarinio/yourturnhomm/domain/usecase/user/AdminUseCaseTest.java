package pl.oskarinio.yourturnhomm.domain.usecase.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.oskarinio.yourturnhomm.domain.model.exception.UsernameNotFoundException;
import pl.oskarinio.yourturnhomm.domain.model.user.Role;
import pl.oskarinio.yourturnhomm.domain.model.user.User;
import pl.oskarinio.yourturnhomm.domain.port.out.UserRepository;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static pl.oskarinio.yourturnhomm.domain.model.user.Role.ROLE_ADMIN;
import static pl.oskarinio.yourturnhomm.domain.model.user.Role.ROLE_USER;

@ExtendWith(MockitoExtension.class)
public class AdminUseCaseTest {

    @Mock
    private UserRepository userRepository;
    private final String[] activeProfiles = new String[1];

    private String ADMIN_USERNAME;
    private final String USERNAME = "testUsername";

    @Captor
    private ArgumentCaptor<User> captor;

    private AdminUseCase adminUseCase;

    @BeforeEach
    void SetUp(){
        adminUseCase = new AdminUseCase(userRepository, new String[1]);
    }

    @ParameterizedTest
    @ValueSource(strings = {"h2", "mysql"})
    @DisplayName("Profil h2/mysql, zwykly użytkownik, uswuwa użytkownika")
    void deleteUser_bothProfilesDeleteBasicUser_resultUserDeleted(String profile){
        setUpProfiles(profile);
        Optional<User> userOpt = getUserOptional();

        when(userRepository.findByUsername(USERNAME)).thenReturn(userOpt);
        adminUseCase.deleteUser(USERNAME);

        deleteUser_assert();
    }

    private void deleteUser_assert(){
        verify(userRepository).findByUsername(USERNAME);
        verify(userRepository).delete(captor.capture());
        User userDeleted = captor.getValue();
        assertThat(userDeleted.getUsername()).isEqualTo(USERNAME);
    }

    @ParameterizedTest
    @ValueSource(strings = {"h2", "mysql"})
    @DisplayName("profil h2/mysql, użytkownik root, nie usuwa użytkownika")
    void deleteUser_bothProfileDeleteRoot_resultNothingDeleted(String profile){
        setUpProfiles(profile);
        Optional<User> userOpt = getUserOptional(ADMIN_USERNAME);

        when(userRepository.findByUsername(ADMIN_USERNAME)).thenReturn(userOpt);
        adminUseCase.deleteUser(ADMIN_USERNAME);

        verify(userRepository).findByUsername(ADMIN_USERNAME);
        verify(userRepository, never()).delete(any());
    }

    private void setUpProfiles(String profileName){
        activeProfiles[0] = profileName;
        adminUseCase = new AdminUseCase(userRepository, activeProfiles);
        if(profileName.equals("h2"))
            ADMIN_USERNAME = "adminUser";
        else
            ADMIN_USERNAME = System.getenv("ADMIN_USERNAME");
    }

    @Test
    @DisplayName("Brak użytkownika, rzuca UsernameNotFoundException")
    void deleteUser_emptyUserOptional_resultUsernameNotFoundException(){
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> adminUseCase.deleteUser(USERNAME));
    }

    @Test
    @DisplayName("Użytkownik zwykły, przyznaje admina")
    void grantAdminRole_basicUser_resultAdminGranted(){
        Optional<User> userOpt = getUserOptional(USERNAME, Set.of(ROLE_USER));

        when(userRepository.findByUsername(USERNAME)).thenReturn(userOpt);
        adminUseCase.grantAdminRole(USERNAME);

        grantAdminRoleCorrect_assert(userOpt);
    }

    private void grantAdminRoleCorrect_assert(Optional<User> userOpt){
        verify(userRepository).findByUsername(USERNAME);
        verify(userRepository).save(captor.capture());
        User capturedUser = captor.getValue();
        assertThat(capturedUser).isEqualTo(userOpt.get());
        assertThat(capturedUser.getRoles())
                .containsExactlyInAnyOrder(ROLE_USER, ROLE_ADMIN)
                .hasSize(2);
    }

    @Test
    @DisplayName("Użytkownik administrator, nic nie przyznaje")
    void grantAdminRole_adminUser_resultNoRoleAdded(){
        Optional<User> userOpt = getUserOptional(USERNAME,Set.of(ROLE_USER, ROLE_ADMIN));

        when(userRepository.findByUsername(USERNAME)).thenReturn(userOpt);
        adminUseCase.grantAdminRole(USERNAME);

        grantAdminRoleIncorrect_assert(userOpt);
    }

    private void grantAdminRoleIncorrect_assert(Optional<User> userOpt){
        verify(userRepository).findByUsername(USERNAME);
        verify(userRepository).save(captor.capture());
        User capturedUser = captor.getValue();

        assertThat(capturedUser).isEqualTo(userOpt.get());
        assertThat(capturedUser.getRoles()).isEqualTo(Set.of(ROLE_USER, ROLE_ADMIN));
    }

    @Test
    @DisplayName("Użytkownik null, nic nie robi")
    void grantAdminRole_nullUser_resultNothingHappened(){
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());
        adminUseCase.grantAdminRole(USERNAME);
        verify(userRepository).findByUsername(USERNAME);
        verify(userRepository, never()).save(any());
    }

    private Optional<User> getUserOptional(){
        User user = new User();
        user.setUsername(USERNAME);
        Optional<User> userOpt = Optional.of(user);
        return userOpt;
    }

    private Optional<User> getUserOptional(String username){
        User user = new User();
        user.setUsername(username);
        return Optional.of(user);
    }

    private Optional<User> getUserOptional(String username, Set<Role> roles){
        User user = new User();
        user.setUsername(username);
        Optional<User> userOpt = Optional.of(user);
        roles.forEach(user::addRole);
        return userOpt;
    }
}
