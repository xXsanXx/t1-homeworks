package com.synthetic_human.core_starter.command;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolCommandQueue implements CommandQueue {

    private final CommandExecutor commandExecutor;

    private final BlockingQueue<Runnable> commandQueue = new ArrayBlockingQueue<>(7);

    private final ThreadPoolExecutor executor = new ThreadPoolExecutor(
            2,
            4,
            10, TimeUnit.SECONDS,
            commandQueue,
            new ThreadPoolExecutor.AbortPolicy()
    );

    public ThreadPoolCommandQueue(CommandExecutor commandExecutor) {
        this.commandExecutor = commandExecutor;
    }

    @Override
    public void add(Command command) {
        executor.execute(() -> commandExecutor.execute(command));
    }

    @Override
    public int size() {
        return commandQueue.size();
    }
}
