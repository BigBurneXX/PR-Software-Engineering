package com.example.go_gruppe1;

import javafx.scene.paint.Color;

public class Stone {
    private final Color colour;
    private Stone[] neighbours = new Stone[4];

    Stone(Color colour){
        this.colour = colour;
    }

    public Color getColour() {
        return colour;
    }

    public Stone[] getNeighbours() {
        return neighbours;
    }

    public void setNeighbours(Stone[] neighbours) {
        this.neighbours = neighbours;
    }
}
