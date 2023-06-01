package com.example.go_gruppe1.model.command;

import javafx.scene.paint.Color;

import java.util.HashSet;
import java.util.Set;

public class StoneGroup {
    private final Color colour;
    private final Set<Position> positions = new HashSet<>();
    private final Set<Position> freeFields = new HashSet<>();
    private BoardLogicControl boardLogicControl;
    private Board board;

    protected StoneGroup(BoardLogicControl boardLogicControl, Color colour, int row, int col){
        this.boardLogicControl = boardLogicControl;
        this.colour = colour;
        positions.add(new Position(row, col));
    }

    protected StoneGroup(Board board, Color colour, int row, int col){
        this.board = board;
        this.colour = colour;
        positions.add(new Position(row, col));
    }

    protected void addPositions(Set<Position> positions){
        this.positions.addAll(positions);
    }
    protected void addPosition(Position p) {
        positions.add(p);
    }

    protected void addFreeField(Position pos){
        freeFields.add(pos);
    }

    protected void removeFreeField(Position pos){
        freeFields.remove(pos);
        if(freeFields.isEmpty()) {
            //boardLogicControl.addToDelete(this);
            board.addToDelete(this);
        }
    }

    protected void addFreeFields(Set<Position> toAdd){
        freeFields.addAll(toAdd);
    }

    public Color getColour() {
        return colour;
    }

    public Set<Position> getPosition() {
        return positions;
    }

    protected Set<Position> getFreeFields(){
        return freeFields;
    }
}