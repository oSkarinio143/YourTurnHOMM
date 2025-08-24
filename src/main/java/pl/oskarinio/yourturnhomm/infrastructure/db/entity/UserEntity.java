package pl.oskarinio.yourturnhomm.infrastructure.db.entity;

import jakarta.persistence.*;
import lombok.*;
import pl.oskarinio.yourturnhomm.domain.model.user.Role;

import java.time.Instant;
import java.util.*;

@Entity
@RequiredArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name = "app_user")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(unique = true)
    private String username;

    @NonNull
    private String password;

    @NonNull
    private Instant registrationDate;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "refresh_token_id")
    private RefreshTokenEntity refreshToken;

    @Transient
    public String getTokenHash() {
        return refreshToken != null ? refreshToken.getTokenHash() : null;
    }

    public void addRole(Role role){
        roles.add(role);
    }
}
