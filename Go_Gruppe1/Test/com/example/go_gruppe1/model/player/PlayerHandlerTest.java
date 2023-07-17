package com.example.go_gruppe1.model.player;

import javafx.application.Platform;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerHandlerTest {

    private PlayerHandler playerHandler;
    private PlayerHandler playerHandler2;

    @BeforeEach
    public void setUp() {
        playerHandler = new PlayerHandler("BlackPlayer", "WhitePlayer");
        playerHandler2 = new PlayerHandler("BluePlayer", "YellowPlayer", 1, 20);
    }

    @Test
    public void testMoveMade() {
        Player currentPlayer = playerHandler.getCurrentPlayer();
        Player nextPlayer = playerHandler.getNextPlayer();

        playerHandler.moveMade();

        assertNotEquals(currentPlayer, playerHandler.getCurrentPlayer());
        assertEquals(currentPlayer, playerHandler.getNextPlayer());
        assertEquals(nextPlayer, playerHandler.getCurrentPlayer());
    }

    @Test
    public void testStartTimer() {
        playerHandler.startTimer();
        playerHandler2.startTimer();
        //assertTrue(playerHandler.getCurrentPlayer().getTimer().isTimerRunning());
    }

    @Test
    public void testChangePlayer() {
        Player currentPlayer = playerHandler.getCurrentPlayer();
        Player nextPlayer = playerHandler.getNextPlayer();

        playerHandler.changePlayer();

        assertEquals(nextPlayer, playerHandler.getCurrentPlayer());
        assertEquals(currentPlayer, playerHandler.getNextPlayer());
    }

    @Test
    public void testGetters() {
        assertEquals("BlackPlayer", playerHandler.getPlayerBlack().getName());
        assertEquals("WhitePlayer", playerHandler.getPlayerWhite().getName());
        assertEquals(0, playerHandler.getByoyomiOverruns());
        assertEquals(0, playerHandler.getByoyomiTimeLimit());
    }

    @Test
    public void testCheckByoyomi() {
        assertFalse(playerHandler.checkByoyomi());
    }

    @Test
    public void testCheckByoyomi_TimerActive() throws InterruptedException {
        // Setup
        int byoyomiOverruns = 1;
        int byoyomiTimeLimit = 2; // set a smaller time limit for the purpose of the test

        Platform.startup(() -> {});

        Platform.runLater(() -> {
            playerHandler2 = new PlayerHandler("BluePlayer", "YellowPlayer", byoyomiOverruns, byoyomiTimeLimit);

            // Act
            playerHandler2.startTimer();
        });

        Thread.sleep((byoyomiTimeLimit + 1) * 1000); // Delay for time limit + 1 second to trigger byoyomi

        Platform.runLater(() -> {
            // Assert
            assertTrue(playerHandler2.checkByoyomi());
            assertEquals("No time left", playerHandler2.getCurrentPlayer().getTimeLabelText().get());
        });
    }




}