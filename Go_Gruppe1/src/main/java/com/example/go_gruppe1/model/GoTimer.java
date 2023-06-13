package com.example.go_gruppe1.model;

import java.util.concurrent.TimeUnit;

public class GoTimer {
    private long elapsedTime;
    private long startTime;

    public GoTimer(){
        elapsedTime = 0;
        startTime = 0;
    }

    public String update(){
        long currentTime = System.currentTimeMillis();
        long elapsedTimeForTurn = currentTime - startTime;
        elapsedTime += elapsedTimeForTurn;
        startTime = currentTime;  // update the start time

        long minutes = TimeUnit.MILLISECONDS.toMinutes(elapsedTime);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedTime) - TimeUnit.MINUTES.toSeconds(minutes);

        return String.format("%02d:%02d", minutes, seconds);
    }
}
