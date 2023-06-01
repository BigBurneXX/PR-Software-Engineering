package com.example.go_gruppe1.model.command;

import com.example.go_gruppe1.controller.boardMaskController;

import java.util.Stack;

public class Game {
    private final Board board;
    private final Stack<Command> undoStack;
    private final Stack<Command> redoStack;

    public Game(boardMaskController controller, int boardSize) {
        this.board = new Board(controller, boardSize);
        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();
    }

    public void executeCommand(Command command) {
        command.execute();
        undoStack.push(command);
        redoStack.clear();
    }

    public void undoLastMove() {
        if (!undoStack.isEmpty()) {
            Command command = undoStack.pop();
            command.undo();
            redoStack.push(command);
        }
    }

    public void redoLastMove() {
        if (!redoStack.isEmpty()) {
            Command command = redoStack.pop();
            command.execute();
            undoStack.push(command);
        }
    }

    public Board getBoard(){
        return board;
    }
}

