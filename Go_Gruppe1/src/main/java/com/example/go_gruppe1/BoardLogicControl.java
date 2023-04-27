package com.example.go_gruppe1;

import javafx.scene.paint.Color;

public class BoardLogicControl {
    private final StoneGroup[][] board;
    private final int size;
    private final boardMaskController controller;
    protected BoardLogicControl(boardMaskController controller, int boardSize) {
        this.controller = controller;
        board = new StoneGroup[boardSize][boardSize];
        size = boardSize;
    }

    protected void setStone(Color colour, int row, int col){
        StoneGroup toAdd = new StoneGroup(this, colour, row, col);
        StoneGroup neighbour;
        boolean isPartOfGroup;

        //check upper neighbour
        if(!(row - 1 < 0)){
            neighbour = board[row-1][col];
            if(neighbour == null)
                toAdd.addLiberty();
            else if(neighbour.getColour() == colour) {
                isPartOfGroup = true;
            } else {
                neighbour.takeLiberty();
            }
            System.out.println("Stone at " + (row+1) + ", " + (col+1) + " has " + toAdd.getLiberties() + " liberties.");
        }

        //check right neighbour
        if(col + 1 < size){
            neighbour = board[row][col+1];
            if(neighbour == null)
                toAdd.addLiberty();
            else if(neighbour.getColour() == colour) {
                isPartOfGroup = true;
            } else {
                neighbour.takeLiberty();
            }
            System.out.println("Stone at " + (row+1) + ", " + (col+1) + " has " + toAdd.getLiberties() + " liberties.");
        }

        //check lower neighbour
        if (row + 1 < size) {
            neighbour = board[row + 1][col];
            if(neighbour == null)
                toAdd.addLiberty();
            else if(neighbour.getColour() == colour) {
                isPartOfGroup = true;
            } else {
                neighbour.takeLiberty();
            }
            System.out.println("Stone at " + (row+1) + ", " + (col+1) + " has " + toAdd.getLiberties() + " liberties.");
        }

        //check left neighbour
        if(!(col - 1 < 0)){
            neighbour = board[row][col-1];
            if(neighbour == null)
                toAdd.addLiberty();
            else if(neighbour.getColour() == colour) {
                isPartOfGroup = true;
            } else {
                neighbour.takeLiberty();
            }
            System.out.println("Stone at " + (row+1) + ", " + (col+1) + " has " + toAdd.getLiberties() + " liberties.");
        }
        board[row][col] = toAdd;
    }

    protected void deleteStone(StoneGroup toDelete){
        for (Position p: toDelete.getPosition()) {
            //StoneGroup stoneToDelete = board[p.row()][p.col()];

            //upper neighbour
            StoneGroup neighbour = board[p.row()-1][p.col()];
            if(neighbour != null && neighbour.getColour() != toDelete.getColour())
                neighbour.addLiberty();

            //right neighbour
            neighbour = board[p.row()][p.col()+1];
            if(neighbour != null && neighbour.getColour() != toDelete.getColour())
                neighbour.addLiberty();

            //lower neighbour
            neighbour = board[p.row()+1][p.col()];
            if(neighbour != null && neighbour.getColour() != toDelete.getColour())
                neighbour.addLiberty();

            //left neighbour
            neighbour = board[p.row()][p.col()-1];
            if(neighbour != null && neighbour.getColour() != toDelete.getColour())
                neighbour.addLiberty();

            //deleting stone at position p
            board[p.row()][p.col()] = null;
        }
        controller.deleteStoneGroup(toDelete);
    }
}