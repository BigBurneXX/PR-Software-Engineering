package com.example.go_gruppe1.model.command;

import javafx.scene.paint.Color;

public class RemoveStoneCommand implements Command {
    private final Board board;
    private final int x;
    private int y;

    public RemoveStoneCommand(Board board, int x, int y) {
        this.board = board;
        this.x = x;
        this.y = y;
    }

    public void execute() {
        board.removeStone(x, y);
    }

    public void undo() {
        board.placeStone(x, y, Color.TRANSPARENT);
    }
}

