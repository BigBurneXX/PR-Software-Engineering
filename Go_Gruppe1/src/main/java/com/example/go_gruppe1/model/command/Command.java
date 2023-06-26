package com.example.go_gruppe1.model.command;

public interface Command {
    int execute();
    void undo();

    SimpleBoard getBoard();
    String getDescription();
}
