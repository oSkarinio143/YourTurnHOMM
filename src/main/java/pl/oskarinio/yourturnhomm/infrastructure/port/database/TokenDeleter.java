package pl.oskarinio.yourturnhomm.infrastructure.port.database;

public interface TokenDeleter {
    void cleanExpiredTokens();
}
