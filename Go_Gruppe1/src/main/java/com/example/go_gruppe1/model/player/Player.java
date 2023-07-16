package com.example.go_gruppe1.model.player;

import com.example.go_gruppe1.controller.GoTimer;
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

    /**
     * @param name player name
     * @param color player color (black or white)
     * @param hoverColor player hover color (light black or light white)
     * @param byoyomi # of byoyomi time periods
     * @param byoyomiTimeLimit time of byoyomi period
     *
     * initiates a player for Go with its attributes
     */
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

    /**
     * @return player name
     */
    public String getName() {
        return name;
    }

    /**
     * @return player color (black or white)
     */
    public Color getColor() {
        return color;
    }

    /**
     * @return player hover color (light black or light white)
     */
    public Color getHoverColor() {
        return hoverColor;
    }

    /**
     * @return player timer
     */
    public GoTimer getTimer() {
        return timer;
    }

    /**
     * @return player passed time for one move
     */
    public StringProperty getTimeLabelText() {
        return timeLabelText;
    }

    /**
     * @return # of byoyomi time periods
     */
    public int getByoyomi() {
        return byoyomi;
    }

    /**
     * @param byoyomi
     *
     * sets byoyomi time periods
     */
    public void setByoyomi(int byoyomi) {
        this.byoyomi = byoyomi;
    }

    /**
     * @param newLabelText
     *
     * sets current time for player
     */
    public void setTimeLabelText(String newLabelText) {
        timeLabelText.set(newLabelText);
    }
}