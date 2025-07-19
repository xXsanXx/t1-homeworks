package com.synthetic_human.core_starter.command;

import com.synthetic_human.core_starter.metrics.MetricsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommandService implements CommandExecutor {

    private final MetricsService metricsService;

    private final Logger logger = LoggerFactory.getLogger(CommandService.class);

    private final CommandQueue commandQueue = new ThreadPoolCommandQueue(this);

    @Autowired
    public CommandService(MetricsService metricsService) {
        this.metricsService = metricsService;

        logger.debug("registered queue");
        metricsService.registerCommandQueue(commandQueue);
    }

    public void add(Command command) {

        command.validate();

        if (command.priority() == CommandPriority.CRITICAL) execute(command);
        else commandQueue.add(command);
    }

    @Override
    public void execute(Command command) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        logger.info("Executing {}", command);
        metricsService.countCommandProcessed(command);
    }


}
