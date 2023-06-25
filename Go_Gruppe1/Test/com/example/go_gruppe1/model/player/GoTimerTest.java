package com.example.go_gruppe1.model.player;

import javafx.beans.property.StringProperty;
import org.junit.Before;
import org.junit.Test;

import static javafx.application.Platform.runLater;
import static org.junit.Assert.assertEquals;

public class GoTimerTest {
    private GoTimer goTimer;

    @Before
    public void setUp() {
        goTimer = new GoTimer();
    }

    @Test
    public void testStartAndStopTimer() {
        runLater(() -> {
            goTimer.startTimer();
            assertEquals("00:00", goTimer.getTimeProperty().get());

            goTimer.stopTimer();
            StringProperty timeProperty = goTimer.getTimeProperty();
            String stoppedTime = timeProperty.get();

            try {
                // Sleep for 2 seconds to simulate elapsed time
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            assertEquals(stoppedTime, timeProperty.get());
        });
    }

    @Test
    public void testElapsedTime() {
        runLater(() -> {
            goTimer.startTimer();

            try {
                // Sleep for 2 seconds to simulate elapsed time
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            int passedSeconds = goTimer.getPassedSeconds();
            assertEquals(2, passedSeconds);
        });
    }
}