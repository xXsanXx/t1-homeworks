package com.synthetic_human.core_starter.command;

import org.springframework.stereotype.Service;

@Service
public class CommandService implements CommandExecutor {

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
        System.out.printf("Executing %s", command);
    }


}
