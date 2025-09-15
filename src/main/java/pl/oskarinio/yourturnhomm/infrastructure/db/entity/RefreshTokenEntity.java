package pl.oskarinio.yourturnhomm.infrastructure.db.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

@Entity
@RequiredArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "refresh_token")
public class RefreshTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String tokenHash;

    @NonNull
    @Column(nullable = false)
    private Instant creationDate;

    @NonNull
    @Column(nullable = false)
    private Instant expirationDate;
}


