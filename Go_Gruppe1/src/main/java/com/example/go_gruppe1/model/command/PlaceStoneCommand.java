package com.example.go_gruppe1.model.command;

import javafx.scene.paint.Color;

public class PlaceStoneCommand implements Command {
    private SimpleBoard board;
    private final SimpleBoard beforeChange;
    private final int row;
    private final int col;
    private final Color color;
    private final String description;

    /**
     * @param board board of game
     * @param row location of stone to be placed
     * @param col location of stone to be placed
     * @param color color of stone to be placed
     *
     * initiates command when stone is to be placed
     */
    public PlaceStoneCommand(SimpleBoard board, int row, int col, Color color) {
        this(board, row, col, color, "");
    }

    public PlaceStoneCommand(SimpleBoard board, int row, int col, Color color, String description) {
        this.board = board;
        this.beforeChange = new SimpleBoard(board);
        this.row = row;
        this.col = col;
        this.color = color;
        this.description = description;
    }

    /**
     * @return 0 if stone placement is fine and 1 if stone placement is suicide
     *
     * sets stone
     */
    public int execute() {
        return board.setStone(row, col, color);
    }

    /**
     * set board to state before last move
     */
    public void undo() {
        board = beforeChange;
    }

    /**
     * @return board of game
     */
    public SimpleBoard getBoard() {
        return board;
    }

    public String getDescription() {
        return description;
    }
}
