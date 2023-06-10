package com.example.go_gruppe1.model.command;

import com.example.go_gruppe1.oldClasses.Board;

public interface Command {
    void execute();
    void undo();
    Board getBoard();
}
