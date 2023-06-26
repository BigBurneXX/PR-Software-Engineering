package com.example.go_gruppe1.model.player;

import javafx.beans.property.StringProperty;
import org.junit.Before;
import org.junit.Test;

import static javafx.application.Platform.runLater;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

public class GoTimerTest {
    private GoTimer goTimer;

    @Before
    public void setUp() {
        goTimer = new GoTimer();
    }

    @Test
    public void testStartAndStopTimer() throws InterruptedException {
        GoTimer timer = new GoTimer();

        timer.startTimer();

        // Sleep for some time to let the timer run
        Thread.sleep(2000);  // 2000 milliseconds = 2 seconds

        timer.stopTimer();

        int passedSeconds = timer.getPassedSeconds();

        // Check that the timer counted at least 1 second and less than 3 seconds.
        // This range is used due to potential delays in thread scheduling, timer precision, etc.
        assertTrue(passedSeconds >= 1);
        assertTrue(passedSeconds < 3);
    }

    @Test
    public void testElapsedTime() throws InterruptedException {
        GoTimer timer = new GoTimer();

        timer.startTimer();

        // Sleep for some time to let the timer run
        Thread.sleep(2000);  // 2000 milliseconds = 2 seconds

        int passedSeconds = timer.getPassedSeconds();

        // Check that the timer counted at least 1 second and less than 3 seconds.
        assertTrue(passedSeconds >= 1);
        assertTrue(passedSeconds < 3);
    }
}