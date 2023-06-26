package com.example.go_gruppe1.model.command;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Stack;

public class Game {
    private SimpleBoard board;
    private final StringProperty description;
    private final Stack<Command> undoStack;
    private final Stack<Command> redoStack;

    /**
     * @param boardSize size of board
     *
     * initiates new game with selected size
     */
    public Game(int boardSize) {
        this.board = new SimpleBoard(boardSize);
        this.description = new SimpleStringProperty("Something!!");
        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();
    }

    /**
     * @param command command to be executed
     * @return 0 if stone placement is fine, 1 if it is suicide and 2 if ko logic is violated
     */
    public int executeCommand(Command command) {
        undoStack.push(command);
        redoStack.clear();
        if(command.execute() == 1)
            return 1;
        board = command.getBoard();
        description.set(command.getDescription());
        return 0;
    }

    /**
     * puts board in state before last move and adds it to redoStack
     */
    public void undoLastMove() {
        if (!undoStack.isEmpty()) {
            Command command = undoStack.pop();
            command.undo();
            board = command.getBoard();
            description.set(command.getDescription());
            redoStack.push(command);
        }
    }

    /**
     * repeats last move and adds it to undoStack
     */
    public void redoLastMove() {
        if (!redoStack.isEmpty()) {
            Command command = redoStack.pop();
            command.execute();
            board = command.getBoard();
            description.set(command.getDescription());
            undoStack.push(command);
        }
    }

    /**
     * @return board of game
     */
    public SimpleBoard getBoard(){
        return board;
    }

    protected StringProperty getDescription(){
        return description;
    }
}

