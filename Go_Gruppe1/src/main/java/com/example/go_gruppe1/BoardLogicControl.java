package com.example.go_gruppe1;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class BoardLogicControl {
    private final char[] alphabet = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T'};
    private final List<StoneGroup> stoneList = new ArrayList<>();
    private final int size;
    private final boardMaskController controller;

    private boolean isSuicide = false;

    private boolean isPartOfGroup;
    protected BoardLogicControl(boardMaskController controller, int boardSize) {
        this.controller = controller;
        size = boardSize;
    }

    protected void setStoneToList(Color colour, int row, int col){
        StoneGroup toAdd = new StoneGroup(this, colour, row, col);
        Position toAddPosition = new Position(row, col);
        StoneGroup neighbour;
        isPartOfGroup = false;

        //check upper neighbour
        System.out.println("upper");
        if(row > 0 && !isSuicide){
            neighbour = searchForStone(row-1, col);
            checkNeighbour(toAdd, toAddPosition, neighbour, new Position(row-1,col));
        }

        //check right neighbour
        System.out.println("right");
        if(col + 1 < size && !isSuicide){
            neighbour = searchForStone(row, col+1);
            checkNeighbour(toAdd, toAddPosition, neighbour, new Position(row,col+1));
        }

        //check lower neighbour
        System.out.println("lower");
        if (row + 1 < size && !isSuicide) {
            neighbour = searchForStone(row+1,col);
            checkNeighbour(toAdd, toAddPosition, neighbour, new Position(row+1,col));
        }

        //check left neighbour
        System.out.println("left");
        if(col > 0 && !isSuicide){
            neighbour = searchForStone(row, col-1);
            checkNeighbour(toAdd, toAddPosition, neighbour, new Position(row,col-1));
        }

        if(searchForStone(row, col) == null)
            if(toAdd.getFreeFields().size() == 0)
                deleteStone(toAdd);
            else if(!isSuicide)
                stoneList.add(toAdd);

        //prints all position per stone group and their free field position
        for(StoneGroup s: stoneList){
            System.out.println(s.getColour() + ", " +  s.getFreeFields().size());
            for(Position p: s.getPosition())
                System.out.print(" " + (p.row()+1) + alphabet[p.col()]);
            System.out.println();
            for(Position p: s.getFreeFields())
                System.out.print(" " + (p.row()+1) + alphabet[p.col()]);
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
        System.out.println("Stone at " + (toAddPosition.row()+1) + alphabet[toAddPosition.col()] + " has " + toAdd.getFreeFields().size() + " liberties.");
    }

    protected void deleteStone(StoneGroup toDelete){
        System.out.println("Something is getting deleted!!");
        for (Position p: toDelete.getPosition()) {
            //upper neighbour
            //System.out.println("upper");
            StoneGroup neighbour = searchForStone((p.row()-1), (p.col()));
            if(neighbour != null && neighbour.getColour() != toDelete.getColour())
                neighbour.addFreeField(p.row(), p.col());

            //right neighbour
            //System.out.println("right");
            neighbour = searchForStone(p.row(), (p.col()+1));
            if(neighbour != null && neighbour.getColour() != toDelete.getColour())
                neighbour.addFreeField(p.row(), p.col());

            //lower neighbour
            //System.out.println("lower");
            neighbour = searchForStone((p.row()+1), p.col());
            if(neighbour != null && neighbour.getColour() != toDelete.getColour())
                neighbour.addFreeField(p.row(), p.col());

            //left neighbour
            //System.out.println("left");
            neighbour = searchForStone(p.row(), (p.col()-1));
            if(neighbour != null && neighbour.getColour() != toDelete.getColour())
                neighbour.addFreeField(p.row(), p.col());
        }
        stoneList.remove(toDelete);
        controller.deleteStoneGroup(toDelete);
    }
}
