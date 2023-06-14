package com.example.go_gruppe1.model;

import com.example.go_gruppe1.model.command.Game;
import com.example.go_gruppe1.model.command.PlaceStoneCommand;
import com.example.go_gruppe1.model.command.SimpleBoard;
import javafx.scene.paint.Color;

public class GameHandler {
    private final Game game;

    public GameHandler(int boardSize){
        game = new Game(boardSize);
    }

    public boolean addMove(int row, int col, Color color){
        return game.executeCommand(new PlaceStoneCommand(game.getBoard(), row, col, color));
    }

    public void redo(){
        game.redoLastMove();
    }

    public void undo(){
        game.undoLastMove();
    }

    public SimpleBoard getBoard(){
        return game.getBoard();
    }
}
