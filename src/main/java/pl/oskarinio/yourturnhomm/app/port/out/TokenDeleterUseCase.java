package pl.oskarinio.yourturnhomm.app.port.in.rest;

public interface TokenDeleterUseCase {
    void cleanExpiredTokens();
}
