package com.example.go_gruppe1.model.command;

import javafx.scene.paint.Color;

import java.util.Stack;

public class Game {
    private SimpleBoard board;
    private final Stack<Command> undoStack;
    private final Stack<Command> redoStack;

    public Game(int boardSize) {
        this.board = new SimpleBoard(boardSize);
        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();
    }

    public boolean executeCommand(Command command) {
        undoStack.push(command);
        redoStack.clear();
        return command.execute();
    }

    public void undoLastMove() {
        if (!undoStack.isEmpty()) {
            Command command = undoStack.pop();
            command.undo();
            board = new SimpleBoard(command.getBoard());
            redoStack.push(command);
        }
    }

    public void redoLastMove() {
        if (!redoStack.isEmpty()) {
            Command command = redoStack.pop();
            command.execute();
            board = command.getBoard();
            undoStack.push(command);
        }
    }

    public SimpleBoard getBoard(){
        return board;
    }
}

