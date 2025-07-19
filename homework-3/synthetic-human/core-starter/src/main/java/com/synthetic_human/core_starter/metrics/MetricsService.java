package com.synthetic_human.core_starter.metrics;

import com.synthetic_human.core_starter.command.Command;
import com.synthetic_human.core_starter.command.CommandQueue;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

@Service
public class MetricsService {
    private final MeterRegistry meterRegistry;


    public MetricsService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public void countCommandProcessed(Command command) {
        meterRegistry.counter("commands.processed", "author", command.author()).increment();
    }

    public void registerCommandQueue(CommandQueue commandQueue) {
        Gauge gauge = Gauge.builder("commands.in.queue", commandQueue, CommandQueue::size)
                .register(meterRegistry);
    }

}
