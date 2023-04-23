package com.example.go_gruppe1;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class BoardLogicControl {
    private final StoneGroup[][] board;

    private List<StoneGroup> stoneList = new ArrayList<>();
    private final int size;
    private final boardMaskController controller;
    protected BoardLogicControl(boardMaskController controller, int boardSize) {
        this.controller = controller;
        board = new StoneGroup[boardSize+1][boardSize+1];
        size = boardSize;
    }

    private StoneGroup searchForStone(int row, int col){
        if(stoneList == null)
            return null;
        for (StoneGroup s: stoneList) {
            for (Position p: s.getPosition())
                if (p.row() == row && p.col() == col)
                    return s;
        }
        return null;
    }
    protected void setStoneToList(Color colour, int row, int col){
        StoneGroup toAdd = new StoneGroup(this, colour, row, col);
        StoneGroup neighbour;

        //check upper neighbour
        if(row - 1 > 0){
            neighbour = searchForStone(row-1, col);
            if(neighbour == null)
                toAdd.addLiberty();
            else if(neighbour.getColour() == colour) {
                neighbour.takeLiberty();
                System.out.println("Stone at " + (row) + ", " + (col+1) + " has " + toAdd.getLiberties() + " liberties.");
                neighbour.addPosition(row, col);
            } else {
                neighbour.takeLiberty();
                System.out.println("Stone at " + (row) + ", " + (col+1) + " has " + toAdd.getLiberties() + " liberties.");
            }
            System.out.println("Stone at " + (row+1) + ", " + (col+1) + " has " + toAdd.getLiberties() + " liberties.");
        }

        //check right neighbour
        if(col + 1 < size){
            neighbour = searchForStone(row, col+1);
            if(neighbour == null)
                toAdd.addLiberty();
            else if(neighbour.getColour() == colour) {
                neighbour.takeLiberty();
                System.out.println("Stone at " + (row+1) + ", " + (col+2) + " has " + toAdd.getLiberties() + " liberties.");
                neighbour.addPosition(row, col);
            } else {
                neighbour.takeLiberty();
                System.out.println("Stone at " + (row+1) + ", " + (col+2) + " has " + toAdd.getLiberties() + " liberties.");
            }
            System.out.println("Stone at " + (row+1) + ", " + (col+1) + " has " + toAdd.getLiberties() + " liberties.");
        }

        //check lower neighbour
        if (row + 1 < size) {
            neighbour = searchForStone(row+1,col);
            if(neighbour == null)
                toAdd.addLiberty();
            else if(neighbour.getColour() == colour) {
                neighbour.takeLiberty();
                System.out.println("Stone at " + (row+1) + ", " + (col+2) + " has " + toAdd.getLiberties() + " liberties.");
                neighbour.addPosition(row, col);
            } else {
                neighbour.takeLiberty();
                System.out.println("Stone at " + (row+2) + ", " + (col+1) + " has " + toAdd.getLiberties() + " liberties.");
            }
            System.out.println("Stone at " + (row+1) + ", " + (col+1) + " has " + toAdd.getLiberties() + " liberties.");
        }

        //check left neighbour
        if(col - 1 > 0){
            neighbour = searchForStone(row, col-1);
            if(neighbour == null)
                toAdd.addLiberty();
            else if(neighbour.getColour() == colour) {
                neighbour.takeLiberty();
                System.out.println("Stone at " + (row+1) + ", " + (col) + " has " + toAdd.getLiberties() + " liberties.");
                neighbour.addPosition(row, col);
            } else {
                neighbour.takeLiberty();
                System.out.println("Stone at " + (row+1) + ", " + (col) + " has " + toAdd.getLiberties() + " liberties.");
            }
            System.out.println("Stone at " + (row+1) + ", " + (col+1) + " has " + toAdd.getLiberties() + " liberties.");
        }

        if(searchForStone(row, col) == null)
            stoneList.add(toAdd);
        for(int i = 0; i < size; i++) {
            System.out.print(i + " ");
            for (int j = 0; j < size; j++){
                StoneGroup print = searchForStone(i, j);
                System.out.print(print == null ? "null" : print.getColour());
                //System.out.print(board[i][j].getColour() + " ");
            }
            System.out.println();
        }
        for(StoneGroup s: stoneList){
            System.out.println(s.getColour());
            for(Position p: s.getPosition())
                System.out.print(" " + p.row() + p.col());
            System.out.println();
        }
    }
    protected void setStone(Color colour, int row, int col){
        StoneGroup toAdd = new StoneGroup(this, colour, row, col);
        StoneGroup neighbour;
        boolean isPartOfGroup = false;

        //check upper neighbour
        if(row - 1 > 0){
            neighbour = board[row-1][col];
            if(neighbour == null)
                toAdd.addLiberty();
            else if(neighbour.getColour() == colour) {
                neighbour.takeLiberty();
                System.out.println("Stone at " + (row) + ", " + (col+1) + " has " + toAdd.getLiberties() + " liberties.");
                isPartOfGroup = true;
            } else {
                neighbour.takeLiberty();
                System.out.println("Stone at " + (row) + ", " + (col+1) + " has " + toAdd.getLiberties() + " liberties.");
            }
            System.out.println("Stone at " + (row+1) + ", " + (col+1) + " has " + toAdd.getLiberties() + " liberties.");
            if(isPartOfGroup) {
                neighbour.addPosition(row, col);
            }
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
                System.out.println("Stone at " + (row+1) + ", " + (col+2) + " has " + toAdd.getLiberties() + " liberties.");
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
                System.out.println("Stone at " + (row+2) + ", " + (col+1) + " has " + toAdd.getLiberties() + " liberties.");
            }
            System.out.println("Stone at " + (row+1) + ", " + (col+1) + " has " + toAdd.getLiberties() + " liberties.");
        }

        //check left neighbour
        if(col - 1 > 0){
            neighbour = board[row][col-1];
            if(neighbour == null)
                toAdd.addLiberty();
            else if(neighbour.getColour() == colour) {
                isPartOfGroup = true;
            } else {
                neighbour.takeLiberty();
                System.out.println("Stone at " + (row+1) + ", " + (col) + " has " + toAdd.getLiberties() + " liberties.");
            }
            System.out.println("Stone at " + (row+1) + ", " + (col+1) + " has " + toAdd.getLiberties() + " liberties.");
        }
        board[row][col] = toAdd;
        for(int i = 0; i < board.length; i++) {
            System.out.print(i + " ");
            for (int j = 0; j < board[i].length; j++){
                System.out.print(board[i][j] == null ? "null" : board[i][j].getColour());
                //System.out.print(board[i][j].getColour() + " ");
            }
            System.out.println();
        }
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
