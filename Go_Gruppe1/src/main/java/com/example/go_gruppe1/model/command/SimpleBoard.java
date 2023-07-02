package com.example.go_gruppe1.model.command;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SimpleBoard {
    private final int size;
    private final Color[][] board;
    private int blackTrapped = 0;
    private int whiteTrapped = 0;

    public int blackTotal = 0;

    public int whiteTotal = 0;
    private final Set<Position> toDelete = new HashSet<>();

    /**
     * @param size size of board
     *
     * initiates a new empty board
     */
    public SimpleBoard(int size){
        this.size = size;
        board = new Color[size][size];
    }

    /**
     * @param toCopy board to be copied
     *
     * initiates a board from a given game state
     */
    public SimpleBoard(SimpleBoard toCopy){
        this.size = toCopy.size;
        this.board = Arrays.stream(toCopy.board).map(Color[]::clone).toArray(Color[][]::new);
        this.blackTrapped = toCopy.getTrapped(Color.BLACK);
        this.whiteTrapped = toCopy.getTrapped(Color.WHITE);
    }

    /**
     * @param row location of stone to be set
     * @param col location of stone to be set
     * @param color color of stone to be set
     *
     * @return 1 if stone placement would be suicide and 0 otherwise
     *
     * sets stone to board and deletes dead stones
     */
    public int setStone(int row, int col, Color color){
        board[row][col] = color;
        System.out.println("stone set at " + row + ", " + col);
        Color toDelete = (color == Color.BLACK)  ? Color.WHITE : Color.BLACK;
        removeDead(toDelete);
        if(checkLiberties(row, col, new boolean[size][size]) == 0){
            removeStone(row, col);
            return 1;
        }
        toDelete = (toDelete == Color.BLACK) ? Color.WHITE : Color.BLACK;
        removeDead(toDelete);
        return 0;
    }

    /**
     * @param row location of stone to be removed
     * @param col location of stone to be removed
     *
     * removes stone from board
     */
    private void removeStone(int row, int col){
        board[row][col] = null;
    }

    /**
     * @param color color of stone to be deleted
     *
     * adds dead stones to list and removes them from board and therefore increments trapped counters
     */
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

    /**
     * @param row location of stone to be checked
     * @param col location of stone to be checked
     * @param checked matrix to keep track of checked positions
     * @return number of liberties
     *
     * checks number of liberties of a position on board
     */
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

    /**
     * @return board
     *
     * returns board with set colors black & white & null if no stone is set
     */
    public Color[][] getBoard(){
        return board;
    }

    /**
     * @param color color of trapped stones
     * @return number of trapped stones
     *
     * returns trapped counter of a player
     */
    public int getTrapped(Color color) {
        return color.equals(Color.BLACK) ? blackTrapped : whiteTrapped;
    }

    public void calcScores(double komi) {
        blackTotal += blackTrapped;
        whiteTrapped += whiteTrapped + komi;

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if(board[row][col] == Color.BLACK) {
                    blackTotal++;
                } else if(board[row][col] == Color.WHITE) {
                    whiteTotal++;
                }
            }
        }

        if(blackTotal == 0 || whiteTotal == 0) {
            return;
        }

        boolean[][] visited = new boolean[size][size];
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                visited[row][col] = false;
            }
        }

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if(!visited[row][col] && board[row][col] == null) {
                    ArrayList<Position> libertyArea = new ArrayList<>();
                    boolean blackSurrounding = true;
                    boolean whiteSurrounding = true;
                    findLibertyArea(row, col, libertyArea, visited);
                    for(Position p : libertyArea) {
                        if(!isSurrounded(p.row(), p.col(), Color.BLACK)) {
                            blackSurrounding = false;
                        }
                        if(!isSurrounded(p.row(), p.col(), Color.WHITE)) {
                            whiteSurrounding = false;
                        }
                        if(!blackSurrounding && !whiteSurrounding) {
                            break;
                        }
                    }

                    if(blackSurrounding) {
                        blackTotal += libertyArea.size();
                    } else if(whiteSurrounding) {
                        whiteTotal += libertyArea.size();
                    }

                }
            }
        }
    }

    private boolean isSurrounded(int row, int col, Color c) {
        boolean isSurrounded;
        isSurrounded = checkNeighbours(row + 1, col, c);
        isSurrounded &= checkNeighbours(row - 1, col, c);
        isSurrounded &= checkNeighbours(row, col + 1, c);
        isSurrounded &= checkNeighbours(row, col - 1, c);
        return isSurrounded;

    }

    private boolean checkNeighbours(int row, int col, Color c) {
        if(row < 0 || row >= size || col < 0 || col >= size) {
            return true;
        }
        return board[row][col] == null || board[row][col] == c;
    }

    private void findLibertyArea(int row, int col, ArrayList<Position> libertyArea, boolean[][] visited) {
        if (row < 0 || row >= size || col < 0 || col >= size) {
            return;
        }
        if (visited[row][col]) {
            return;
        }
        if (board[row][col] != null) {
            return;
        }

        visited[row][col] = true;
        libertyArea.add(new Position(row, col));
        findLibertyArea(row + 1, col, libertyArea, visited);
        findLibertyArea(row - 1, col, libertyArea, visited);
        findLibertyArea(row, col + 1, libertyArea, visited);
        findLibertyArea(row, col - 1, libertyArea, visited);
    }
}
