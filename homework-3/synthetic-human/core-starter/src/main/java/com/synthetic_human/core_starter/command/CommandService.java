package com.synthetic_human.core_starter.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CommandService implements CommandExecutor {

    private final Logger logger = LoggerFactory.getLogger(CommandService.class);

    private final CommandQueue commandQueue = new CommandQueue(this);

    public void add(Command command) {

        command.validate();

        if (command.priority() == CommandPriority.CRITICAL) execute(command);
        else commandQueue.add(command);
    }

    @Override
    public void execute(Command command) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        logger.info("Executing {}", command);
    }


}
