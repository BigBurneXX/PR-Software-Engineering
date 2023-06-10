package com.example.go_gruppe1.model.command;

import com.example.go_gruppe1.controller.boardMaskController;
import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SimpleBoard {
    boardMaskController controller;
    int size;
    Color[][] board;
    boolean[][] checked;

    public SimpleBoard(boardMaskController controller, int size){
        this.controller = controller;
        this.size = size;
        board = new Color[size][size];
        checked = new boolean[size][size];
        System.out.println(size + "!!!");
    }

    public boolean setStone(int row, int col, Color color){
        Color[][] temporaryBoard = Arrays.stream(board).map(Color[]::clone).toArray(Color[][]::new);
        board[row][col] = color;
        System.out.println("stone set at " + row + ", " + col);
        Color toDelete = color == Color.BLACK  ? Color.WHITE : Color.BLACK;
        removeDead(toDelete);
        if(checkLiberties(row, col) == 0){
            board = temporaryBoard;
            return true;
        }
        toDelete = toDelete == Color.BLACK ? Color.WHITE : Color.BLACK;
        removeDead(toDelete);
        return false;
    }

    private void removeStone(int row, int col){
        board[row][col] = null;
    }

    private void removeDead(Color color){
        Set<Position> toDelete = new HashSet<>();
        for(int r = 0; r < size; r++){
            for(int c = 0; c < size; c++){
                checked = new boolean[size][size];
                if(checkLiberties(r, c) == 0 && board[r][c] == color)
                    toDelete.add(new Position(r, c));
            }
        }
        for(Position p : toDelete){
            removeStone(p.row(), p.col());
        }
    }

    private int checkLiberties(int row, int col){
        checked[row][col] = true;
        int liberties = 0;
        if(row > 0)
            if(board[row-1][col] == null)
                liberties++;
            else if(board[row][col] == board[row-1][col] && !checked[row-1][col])
                liberties += checkLiberties(row-1, col);
        if(col+1 < size)
            if(board[row][col+1] == null)
                liberties++;
            else if(board[row][col] == board[row][col+1] && !checked[row][col+1])
                liberties += checkLiberties(row, col+1);
        if(row+1 < size)
            if(board[row+1][col] == null)
                liberties++;
            else if(board[row][col] == board[row+1][col] && !checked[row+1][col])
                liberties += checkLiberties(row+1, col);
        if(col > 0)
            if(board[row][col-1] == null)
                liberties++;
            else if(board[row][col] == board[row][col-1] && !checked[row][col-1])
                liberties += checkLiberties(row, col-1);
        return liberties;
    }

    public Color[][] getBoard(){
        return board;
    }
}
