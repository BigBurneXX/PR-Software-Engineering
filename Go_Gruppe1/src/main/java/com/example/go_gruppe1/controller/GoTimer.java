package com.example.go_gruppe1.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Duration;

import java.util.concurrent.TimeUnit;

public class GoTimer {
    private long startTime;
    private final StringProperty timeProperty;
    private final Timeline timeline;

    /**
     * initiates timer with start time 0
     */
    public GoTimer(){
        startTime = 0;
        timeProperty = new SimpleStringProperty("00:00");

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateTimer()));
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    /**
     * starts timer
     */
    public void startTimer(){
        startTime = System.currentTimeMillis();
        timeline.play();
    }

    /**
     * stops timer
     */
    public void stopTimer(){
        timeline.stop();
    }

    /**
     * updates timer with passed time
     */
    private void updateTimer(){
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - startTime;

        long minutes = TimeUnit.MILLISECONDS.toMinutes(elapsedTime);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedTime) - TimeUnit.MINUTES.toSeconds(minutes);

        timeProperty.set(String.format("%02d:%02d", minutes, seconds));
    }


    /**
     * @return seconds passed from move
     */
    public int getPassedSeconds() {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - startTime;

        //restart startTime (caution when calling!!!)
        startTime = System.currentTimeMillis();
        return (int) TimeUnit.MILLISECONDS.toSeconds(elapsedTime);
    }

    /**
     * @return time as String
     */
    public StringProperty getTimeProperty() {
        return timeProperty;
    }
}
