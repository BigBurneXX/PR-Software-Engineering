package com.example.go_gruppe1.model.player;

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
    public void testCheckByoyomi_TimerActive() {
        playerHandler2.moveMade();
        assertFalse(playerHandler2.checkByoyomi());

        // Simulate a move that surpasses the byoyomi time limit
        Player currentPlayer = playerHandler2.getCurrentPlayer();
        try {
            currentPlayer.getTimer().wait(25000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        assertTrue(playerHandler2.checkByoyomi());

        // Verify that the player's byoyomi time has been reduced
        assertEquals(0, currentPlayer.getByoyomi());

        try {
            currentPlayer.getTimer().wait(25000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // Verify the updated time label text
        assertEquals("No time left", currentPlayer.getTimeLabelText().get());
    }
}