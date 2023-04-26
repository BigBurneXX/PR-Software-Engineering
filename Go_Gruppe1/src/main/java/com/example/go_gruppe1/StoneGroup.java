package com.example.go_gruppe1;

import javafx.scene.paint.Color;

 Changes-by-Ivan
import java.util.ArrayList;
import java.util.List;

public class StoneGroup {
    private final Color colour;
    private List<Position> positions = new ArrayList<>();
    private final BoardLogicControl boardLogicControl;
    private int liberties = 0;

import java.util.HashSet;
import java.util.Set;

public class StoneGroup {
    private final Color colour;
    private final Set<Position> positions = new HashSet<>();
    private final Set<Position> freeFields = new HashSet<>();
    private final BoardLogicControl boardLogicControl;
 main

    protected StoneGroup(BoardLogicControl boardLogicControl, Color colour, int row, int col){
        this.boardLogicControl = boardLogicControl;
        this.colour = colour;
        positions.add(new Position(row, col));
    }

 Changes-by-Ivan
    protected void addPositions(List<Position> positions){

    protected void addPositions(Set<Position> positions){
 main
        this.positions.addAll(positions);
    }
    protected void addPosition(Position p) {
        positions.add(p);
    }

 Changes-by-Ivan
    protected void addPosition(int row, int col){
        positions.add(new Position(row, col));
    }

    protected void addLiberty(){
        liberties++;
    }

    protected void takeLiberty(){
        liberties--;
        if(liberties < 1)
            isDead();
    }

    private void isDead() {
        //should return all positions to be deleted!
        boardLogicControl.deleteStone(this);
    }

    protected void addLiberties(int liberties){
        this.liberties += liberties;

    protected void addFreeField(int row, int col){
        freeFields.add(new Position(row, col));
    }

    protected void addFreeField(Position pos){
        freeFields.add(pos);
    }

    protected void removeFreeField(Position pos){
        freeFields.remove(pos);
        if(freeFields.isEmpty())
            isDead();
    }

    protected void addFreeFields(Set<Position> toAdd){
        freeFields.addAll(toAdd);
    }

    private void isDead() {
        boardLogicControl.deleteStone(this);
 main
    }

    protected Color getColour() {
        return colour;
    }

 Changes-by-Ivan
    protected List<Position> getPosition() {
        return positions;
    }

    protected int getLiberties(){
        return liberties;
    }
}

    protected Set<Position> getPosition() {
        return positions;
    }

    protected Set<Position> getFreeFields(){
        return freeFields;
    }
}
 main
