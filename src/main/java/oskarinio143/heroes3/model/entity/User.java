package oskarinio143.heroes3.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@RequiredArgsConstructor
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
    private List<String> role;
    @OneToOne
    @JoinColumn(name = "refreshToken")
    private RefreshToken refreshToken;

    @Transient
    public String getTokenHash() {
        return refreshToken != null ? refreshToken.getTokenHash() : null;
    }

    public void setRefreshToken(RefreshToken refreshToken){
        this.refreshToken = refreshToken;
        if(refreshToken.getUser() != this) {
            refreshToken.setUser(this);
        }
    }
}
