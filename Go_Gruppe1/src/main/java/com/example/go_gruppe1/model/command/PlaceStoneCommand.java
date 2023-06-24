package com.example.go_gruppe1.model.command;

import javafx.scene.paint.Color;

public class PlaceStoneCommand implements Command {
    private SimpleBoard board;
    private final SimpleBoard beforeChange;
    private final int row;
    private final int col;
    private final Color color;

    /**
     * @param board board of game
     * @param row location of stone to be set
     * @param col location of stone to be set
     * @param color color of stone to be set
     *
     * initiates a command for setting a stone
     */
    public PlaceStoneCommand(SimpleBoard board, int row, int col, Color color) {
        this.board = board;
        this.beforeChange = new SimpleBoard(board);
        this.row = row;
        this.col = col;
        this.color = color;
    }

    /**
     * @return true if stone could be set
     */
    public boolean execute() {
        return board.setStone(row, col, color);
    }

    /**
     * resets board to state before last move
     */
    public void undo() {
        board = new SimpleBoard(beforeChange);
    }

    /**
     * @return board of game
     */
    public SimpleBoard getBoard(){
        return board;
    }
}
