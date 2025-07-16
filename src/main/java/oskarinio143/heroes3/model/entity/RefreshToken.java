package oskarinio143.heroes3.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;
import java.time.Instant;
import java.util.Date;

@RequiredArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity

public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

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


