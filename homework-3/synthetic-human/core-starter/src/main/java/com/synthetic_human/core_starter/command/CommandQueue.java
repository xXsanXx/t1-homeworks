package com.synthetic_human.core_starter.command;

public interface CommandQueue {
    void add(Command command);

    int size();
}
