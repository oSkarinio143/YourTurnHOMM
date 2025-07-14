package oskarinio143.heroes3.model.entity;

import jakarta.persistence.*;
import lombok.*;
import oskarinio143.heroes3.model.constant.Role;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@RequiredArgsConstructor
@NoArgsConstructor
@Setter
@Getter

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NonNull
    @Column(unique = true)
    private String username;

    @NonNull
    private String password;

    @NonNull
    private Date registrationDate = new Date();

    @NonNull
    private List<Role> roles = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "refreshToken")
    private RefreshToken refreshToken;

    @Transient
    public String getTokenHash() {
        return refreshToken != null ? refreshToken.getTokenHash() : null;
    }

    public void addRole(Role role){
        roles.add(role);
    }
}
