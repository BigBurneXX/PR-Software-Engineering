package com.example.go_gruppe1;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Stone {
    private final Color colour;
    //private final int col;
    //private final int row;
    private int liberties = 0;

    /*private List<Stone> group = new ArrayList<>();

    int getCol() {
        return col;
    }

    protected int getRow() {
        return row;
    }*/

    protected Stone(Color colour){//}, int row, int col){
        this.colour = colour;
        //this.row = row;
        //this.col = col;
        //group.add(this);
    }

    protected Color getColour() {
        return colour;
    }

    protected int getLiberties() {
        return liberties;
    }

    protected void addToGroup(Stone s){
        //group.add(s);
        s.addToGroup(this);
        changeLiberty(s.getLiberties());
    }

    protected void changeLiberty(int addLiberty) {
        /*for (Stone s: group) {
            s.liberties += addLiberty;
        }*/
        liberties += addLiberty;
       //System.out.println("Stone " + this.getClass().getName() + " has " + liberties + " liberties");
       isDead();
    }

    protected boolean isDead(){
        if(liberties < 1) {
            //System.out.println("Stone at " + col + ", " + row + " should be deleted");
            return true;
        }
        return false;
    }

}
