package com.example.go_gruppe1.model.command;

import javafx.scene.paint.Color;

public class PlaceStoneCommand implements Command {
    private final Board board;
    private final int row;
    private final int col;
    private final Color color;

    public PlaceStoneCommand(Board board, int row, int col, Color color) {
        this.board = board;
        this.row = row;
        this.col = col;
        this.color = color;
    }

    public void execute() {
        board.placeStone(row, col, color);
    }

    public void undo() {
        board.removeStone(row, col);
    }
}
