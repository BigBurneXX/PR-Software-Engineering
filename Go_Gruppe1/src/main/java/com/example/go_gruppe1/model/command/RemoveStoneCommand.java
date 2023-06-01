package com.example.go_gruppe1.model.command;

import javafx.scene.paint.Color;

public class RemoveStoneCommand implements Command {
    private final Board board;
    private final int row;
    private final int col;

    public RemoveStoneCommand(Board board, int row, int col) {
        this.board = board;
        this.row = row;
        this.col = col;
    }

    public void execute() {
        board.removeStone(row, col);
    }

    //should definitely be the color of the stone to be placed
    public void undo() {
        board.placeStone(row, col, Color.TRANSPARENT);
    }
}

