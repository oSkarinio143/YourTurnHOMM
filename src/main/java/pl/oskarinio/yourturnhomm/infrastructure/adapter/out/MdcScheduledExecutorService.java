package pl.oskarinio.yourturnhomm.infrastructure.adapter.out;

import org.springframework.stereotype.Service;
import pl.oskarinio.yourturnhomm.domain.port.out.MdcScheduledExecutor;
import pl.oskarinio.yourturnhomm.infrastructure.usecase.communication.MdcScheduledExecutorUseCase;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Service
public class MdcScheduledExecutorService implements MdcScheduledExecutor {
    private final MdcScheduledExecutorUseCase mdcScheduledExecutorUseCase;

    public MdcScheduledExecutorService() {
        this.mdcScheduledExecutorUseCase = new MdcScheduledExecutorUseCase(Executors.newSingleThreadScheduledExecutor());
    }

    @Override
    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
        return mdcScheduledExecutorUseCase.schedule(command,delay,unit);
    }
}
