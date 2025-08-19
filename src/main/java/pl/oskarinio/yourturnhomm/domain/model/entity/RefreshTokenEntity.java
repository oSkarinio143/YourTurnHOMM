package pl.oskarinio.yourturnhomm.domain.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@RequiredArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "refresh_token")
public class RefreshTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String tokenHash;

    @OneToOne(mappedBy = "refreshToken")
    private User user;

    @NonNull
    @Column(nullable = false)
    private Instant creationDate;

    @NonNull
    @Column(nullable = false)
    private Instant expirationDate;
}


