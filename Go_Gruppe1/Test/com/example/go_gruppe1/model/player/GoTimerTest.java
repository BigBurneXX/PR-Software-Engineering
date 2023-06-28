package com.example.go_gruppe1.model.player;

import javafx.beans.property.StringProperty;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.concurrent.TimeUnit;

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
        goTimer.startTimer();

        // Sleep for some time to let the timer run
        Thread.sleep(2000);  // 2000 milliseconds = 2 seconds

        goTimer.stopTimer();

        int passedSeconds = goTimer.getPassedSeconds();

        // Check that the timer counted 2 seconds
        Assertions.assertEquals(2, passedSeconds);
    }

    @Test
    public void testUpdateTimer() {
        goTimer.startTimer();
        // Sleep for 2 seconds to allow the timer to update
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        StringProperty timeProperty = goTimer.getTimeProperty();
        assertNotNull(timeProperty);
        assertNotEquals("00:02", timeProperty.get());
    }

    @Test
    public void testElapsedTime() throws InterruptedException {
        goTimer.startTimer();

        // Sleep for some time to let the timer run
        Thread.sleep(2000);  // 2000 milliseconds = 2 seconds

        int passedSeconds = goTimer.getPassedSeconds();

        // Check that the timer counted 2 seconds.
        Assertions.assertEquals(2, passedSeconds);
    }

    @Test
    public void testGetTimeProperty() {
        StringProperty timeProperty = goTimer.getTimeProperty();
        assertNotNull(timeProperty);
        assertEquals("00:00", timeProperty.get());
    }
}