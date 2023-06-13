package com.example.go_gruppe1.oldClasses;

import com.example.go_gruppe1.controller.boardMaskController;
import com.example.go_gruppe1.model.command.Position;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Board {
    private int size;
    private final char[] ALPHABET = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T'};
    private final List<StoneGroup> stoneList = new ArrayList<>();
    private final Set<StoneGroup> toDelete = new HashSet<>();
    private boolean isPartOfGroup;
    private StoneGroup[][] stoneMapping;
    private boardMaskController controller;

    public Board(boardMaskController controller, int boardSize) {
        this.controller = controller;
        size = boardSize;
        stoneMapping = new StoneGroup[size][size];
    }

    public Board(Board board){
        this.controller = board.controller;
        this.size = board.size;
        this.stoneMapping = board.stoneMapping;
        this.stoneList.addAll(board.stoneList);
    }

    public void placeStone(int row, int col, Color colour) {
        //send feedback
        // Additional game logic for capturing opponent stones, etc.
        StoneGroup toAdd = new StoneGroup(this, colour, row, col);
        Position toAddPosition = new Position(row, col);
        isPartOfGroup = false;
        stoneMapping[row][col] = toAdd;
        stoneList.add(toAdd);

        //check upper neighbour
        if(row > 0)
            checkNeighbour(toAdd, toAddPosition, stoneMapping[row-1][col], new Position(row-1, col));
        //check right neighbour
        if(col + 1 < size)
            checkNeighbour(toAdd, toAddPosition, stoneMapping[row][col+1], new Position(row, col+1));
        //check lower neighbour
        if(row + 1 < size)
            checkNeighbour(toAdd, toAddPosition, stoneMapping[row+1][col], new Position(row+1, col));
        //check left neighbour
        if(col > 0)
            checkNeighbour(toAdd, toAddPosition, stoneMapping[row][col-1], new Position(row, col-1));

        //printSomething();
        stonesToDelete();
        toDelete.clear();
        if(stoneMapping[row][col] != null && stoneMapping[row][col].getFreeFields().isEmpty()) {
            deleteStone(toAdd);
            System.out.println("THIS IS SUICIDE");
        }
        //printSomething();
    }

    public void removeStone(Board board) {
        this.controller = board.controller;
        this.size = board.size;
        this.stoneMapping = board.stoneMapping;
        this.stoneList.clear();
        this.stoneList.addAll(board.stoneList);
        //stones[x][y] = Color.TRANSPARENT;
        // Additional game logic for restoring captured stones, etc.
    }

    private void checkNeighbour(StoneGroup toAdd, Position toAddPosition, StoneGroup neighbour, Position neighbourPosition){
        if(neighbour == null){
            if(isPartOfGroup)
                stoneMapping[toAddPosition.row()][toAddPosition.col()].addFreeField(neighbourPosition);
            else
                toAdd.addFreeField(neighbourPosition);
            //if neighbour has the same colour the current stone should be added to the neighbours group
        }else if(neighbour.getColour() == toAdd.getColour()) {
            if(isPartOfGroup) {
                StoneGroup listToCombine = stoneMapping[toAddPosition.row()][toAddPosition.col()];
                neighbour.addPositions(listToCombine.getPosition());
                neighbour.addFreeFields(listToCombine.getFreeFields());
                stoneList.remove(listToCombine);
            } else {
                neighbour.addPosition(toAddPosition);
                neighbour.addFreeFields(toAdd.getFreeFields());
                isPartOfGroup = true;
                stoneList.remove(toAdd);
            }
            stoneMapping[toAddPosition.row()][toAddPosition.col()] = neighbour;
            neighbour.removeFreeField(toAddPosition);
            //if neighbour has the opposite colour to the current stone a liberty should be taken
        } else {
            neighbour.removeFreeField(toAddPosition);
        }

        //print liberty for every stone
        //if(searchForStone(toAddPosition) != null)
        //    searchForStone(toAddPosition).getFreeFields().size();
        //System.out.println("Stone at " + (toAddPosition.row()+1) + alphabet[toAddPosition.col()] + " has " + toAdd.getFreeFields().size() + " liberties.");
    }

    public void addToDelete(StoneGroup addToDelete){
        toDelete.add(addToDelete);
    }

    private void stonesToDelete(){
        for(StoneGroup s: toDelete)
            if(s.getFreeFields().isEmpty())
                deleteStone(s);
        /*for(StoneGroup s: stoneList)
            if(s.getFreeFields().isEmpty())
                deleteStone(s);*/
    }

    protected void deleteStone(StoneGroup toDelete){
        System.out.println("Something is getting deleted!!");
        HashSet<Position> positions = new HashSet<>(toDelete.getPosition());
        System.out.println("positions to delete " + positions.size());
        for (Position p : positions) {
            int row = p.row();
            int col = p.col();
            Position newLiberty = new Position(row, col);
            Color willDelete = toDelete.getColour();

            //upper neighbour
            if(row > 0)
                checkForNewLiberty(row-1, col, newLiberty, willDelete);
            //right neighbour
            if(col+1 < size)
                checkForNewLiberty(row, col+1, newLiberty, willDelete);
            //lower neighbour
            if(row+1 < size)
                checkForNewLiberty(row+1, col, newLiberty, willDelete);
            //left neighbour
            if(col > 0)
                checkForNewLiberty(row, col-1, newLiberty, willDelete);

            stoneMapping[row][col] = null;
            System.out.println("deleting stone at " + indexToNum(row) + ALPHABET[col]);
        }
        stoneList.remove(toDelete);
        //controller.deleteStoneGroup(toDelete);
    }

    private void checkForNewLiberty(int row, int col, Position newLiberty, Color color){
        StoneGroup neighbour = stoneMapping[row][col];
        if (neighbour != null && neighbour.getColour() != color) {
            neighbour.addFreeField(newLiberty);
            System.out.println("liberty added to " + indexToNum(row+2) + ALPHABET[col]);
        }
    }

    private int indexToNum(int row){
        return (size+1) - row;
    }

    private void printSomething(){
        //prints the logic representation of the board
        for(int i = 0; i < size; i++) {
            System.out.print(indexToNum(i+1) + "\t");
            for (int j = 0; j < size; j++)
                System.out.print(stoneMapping[i][j] == null ? "empty\t" : (stoneMapping[i][j].getColour() == Color.BLACK ? "BLACK\t" : "WHITE\t"));
            System.out.println();
        }

        //prints all position per stone group and their free field position
        System.out.println("all stone groups");
        if(stoneList.isEmpty())
            System.out.println("stone-list is empty");
        for(StoneGroup s: stoneList){
            System.out.println(s.getColour() + ", " +  s.getFreeFields().size());
            for(Position p: s.getPosition())
                System.out.print(" " + (indexToNum(p.row()+1)) + ALPHABET[p.col()]);
            System.out.println();
            for(Position p: s.getFreeFields())
                System.out.print(" " + (indexToNum(p.row()+1)) + ALPHABET[p.col()]);
            System.out.println();
        }
    }
}
