package com.example.go_gruppe1.model.command;

import javafx.beans.property.StringProperty;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class GameHandler {
    private final Game game;

    /**
     * @param boardSize size of board
     *
     * initiates gameHandler for game with selected board size
     */
    public GameHandler(int boardSize){
        game = new Game(boardSize);
    }

    /**
     * @param row location of move to be made
     * @param col location of move to be made
     * @param color color of stone to be set
     * @return 0 if stone placement is fine, 1 if stone placement is suicide and 2 if stone placement violates ko logic
     */
    public int addMove(int row, int col, Color color){
        return addMove(row, col, color, "");
    }
    public int addMove(int row, int col, Color color, String description){
        // 0 if move is fine
        // 1 if move is suicide
        // 2 if move violates ko logic
        return game.executeCommand(new PlaceStoneCommand(game.getBoard(), row, col, color, description));
    }

    /**
     * executes the last move again
     */
    public void redo(){
        game.redoLastMove();
    }

    /**
     * puts the game in the state before last move (for navigation)
     */
    public void undo(){
        game.undoLastMove();
    }

    /**
     * @return board of game
     */
    public SimpleBoard getBoard(){
        return game.getBoard();
    }
    public StringProperty getDescription() {return game.getDescription();}
}
