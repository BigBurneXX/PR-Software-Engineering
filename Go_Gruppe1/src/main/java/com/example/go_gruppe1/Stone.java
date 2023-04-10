package com.example.go_gruppe1;

import javafx.scene.paint.Color;

public class Stone {
    private final Color colour;
    private final int col;
    private final int row;
    private int liberties = 0;

    protected int getCol() {
        return col;
    }

    protected int getRow() {
        return row;
    }

    protected Stone(Color colour, int row, int col){
        this.colour = colour;
        this.row = row;
        this.col = col;
    }

    protected Color getColour() {
        return colour;
    }

    protected int getLiberties() {
        return liberties;
    }

    protected void changeLiberty(int addLiberty) {
       liberties += addLiberty;
       System.out.println("Stone at " + (col+1) + ", " + (row+1) + " has " + liberties + " liberties");
       isDead();
    }

    protected boolean isDead(){
        if(liberties < 1) {
            System.out.println("Stone at " + col + ", " + row + " should be deleted");
            return true;
        }
        return false;
    }

}
