package pl.oskarinio.yourturnhomm.domain.service.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import pl.oskarinio.yourturnhomm.domain.model.exception.UsernameNotFoundException;
import pl.oskarinio.yourturnhomm.domain.model.user.Role;
import pl.oskarinio.yourturnhomm.domain.model.user.User;
import pl.oskarinio.yourturnhomm.domain.port.out.UserRepository;
import pl.oskarinio.yourturnhomm.domain.usecase.user.AdminUseCase;

import java.util.Arrays;
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
    private String[] activeProfiles = new String[1];

    @Value("${ADMIN_USERNAME}")
    private String adminUsername;
    private final String TEST_USERNAME = "user";

    @Captor
    private ArgumentCaptor<User> captor;

    private AdminUseCase adminUseCase;

    @BeforeEach
    void SetUp(){
        adminUseCase = new AdminUseCase(userRepository, new String[1]);
    }

    @ParameterizedTest
    @ValueSource(strings = {"h2", "mysql"})
    void deleteUser_profilesRegularUser_correctValues(String profile){
        String[] profiles = {profile};
        adminUseCase = new AdminUseCase(userRepository, profiles);
        Optional<User> userOpt = getUserOptional();

        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(userOpt);
        adminUseCase.deleteUser(TEST_USERNAME);

        verify(userRepository).findByUsername(TEST_USERNAME);
        verify(userRepository).delete(captor.capture());
        User userDeleted = captor.getValue();
        assertThat(userDeleted.getUsername()).isEqualTo(TEST_USERNAME);
    }

    @Test
    void deleteUser_profileh2DeleteRoot_resultNothingDeleted(){
        String[] profiles = {"h2"};
        adminUseCase = new AdminUseCase(userRepository, profiles);

        String username = "adminUser";
        Optional<User> userOpt = getUserOptional(username);

        when(userRepository.findByUsername(username)).thenReturn(userOpt);
        adminUseCase.deleteUser(username);

        verify(userRepository).findByUsername(username);
        verify(userRepository, never()).delete(any());
    }

    @Test
    void deleteUser_profilemysqlDeleteRoot_resultNothingDeleted(){
        String[] profiles = {"mysql"};
        adminUseCase = new AdminUseCase(userRepository, profiles);

        String username = System.getenv("ADMIN_USERNAME");
        Optional<User> userOpt = getUserOptional(username);

        when(userRepository.findByUsername(username)).thenReturn(userOpt);
        adminUseCase.deleteUser(username);

        verify(userRepository).findByUsername(username);
        verify(userRepository, never()).delete(any());
    }

    @Test
    void deleteUser_emptyUserOptional_resultUsernameNotFoundException(){
        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> adminUseCase.deleteUser(TEST_USERNAME));
    }

    @Test
    void grantAdminRole_correctValues(){
        Optional<User> userOpt = getUserOptional(TEST_USERNAME, Set.of(ROLE_USER));

        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(userOpt);
        adminUseCase.grantAdminRole(TEST_USERNAME);

        verify(userRepository).findByUsername(TEST_USERNAME);
        verify(userRepository).save(captor.capture());
        User capturedUser = captor.getValue();
        assertThat(capturedUser).isEqualTo(userOpt.get());
        assertThat(capturedUser.getRoles())
                .containsExactlyInAnyOrder(ROLE_USER, ROLE_ADMIN)
                .hasSize(2);
    }

    @Test
    void grantAdminRole_userIsAdmin_resultNoRoleAdded(){
        Optional<User> userOpt = getUserOptional(TEST_USERNAME,Set.of(ROLE_USER, ROLE_ADMIN));

        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(userOpt);
        adminUseCase.grantAdminRole(TEST_USERNAME);

        verify(userRepository).findByUsername(TEST_USERNAME);
        verify(userRepository).save(captor.capture());
        User capturedUser = captor.getValue();
        assertThat(capturedUser).isEqualTo(userOpt.get());
        assertThat(capturedUser.getRoles()).isEqualTo(Set.of(ROLE_USER, ROLE_ADMIN));
    }

    @Test
    void grantAdminRole_nullUser_resultNothingHappened(){
        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.empty());
        adminUseCase.grantAdminRole(TEST_USERNAME);
        verify(userRepository).findByUsername(TEST_USERNAME);
        verify(userRepository, never()).save(any());
    }

    private Optional<User> getUserOptional(){
        User user = new User();
        user.setUsername(TEST_USERNAME);
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
