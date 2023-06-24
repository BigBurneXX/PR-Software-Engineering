package com.example.go_gruppe1.model.command;

import java.util.Stack;

public class Game {
    private SimpleBoard board;
    private final Stack<Command> undoStack;
    private final Stack<Command> redoStack;

    /**
     * @param boardSize size of board
     *
     * initiates new game with selected size
     */
    public Game(int boardSize) {
        this.board = new SimpleBoard(boardSize);
        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();
    }

    /**
     * @param command command to be executed
     * @return true if command could be executed
     *
     * adds command to undo stack and clears redo stack for game tracking
     */
    public boolean executeCommand(Command command) {
        undoStack.push(command);
        redoStack.clear();
        return command.execute();
    }

    /**
     * resets game to state before last move
     */
    public void undoLastMove() {
        if (!undoStack.isEmpty()) {
            Command command = undoStack.pop();
            command.undo();
            board = new SimpleBoard(command.getBoard());
            redoStack.push(command);
        }
    }

    /**
     * repeats the last move of the game
     */
    public void redoLastMove() {
        if (!redoStack.isEmpty()) {
            Command command = redoStack.pop();
            command.execute();
            board = command.getBoard();
            undoStack.push(command);
        }
    }

    /**
     * @return board of the game
     */
    public SimpleBoard getBoard(){
        return board;
    }
}

