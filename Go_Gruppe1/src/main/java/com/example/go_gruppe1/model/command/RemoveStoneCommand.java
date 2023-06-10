package com.example.go_gruppe1.model.command;

import com.example.go_gruppe1.oldClasses.Board;
import javafx.scene.paint.Color;

public class RemoveStoneCommand implements Command {
    private Board board;
    private final Board beforeChange;
    private final int row;
    private final int col;
    private final Color color;

    public RemoveStoneCommand(Board board, int row, int col, Color color) {
        this.board = board;
        this.beforeChange = board;
        this.row = row;
        this.col = col;
        this.color = color;
    }

    public void execute() {
        //board.removeStone(row, col);
    }

    public void undo() {
        board.placeStone(row, col, color);
    }

    public Board getBoard(){
        return board;
    }
}

