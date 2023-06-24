package com.example.go_gruppe1.model.command;

public interface Command {
    boolean execute();
    void undo();
    SimpleBoard getBoard();
}
