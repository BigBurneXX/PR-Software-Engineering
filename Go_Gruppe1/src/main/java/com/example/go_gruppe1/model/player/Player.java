package com.example.go_gruppe1.model.player;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.paint.Color;

public class Player {
    private final String name;
    private final Color color;
    private final Color hoverColor;
    private GoTimer timer;
    private StringProperty timeLabelText;
    private int byoyomi;

    public Player(String name, Color color, Color hoverColor, int byoyomi, int byoyomiTimeLimit) {
        this.name = name;
        this.color = color;
        this.hoverColor = hoverColor;
        this.byoyomi = byoyomi;
        if(byoyomi != 0){
            this.timer = new GoTimer();
            this.timeLabelText = new SimpleStringProperty(byoyomi + " time period(s) à " + byoyomiTimeLimit + " s");
        }
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public Color getHoverColor() {
        return hoverColor;
    }

    public GoTimer getTimer() {
        return timer;
    }

    public StringProperty getTimeLabelText() {
        return timeLabelText;
    }

    public int getByoyomi() {
        return byoyomi;
    }

    public void setByoyomi(int byoyomi) {
        this.byoyomi = byoyomi;
    }

    public void setTimeLabelText(String newLabelText) {
        timeLabelText.set(newLabelText);
    }
}