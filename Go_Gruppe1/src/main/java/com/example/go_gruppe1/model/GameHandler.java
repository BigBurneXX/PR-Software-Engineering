package com.example.go_gruppe1.model;

import com.example.go_gruppe1.model.command.Game;
import com.example.go_gruppe1.model.command.PlaceStoneCommand;
import com.example.go_gruppe1.model.command.Position;
import com.example.go_gruppe1.model.command.SimpleBoard;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class GameHandler {
    private final Game game;

    public GameHandler(int boardSize){
        game = new Game(boardSize);
    }

    public boolean addMove(int row, int col, Color color){
        return game.executeCommand(new PlaceStoneCommand(game.getBoard(), row, col, color));
    }

    public void redo(){
        game.redoLastMove();
    }

    public void undo(){
        game.undoLastMove();
    }

    public SimpleBoard getBoard(){
        return game.getBoard();
    }

    public ArrayList<ArrayList<Position>> getLibertyIslands() {
        Color[][] board = getBoard().getBoard();
        ArrayList<ArrayList<Position>> allLibertyGroups = new ArrayList<>();

        for(int row = 0; row < board.length; row++) {
            for(int col = 0; col < board[row].length; col++) {
                if(board[row][col] == null) {
                    ArrayList<Position> libertyGroup = new ArrayList<>();
                    libertyGroup.add(new Position(row, col));
                    allLibertyGroups.add(libertyGroup);
                    isPartOfStoneGroup(board, row, col);
                }
            }
        }
        return allLibertyGroups;
    }

    private void isPartOfStoneGroup(Color[][] board, int row, int col) {
        if(row < 0 || row >= board.length || col < 0 || col >= board[row].length || board[row][col] == Color.BLACK || board[row][col] == Color.WHITE || board[row][col] == Color.PINK) {
            return;
        }

        board[row][col] = Color.PINK; //just any color other than black, white or transparent to mark it as checked
        isPartOfStoneGroup(board, row + 1, col); //upper
        isPartOfStoneGroup(board, row - 1, col); //lower
        isPartOfStoneGroup(board, row, col - 1); //left
        isPartOfStoneGroup(board, row, col + 1); //right
    }

    //checks whether an island of liberties is a territory of black or white or neither
    //returns transparent if neither
    public Color isTerritory(ArrayList<Position> libertyGroup) {
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

        //down
        if(position.row() - 1 >= 0 && board[position.row() - 1][position.col()] != null) {
            neighbours.add(board[position.row() - 1][position.col()]);
        }

        //up
        if(position.row() + 1 < board.length && board[position.row() + 1][position.col()] != null) {
            neighbours.add(board[position.row() + 1][position.col()]);
        }

        //left
        if(position.col() - 1 >=0 && board[position.row()][position.col() - 1] != null) {
            neighbours.add(board[position.row()][position.col() - 1]);
        }

        //right
        if(position.col() + 1 < board[position.row()].length && board[position.row()][position.col() + 1] != null) {
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
}
