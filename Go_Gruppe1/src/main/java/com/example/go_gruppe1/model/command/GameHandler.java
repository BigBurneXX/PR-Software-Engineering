package com.example.go_gruppe1.model.command;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class GameHandler {
    private final Game game;

    /**
     * @param boardSize size of board
     *
     * initiates gamehandler for game with selected board size
     */
    public GameHandler(int boardSize){
        game = new Game(boardSize);
    }

    /**
     * @param row location of move to be made
     * @param col location of move to be made
     * @param color color of stone to be set
     * @return true if stone is trapped and removed
     */
    public boolean addMove(int row, int col, Color color){
        return game.executeCommand(new PlaceStoneCommand(game.getBoard(), row, col, color));
    }

    /**
     * executes the last move again
     */
    public void redo(){
        game.redoLastMove();
    }

    /**
     * puts the game in the state before last move (for navigation)
     */
    public void undo(){
        game.undoLastMove();
    }

    /**
     * @return board of game
     */
    public SimpleBoard getBoard(){
        return game.getBoard();
    }

    private ArrayList<ArrayList<Position>> getLibertyIslands() {
        Color[][] board = getBoard().getBoard();
        ArrayList<ArrayList<Position>> allLibertyGroups = new ArrayList<>();

        for(int row = 0; row < board.length; row++) {
            for(int col = 0; col < board[row].length; col++) {
                if(board[row][col] == null) {
                    ArrayList<Position> libertyGroup = new ArrayList<>();
                    isPartOfStoneGroup(board, row, col, libertyGroup);
                    allLibertyGroups.add(libertyGroup);
                }
            }
        }
        return allLibertyGroups;
    }

    private void isPartOfStoneGroup(Color[][] board, int row, int col, ArrayList<Position> libertyGroup) {
        if(row < 0 || row >= board.length || col < 0 || col >= board[row].length || board[row][col] == Color.BLACK || board[row][col] == Color.WHITE || board[row][col] == Color.PINK) {
            return;
        }

        board[row][col] = Color.PINK; //just any color other than black, white or transparent to mark it as checked
        libertyGroup.add(new Position(row, col));
        isPartOfStoneGroup(board, row + 1, col, libertyGroup); //upper
        isPartOfStoneGroup(board, row - 1, col, libertyGroup); //lower
        isPartOfStoneGroup(board, row, col - 1, libertyGroup); //left
        isPartOfStoneGroup(board, row, col + 1, libertyGroup); //right
    }

    //checks whether an island of liberties is a territory of black or white or neither
    //returns transparent if neither
    private Color isTerritory(ArrayList<Position> libertyGroup) {
        int counter = 0;
        Color firstFoundColor = Color.TRANSPARENT;
        for(Position liberty : libertyGroup) {
               if(neighbourColors(liberty) != null) {
                   if(neighbourColors(liberty) == Color.TRANSPARENT) {
                       return Color.TRANSPARENT;
                   }
                   if(counter == 0) {
                       firstFoundColor = neighbourColors(liberty);
                   } else {
                       if(neighbourColors(liberty) != firstFoundColor) {
                           return Color.TRANSPARENT;
                       }
                   }
                   counter++;
               }

        }
        return firstFoundColor;
    }

    //returns black if position only has black or no neighbours
    //returns white if position only has white or no neighbours
    //returns transparent if position has black and white as neighbour
    private Color neighbourColors(Position position) {
        Color[][] board = game.getBoard().getBoard();

        if(position.row() < 0 || position.row() > board.length ||
                position.col() < 0 || position.col() > board[position.row()].length) {
            return null;
        }

        List<Color> neighbours = new ArrayList<>();

        //up
        if(position.row() - 1 >= 0 && board[position.row() - 1][position.col()] != null
        && board[position.row() - 1][position.col()] != Color.PINK) {
            neighbours.add(board[position.row() - 1][position.col()]);
        }

        //down
        if(position.row() + 1 < board.length && board[position.row() + 1][position.col()] != null
        && board[position.row() + 1][position.col()] != Color.PINK) {
            neighbours.add(board[position.row() + 1][position.col()]);
        }

        //left
        if(position.col() - 1 >=0 && board[position.row()][position.col() - 1] != null
        && board[position.row()][position.col() - 1] != Color.PINK) {
            neighbours.add(board[position.row()][position.col() - 1]);
        }

        //right
        if(position.col() + 1 < board[position.row()].length && board[position.row()][position.col() + 1] != null
        && board[position.row()][position.col() + 1] != Color.PINK) {
            neighbours.add(board[position.row()][position.col() + 1]);
        }

        Color firstColor;

        if(!neighbours.isEmpty()) {
            firstColor = neighbours.get(0);
            for(Color c : neighbours) {
                if(c != firstColor) { //found black & white as neighbours
                    return Color.TRANSPARENT;
                } else { //only found one color as neighbours
                    return firstColor;
                }
            }
        }
        return null; //no stone neighbours
    }

    public int getTerritoryScore(Color c) {
        int score = 0;

        for(ArrayList<Position> libertyGroup : getLibertyIslands()) {
            if(isTerritory(libertyGroup) == c) {
                score += libertyGroup.size();
            }
        }
        return score;
    }
}
