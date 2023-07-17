package com.example.go_gruppe1.model.command;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.paint.Color;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class Game {
    private SimpleBoard board;
    private final Set<Position> beforeTwo = new HashSet<>();
    private final Set<Position> beforeOne = new HashSet<>();
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
        for(int row = 0; row < board.getBoard().length; row++) {
            for (int col = 0; col < board.getBoard()[row].length; col++)
                System.out.print(board.getBoard()[row][col] + "\t\t");
            System.out.println();
        }
        undoStack.push(command);
        redoStack.clear();
        if(command.execute() == 1)
            return 1;
        if(checkForKo(command)) {
            for(int row = 0; row < command.getBoard().getBoard().length; row++) {
                for (int col = 0; col < command.getBoard().getBoard()[row].length; col++)
                    System.out.print(command.getBoard().getBoard()[row][col] + "\t\t");
                System.out.println();
            }
            return 2;
        }
        board = command.getBoard();
        description.set(command.getDescription());
        return 0;
    }

    /**
     * @param command command to be executed
     * @return true if ko is violated, false if stone placement is fine
     */
    private boolean checkForKo(Command command) {
        Set<Position> currentSet = new HashSet<>();
        Color[][] currentBoard = command.getBoard().getBoard();
        for(int row = 0; row < currentBoard.length; row++)
            for(int col = 0; col < currentBoard[row].length; col++)
                if(currentBoard[row][col] != null)
                    currentSet.add(new Position(row, col));
        if(beforeOne.isEmpty())
            beforeOne.addAll(currentSet);
        else if(beforeTwo.isEmpty())
            beforeTwo.addAll(currentSet);
        else
            if(koViolated(command, currentSet))
                return true;
        beforeTwo.clear();
        beforeTwo.addAll(beforeOne);
        beforeOne.clear();
        beforeOne.addAll(currentSet);
        return false;
    }

    /**
     * @param command command to be executed
     * @param currentSet set of current positions
     * @return true if ko is violated, false if stone placement is fine
     */
    private boolean koViolated(Command command, Set<Position> currentSet){
        Color[][] testBoard = command.getBoard().getBoard();
        for(int row = 0; row < testBoard.length; row++)
            for(int col = 0; col < testBoard[row].length; col++)
                if(beforeTwo.equals(currentSet))
                    return true;
        return false;
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

    /**
     * @return description of the current board state
     */
    protected StringProperty getDescription(){
        return description;
    }
}

