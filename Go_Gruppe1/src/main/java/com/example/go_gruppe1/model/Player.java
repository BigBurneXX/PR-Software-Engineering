package com.example.go_gruppe1.model;

import javafx.scene.paint.Color;

public class Player {
    private final String name;
    private final Color color;
    private final Color hoverColor;
    private int trappedStone;
    private final GoTimer timer;

    public Player(String name, Color color, Color hoverColor) {
        this.name = name;
        this.color = color;
        this.hoverColor = hoverColor;
        this.trappedStone = 0;
        this.timer = new GoTimer();
    }

    public void setTrappedStone(int add) {
        trappedStone += add;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public Color getHoverColor(){
        return hoverColor;
    }

    public int isTrappedStone() {
        return trappedStone;
    }

    public GoTimer getTimer() {
        return timer;
    }
}