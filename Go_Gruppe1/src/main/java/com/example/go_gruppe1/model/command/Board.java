package com.example.go_gruppe1.model.command;

import com.example.go_gruppe1.controller.boardMaskController;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Board {
    //private Color[][] stones;
    private final int size;
    private final char[] ALPHABET = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T'};
    private final List<StoneGroup> stoneList = new ArrayList<>();
    private boolean isSuicide = false;
    private final Set<StoneGroup> toDelete = new HashSet<>();
    private boolean isPartOfGroup;
    private final StoneGroup[][] stoneMapping;
    private final boardMaskController controller;

    public Board(boardMaskController controller, int boardSize) {
        this.controller = controller;
        size = boardSize;
        //this.stones = new Color[size][size];
        stoneMapping = new StoneGroup[size][size];
        // Initialize the board with empty stones
        //for (int i = 0; i < size; i++)
        //    for (int j = 0; j < size; j++)
        //        stones[i][j] = Color.TRANSPARENT;
    }

    /*
    public Board(boardMaskController controller, int boardSize, StoneGroup[][] stoneMapping){
        this.controller = controller;
        size = boardSize;
        this.stoneMapping = stoneMapping;
    }*/

    public void placeStone(int row, int col, Color colour) {
        //stones[row][col] = colour;
        //add to stoneMapping
        //check liberties
        //send feedback
        // Additional game logic for capturing opponent stones, etc.
        StoneGroup toAdd = new StoneGroup(this, colour, row, col);
        Position toAddPosition = new Position(row, col);
        StoneGroup neighbour;
        isPartOfGroup = false;

        //check upper neighbour
        //System.out.println("upper");
        if(row > 0 && !isSuicide){
            neighbour = searchForStone(row-1, col);
            checkNeighbour(toAdd, toAddPosition, neighbour, new Position(row-1,col));
        }

        //check right neighbour
        //System.out.println("right");
        if(col + 1 < size && !isSuicide){
            neighbour = searchForStone(row, col+1);
            checkNeighbour(toAdd, toAddPosition, neighbour, new Position(row,col+1));
        }

        //check lower neighbour
        //System.out.println("lower");
        if (row + 1 < size && !isSuicide) {
            neighbour = searchForStone(row+1,col);
            checkNeighbour(toAdd, toAddPosition, neighbour, new Position(row+1,col));
        }

        //check left neighbour
        //System.out.println("left");
        if(col > 0 && !isSuicide){
            neighbour = searchForStone(row, col-1);
            checkNeighbour(toAdd, toAddPosition, neighbour, new Position(row,col-1));
        }

        stonesToDelete();
        toDelete.clear();

        if(searchForStone(row, col) == null)
            if(toAdd.getFreeFields().isEmpty()) {
                System.out.println("THIS IS SUICIDE");
                deleteStone(toAdd);
            }else if(!isSuicide) {
                stoneList.add(toAdd);
                stoneMapping[row][col] = toAdd;
            }
        //prints the logic representation of the board
        for(int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++)
                System.out.print(stoneMapping[i][j] == null ? "null" : stoneMapping[i][j].getColour());
            System.out.println();
        }

        //prints all position per stone group and their free field position
        System.out.println("all stone groups");
        for(StoneGroup s: stoneList){
            System.out.println(s.getColour() + ", " +  s.getFreeFields().size());
            for(Position p: s.getPosition())
                System.out.print(" " + (p.row()+1) + ALPHABET[p.col()]);
            System.out.println();
            for(Position p: s.getFreeFields())
                System.out.print(" " + (p.row()+1) + ALPHABET[p.col()]);
            System.out.println();
        }
    }

    public void removeStone(int x, int y) {
        //stones[x][y] = Color.TRANSPARENT;
        // Additional game logic for restoring captured stones, etc.
    }

    private StoneGroup searchForStone(int row, int col){
        return stoneMapping[row][col];
        /*for (StoneGroup s: stoneList) {
            for (Position p: s.getPosition())
                if (p.row() == row && p.col() == col)
                    return s;
        }
        return null;*/
    }

    private void checkNeighbour(StoneGroup toAdd, Position toAddPosition, StoneGroup neighbour, Position neighbourPosition){
        if(neighbour == null){
            if(isPartOfGroup){
                StoneGroup libertyToAdd = searchForStone(toAddPosition.row(), toAddPosition.col());
                libertyToAdd.addFreeField(neighbourPosition);
            } else {
                toAdd.addFreeField(neighbourPosition);
            }
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
            }
            stoneMapping[toAddPosition.row()][toAddPosition.col()] = neighbour;
            neighbour.removeFreeField(toAddPosition);
            if(searchForStone(toAddPosition.row(), toAddPosition.col()) == null) {
                isSuicide = true;
                System.out.println("This is suicide!!");
            }
            //if neighbour has the opposite colour to the current stone a liberty should be taken
        } else {
            neighbour.removeFreeField(toAddPosition);
        }
        //if(searchForStone(toAddPosition) != null)
        //    searchForStone(toAddPosition).getFreeFields().size();
        //System.out.println("Stone at " + (toAddPosition.row()+1) + alphabet[toAddPosition.col()] + " has " + toAdd.getFreeFields().size() + " liberties.");
    }

    protected void addToDelete(StoneGroup addToDelete){
        toDelete.add(addToDelete);
    }

    private void stonesToDelete(){
        for(StoneGroup s: toDelete)
            if(s.getFreeFields().isEmpty())
                deleteStone(s);
    }

    protected void deleteStone(StoneGroup toDelete){
        System.out.println("Something is getting deleted!!");
        HashSet<Position> positions = new HashSet<>(toDelete.getPosition());
        for (Position p : positions) {
            int row = p.row();
            int col = p.col();
            StoneGroup neighbour;

            // Upper neighbour
            neighbour = searchForStone(row - 1, col);
            if (neighbour != null && neighbour.getColour() != toDelete.getColour())
                neighbour.addFreeField(row, col);

            // Right neighbour
            neighbour = searchForStone(row, col + 1);
            if (neighbour != null && neighbour.getColour() != toDelete.getColour())
                neighbour.addFreeField(row, col);

            // Lower neighbour
            neighbour = searchForStone(row + 1, col);
            if (neighbour != null && neighbour.getColour() != toDelete.getColour())
                neighbour.addFreeField(row, col);

            // Left neighbour
            neighbour = searchForStone(row, col - 1);
            if (neighbour != null && neighbour.getColour() != toDelete.getColour())
                neighbour.addFreeField(row, col);
        }
        stoneList.remove(toDelete);
        controller.deleteStoneGroup(toDelete);
    }
}
