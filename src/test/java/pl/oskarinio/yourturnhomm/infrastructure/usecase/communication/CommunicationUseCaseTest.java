package pl.oskarinio.yourturnhomm.infrastructure.usecase.communication;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommunicationUseCaseTest {
    @Mock
    private Map<UUID, SseEmitter> emitters;
    private static final String TEST_MESSAGE = "testMessage";
    private UUID randomUUID;
    private SseEmitter emitter;

    private CommunicationUseCase communicationUseCase;

    @BeforeEach
    void setUp(){
        communicationUseCase = new CommunicationUseCase(emitters);
        randomUUID = UUID.randomUUID();
        emitter = mock(SseEmitter.class);
    }

    @Test
    @DisplayName("Utworzony emitter dla UUID, ustawia w mapie")
    void createEmitter_correctValues_resultEmitterCreated(){
        SseEmitter workingEmitter = communicationUseCase.createEmitter(randomUUID);

        assertThat(workingEmitter).isNotNull();
        assertThat(workingEmitter.getTimeout()).isEqualTo(3600000L);
        verify(emitters).put(randomUUID,workingEmitter);
    }

    @Test
    @DisplayName("Utworzony emitter dla UUID, wysyła wiadomość")
    void sendMessage_correctValues_resultMessageSend() throws IOException {
        when(emitters.get(randomUUID)).thenReturn(emitter);
        communicationUseCase.sendMessage(randomUUID, TEST_MESSAGE);

        verify(emitter).send(TEST_MESSAGE);
        verify(emitters, never()).remove(randomUUID);
    }

    @Test
    @DisplayName("Występuje IOException, zamyka emitter i usuwa z mapy")
    void sendMessage_throwException_resultEmitterClosedAndRemoved() throws IOException {
        when(emitters.get(randomUUID)).thenReturn(emitter);
        doThrow(new IOException()).when(emitter).send(TEST_MESSAGE);
        communicationUseCase.sendMessage(randomUUID, TEST_MESSAGE);

        verify(emitter).completeWithError(any());
        verify(emitters).remove(randomUUID);
        verifyNoMoreInteractions(emitter);
    }

    @Test
    @DisplayName("Utworzony emitter dla UUID, zamyka emitter, usuwa z mapy")
    void closeEmitter_correctEmitter_resultEmitterClosedAndDeleted(){
        when(emitters.get(randomUUID)).thenReturn(emitter);
        communicationUseCase.closeConnection(randomUUID);

        verify(emitters).remove(randomUUID);
        verify(emitter).complete();
    }

    @Test
    @DisplayName("Nieutworzony emitter dla UUID, nic nie robi")
    void closeEmitter_emitterNotExist_resultNothingHappened(){
        when(emitters.get(randomUUID)).thenReturn(null);
        communicationUseCase.closeConnection(randomUUID);

        verify(emitters, never()).remove(randomUUID);
    }

    @Test
    @DisplayName("Występuje IOException, zamyka emitter i usuwa z mapy")
    void closeConnection_throwException_resultEmitterClosedAndDeleted() throws IOException {
        when(emitters.get(randomUUID)).thenReturn(emitter);
        doThrow(new IOException()).when(emitter).send("CLOSE");
        communicationUseCase.closeConnection(randomUUID);

        verify(emitters).remove(randomUUID);
        verify(emitter).complete();
        verifyNoMoreInteractions(emitter);
    }
}
