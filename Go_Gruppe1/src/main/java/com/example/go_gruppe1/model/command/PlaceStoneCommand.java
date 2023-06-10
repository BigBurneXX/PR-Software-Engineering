package com.example.go_gruppe1.model.command;

import com.example.go_gruppe1.oldClasses.Board;
import javafx.scene.paint.Color;

public class PlaceStoneCommand implements Command {
    private Board board;
    private final Board beforeChange;
    private final int row;
    private final int col;
    private final Color color;

    public PlaceStoneCommand(Board board, int row, int col, Color color) {
        this.beforeChange = board;
        this.board = board;
        this.row = row;
        this.col = col;
        this.color = color;
    }

    public void execute() {
        board.placeStone(row, col, color);
    }

    public void undo() {
        board = new Board(beforeChange);
    }

    public Board getBoard(){
        return board;
    }
}
