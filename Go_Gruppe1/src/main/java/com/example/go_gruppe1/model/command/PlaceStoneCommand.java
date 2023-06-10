package com.example.go_gruppe1.model.command;

import javafx.scene.paint.Color;

public class PlaceStoneCommand implements Command {
    private SimpleBoard board;
    private final SimpleBoard beforeChange;
    private final int row;
    private final int col;
    private final Color color;

    public PlaceStoneCommand(SimpleBoard board, int row, int col, Color color) {
        this.board = board;
        this.beforeChange = new SimpleBoard(board);
        this.row = row;
        this.col = col;
        this.color = color;
    }

    public boolean execute() {
        return board.setStone(row, col, color);
    }

    public void undo() {
        board = new SimpleBoard(beforeChange);
    }

    public SimpleBoard getBoard(){
        return board;
    }
}
