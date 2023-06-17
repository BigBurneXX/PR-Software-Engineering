package com.example.go_gruppe1.model.command;

import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SimpleBoard {
    private final int size;
    private final Color[][] board;
    private int blackTrapped = 0;
    private int whiteTrapped = 0;
    private final Set<Position> toDelete = new HashSet<>();

    public SimpleBoard(int size){
        this.size = size;
        board = new Color[size][size];
    }

    public SimpleBoard(SimpleBoard toCopy){
        this.size = toCopy.size;
        //chatGPT suggests Arrays.copyOf();
        this.board = Arrays.stream(toCopy.board).map(Color[]::clone).toArray(Color[][]::new);
        this.blackTrapped = toCopy.getBlackTrapped();
        this.whiteTrapped = toCopy.getWhiteTrapped();
    }

    public boolean setStone(int row, int col, Color color){
        board[row][col] = color;
        System.out.println("stone set at " + row + ", " + col);
        Color toDelete = (color == Color.BLACK)  ? Color.WHITE : Color.BLACK;
        removeDead(toDelete);
        if(checkLiberties(row, col, new boolean[size][size]) == 0){
            removeStone(row, col);
            return true;
        }
        toDelete = (toDelete == Color.BLACK) ? Color.WHITE : Color.BLACK;
        removeDead(toDelete);
        return false;
    }

    private void removeStone(int row, int col){
        board[row][col] = null;
    }

    private void removeDead(Color color){
        for(int r = 0; r < size; r++)
            for(int c = 0; c < size; c++)
                if(checkLiberties(r, c, new boolean[size][size]) == 0 && board[r][c] == color)
                    toDelete.add(new Position(r, c));

        for(Position p : toDelete)
            removeStone(p.row(), p.col());

        if(color == Color.BLACK)
            whiteTrapped += toDelete.size();
        else
            blackTrapped += toDelete.size();
        toDelete.clear();
    }

    private int checkLiberties(int row, int col, boolean[][] checked){
        checked[row][col] = true;
        int liberties = 0;

        //check upper neighbour
        if(row > 0)
            if(board[row-1][col] == null)
                liberties++;
            else if(board[row][col] == board[row-1][col] && !checked[row-1][col])
                liberties += checkLiberties(row-1, col, checked);

        //check left neighbour
        if(col+1 < size)
            if(board[row][col+1] == null)
                liberties++;
            else if(board[row][col] == board[row][col+1] && !checked[row][col+1])
                liberties += checkLiberties(row, col+1, checked);

        //check lower neighbour
        if(row+1 < size)
            if(board[row+1][col] == null)
                liberties++;
            else if(board[row][col] == board[row+1][col] && !checked[row+1][col])
                liberties += checkLiberties(row+1, col, checked);

        //check left neighbour
        if(col > 0)
            if(board[row][col-1] == null)
                liberties++;
            else if(board[row][col] == board[row][col-1] && !checked[row][col-1])
                liberties += checkLiberties(row, col-1, checked);
        return liberties;
    }

    public Color[][] getBoard(){
        return board;
    }

    public int getBlackTrapped() {
        return blackTrapped;
    }

    public int getWhiteTrapped() {
        return whiteTrapped;
    }
}
