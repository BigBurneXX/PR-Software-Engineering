package com.example.go_gruppe1.model;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Duration;

import java.util.concurrent.TimeUnit;

public class GoTimer {
    private long elapsedTime;
    private long startTime;
    private final StringProperty timeProperty;
    private final Timeline timeline;
    private int passedSeconds;

    public GoTimer(){
        elapsedTime = 0;
        startTime = 0;
        timeProperty = new SimpleStringProperty("00:00");

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateTimer()));
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    public void startTimer(){
        startTime = System.currentTimeMillis();
        timeline.play();
    }

    public void stopTimer(){
        timeline.stop();
    }

    public void updateTimer(){
        long currentTime = System.currentTimeMillis();
        long elapsedTimeForTurn = currentTime - startTime;
        elapsedTime += elapsedTimeForTurn;
        startTime = currentTime;  // update the start time

        long minutes = TimeUnit.MILLISECONDS.toMinutes(elapsedTime);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedTime) - TimeUnit.MINUTES.toSeconds(minutes);

        timeProperty.set(String.format("%02d:%02d", minutes, seconds));
    }

    public void passedSlotSeconds() {
        long currentTime = System.currentTimeMillis();
        long elapsedTimeForTurn = currentTime - startTime;
        passedSeconds = (int) TimeUnit.MILLISECONDS.toSeconds(elapsedTime + elapsedTimeForTurn);
        elapsedTime = 0;
        System.out.println("passed: " + passedSeconds);
    }

    public int getPassedSeconds(){
        return passedSeconds;
    }

    public StringProperty timeProperty() {
        return timeProperty;
    }
}
