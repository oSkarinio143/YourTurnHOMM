package pl.oskarinio.yourturnhomm.infrastructure.port.cookie;

public interface TokenDeleter {
    void cleanExpiredTokens();
}
