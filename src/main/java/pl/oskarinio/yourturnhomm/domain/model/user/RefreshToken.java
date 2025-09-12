package pl.oskarinio.yourturnhomm.domain.model.user;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
public class RefreshToken {
    private Long id;
    private final String tokenHash;
    private final Instant creationDate;
    private final Instant expirationDate;
    private User user;

    public RefreshToken(String tokenHash, Instant creationDate, Instant expirationDate){
        this.tokenHash = tokenHash;
        this.creationDate = creationDate;
        this.expirationDate = expirationDate;
    }
}
