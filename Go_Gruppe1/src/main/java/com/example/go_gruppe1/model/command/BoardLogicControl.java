package com.example.go_gruppe1.model.command;

import com.example.go_gruppe1.controller.boardMaskController;
import javafx.scene.paint.Color;

import java.util.*;

public class BoardLogicControl {
    private final char[] ALPHABET = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T'};
    private final List<StoneGroup> stoneList = new ArrayList<>();
    private final int size;
    private final boardMaskController controller;

    private boolean isSuicide = false;

    private final Set<StoneGroup> toDelete = new HashSet<>();
    private boolean isPartOfGroup;

    public BoardLogicControl(boardMaskController controller, int boardSize) {
        this.controller = controller;
        size = boardSize;
    }

    public void setStoneToList(Color colour, int row, int col){
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
            }else if(!isSuicide)
                stoneList.add(toAdd);
        //prints all position per stone group and their free field position
        System.out.println("output of boardLogicControl");
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

    private StoneGroup searchForStone(int row, int col){
        for (StoneGroup s: stoneList) {
            for (Position p: s.getPosition())
                if (p.row() == row && p.col() == col)
                    return s;
        }
        return null;
    }

    private StoneGroup searchForStone(Position toFind){
        for (StoneGroup s: stoneList) {
            for (Position p: s.getPosition())
                if (p.equals(toFind))
                    return s;
        }
        return null;
    }

    private void checkNeighbour(StoneGroup toAdd, Position toAddPosition, StoneGroup neighbour, Position neighbourPosition){
        if(neighbour == null){
            if(isPartOfGroup){
                StoneGroup libertyToAdd = searchForStone(toAddPosition);
                libertyToAdd.addFreeField(neighbourPosition);
            } else {
                toAdd.addFreeField(neighbourPosition);
            }
        //if neighbour has the same colour the current stone should be added to the neighbours group
        }else if(neighbour.getColour() == toAdd.getColour()) {
            if(isPartOfGroup) {
                StoneGroup listToCombine = searchForStone(toAddPosition);
                neighbour.addPositions(listToCombine.getPosition());
                neighbour.addFreeFields(listToCombine.getFreeFields());
                stoneList.remove(listToCombine);
            } else {
                neighbour.addPosition(toAddPosition);
                neighbour.addFreeFields(toAdd.getFreeFields());
                isPartOfGroup = true;
            }
            neighbour.removeFreeField(toAddPosition);
            if(searchForStone(toAddPosition) == null) {
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
            Position newLiberty = new Position(row, col);

            // Upper neighbour
            neighbour = searchForStone(row - 1, col);
            if (neighbour != null && neighbour.getColour() != toDelete.getColour())
                neighbour.addFreeField(newLiberty);

            // Right neighbour
            neighbour = searchForStone(row, col + 1);
            if (neighbour != null && neighbour.getColour() != toDelete.getColour())
                neighbour.addFreeField(newLiberty);

            // Lower neighbour
            neighbour = searchForStone(row + 1, col);
            if (neighbour != null && neighbour.getColour() != toDelete.getColour())
                neighbour.addFreeField(newLiberty);

            // Left neighbour
            neighbour = searchForStone(row, col - 1);
            if (neighbour != null && neighbour.getColour() != toDelete.getColour())
                neighbour.addFreeField(newLiberty);
        }
        stoneList.remove(toDelete);
        controller.deleteStoneGroup(toDelete);
    }
}