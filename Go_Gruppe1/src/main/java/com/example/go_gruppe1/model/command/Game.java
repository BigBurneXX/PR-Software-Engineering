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
     * @return true if stone is trapped and removed
     */
    public boolean executeCommand(Command command) {
        undoStack.push(command);
        redoStack.clear();
        boolean isSuicide = command.execute();
        board = command.getBoard();
        return isSuicide;
    }

    /**
     * puts board in state before last move and adds it to redo stack
     */
    public void undoLastMove() {
        if (!undoStack.isEmpty()) {
            Command command = undoStack.pop();
            command.undo();
            board = command.getBoard();
            redoStack.push(command);
        }
    }

    /**
     * repeats last move and adds it to undostack
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
     * @return board of game
     */
    public SimpleBoard getBoard(){
        return board;
    }
}

