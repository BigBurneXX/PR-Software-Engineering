package com.example.go_gruppe1;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class StoneGroup {
    private final Color colour;
    private List<Position> positions = new ArrayList<>();
    private final BoardLogicControl boardLogicControl;
    private int liberties = 0;

    protected StoneGroup(BoardLogicControl boardLogicControl, Color colour, int row, int col){
        this.boardLogicControl = boardLogicControl;
        this.colour = colour;
        positions.add(new Position(row, col));
    }

    protected void addPositions(List<Position> positions){
        this.positions.addAll(positions);
    }
    protected void addPosition(Position p) {
        positions.add(p);
    }

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
        boardLogicControl.deleteStone(this);
    }

    protected void addLiberties(int liberties){
        this.liberties += liberties;
    }

    protected Color getColour() {
        return colour;
    }

    protected List<Position> getPosition() {
        return positions;
    }

    protected int getLiberties(){
        return liberties;
    }
}
