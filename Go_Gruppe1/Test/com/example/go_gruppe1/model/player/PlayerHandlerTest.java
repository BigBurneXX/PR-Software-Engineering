package com.example.go_gruppe1.model.player;

import javafx.application.Platform;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;

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
    public void testCheckByoyomi_TimerActive_ExceedsTimeLimit() throws InterruptedException {
        // Setup
        int byoyomiOverruns = 2;
        int byoyomiTimeLimit = 2; // set a smaller time limit for testing

        Platform.startup(() -> {});

        Platform.runLater(() -> {
            playerHandler2 = new PlayerHandler("BluePlayer", "YellowPlayer", byoyomiOverruns, byoyomiTimeLimit);

            // Act
            playerHandler2.startTimer();
        });

        // Wait for (byoyomiTimeLimit * byoyomiOverruns + 1) seconds to force byoyomi overrun
        Thread.sleep((byoyomiTimeLimit * byoyomiOverruns + 1) * 1000);

        Platform.runLater(() -> {
            // Assert
            assertTrue(playerHandler2.checkByoyomi());
            assertEquals("No time left", playerHandler2.getCurrentPlayer().getTimeLabelText().get());
        });
    }
    @Test
    public void testChangePlayer_AfterMoveMade() {
        Player initialPlayer = playerHandler2.getCurrentPlayer();
        playerHandler2.moveMade();
        assertNotEquals(initialPlayer, playerHandler2.getCurrentPlayer());
    }

    @Test
    public void testChangePlayer_AfterStartTimer() {
        Player initialPlayer = playerHandler2.getCurrentPlayer();
        playerHandler2.startTimer();
        assertEquals(initialPlayer, playerHandler2.getCurrentPlayer());
    }

    @Test
    public void testCurrentPlayer_ByoyomiAfterConstruction() {
        // Check the byoyomi periods of the current players after construction
        assertEquals(0, playerHandler.getCurrentPlayer().getByoyomi());
        assertEquals(1, playerHandler2.getCurrentPlayer().getByoyomi());
    }

    @Test
    public void testNextPlayer_ByoyomiAfterConstruction() {
        // Check the byoyomi periods of the next players after construction
        assertEquals(0, playerHandler.getNextPlayer().getByoyomi());
        assertEquals(1, playerHandler2.getNextPlayer().getByoyomi());
    }

    @Test
    public void testMoveMade_timerInactive() {
        PlayerHandler playerHandlerNoByoyomi = new PlayerHandler("BlackPlayer", "WhitePlayer");
        Player initialPlayer = playerHandlerNoByoyomi.getCurrentPlayer();

        playerHandlerNoByoyomi.moveMade();

        assertNotEquals(initialPlayer, playerHandlerNoByoyomi.getCurrentPlayer());
    }

    @Test
    public void testCheckByoyomi_timerInactive() {
        PlayerHandler playerHandlerNoByoyomi = new PlayerHandler("BlackPlayer", "WhitePlayer");

        assertFalse(playerHandlerNoByoyomi.checkByoyomi());
    }

    @Test
    public void testConstructor_PlayerInitialization() {
        PlayerHandler playerHandlerNoByoyomi = new PlayerHandler("BlackPlayer", "WhitePlayer");
        PlayerHandler playerHandlerByoyomi = new PlayerHandler("BluePlayer", "YellowPlayer", 1, 20);

        assertEquals("BlackPlayer", playerHandlerNoByoyomi.getPlayerBlack().getName());
        assertEquals("WhitePlayer", playerHandlerNoByoyomi.getPlayerWhite().getName());
        assertEquals("BluePlayer", playerHandlerByoyomi.getPlayerBlack().getName());
        assertEquals("YellowPlayer", playerHandlerByoyomi.getPlayerWhite().getName());
    }

    @Test
    public void testStartTimerTwice() {
        playerHandler.startTimer();
        playerHandler.startTimer();
        // Assert something here, depending on your implementation.
    }
    @Test
    public void testChangePlayerMultipleTimes() {
        Player initialPlayer = playerHandler.getCurrentPlayer();

        playerHandler.changePlayer();
        playerHandler.changePlayer();

        assertEquals(initialPlayer, playerHandler.getCurrentPlayer());
    }

    @Test
    public void testCheckByoyomiWithoutStartingTimer() {
        assertFalse(playerHandler.checkByoyomi());
    }

    @Test
    public void testMoveMade_NoByoyomi() {
        Player initialPlayer = playerHandler.getCurrentPlayer();

        playerHandler.moveMade();

        assertNotEquals(initialPlayer, playerHandler.getCurrentPlayer());
    }

    @Test
    public void testStartTimer_NoByoyomi() {
        playerHandler.startTimer();

        // Assert the state of the timers, this might require more setup to test.
    }

    @Test
    public void testChangePlayerAfterInitialization() {
        Player initialCurrentPlayer = playerHandler.getCurrentPlayer();
        Player initialNextPlayer = playerHandler.getNextPlayer();

        playerHandler.changePlayer();

        assertEquals(initialCurrentPlayer, playerHandler.getNextPlayer());
        assertEquals(initialNextPlayer, playerHandler.getCurrentPlayer());
    }

    @Test
    public void testMoveMadeWithoutByoyomi() {
        PlayerHandler playerHandlerNoByoyomi = new PlayerHandler("BlackPlayer", "WhitePlayer");
        Player initialPlayer = playerHandlerNoByoyomi.getCurrentPlayer();

        playerHandlerNoByoyomi.moveMade();

        assertNotEquals(initialPlayer, playerHandlerNoByoyomi.getCurrentPlayer());
    }

    @Test
    public void testCheckByoyomiWithDifferentValues() {
        PlayerHandler playerHandlerByoyomi = new PlayerHandler("BlackPlayer", "WhitePlayer", 2, 30);
        // Setup and assertions here...
    }

    @Test
    public void testStartTimerWithoutByoyomi() {
        PlayerHandler playerHandlerNoByoyomi = new PlayerHandler("BlackPlayer", "WhitePlayer");
        playerHandlerNoByoyomi.startTimer();

        // Add some assertions here, depending on your implementation.
    }

    @Test
    public void testTerminalInfo() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        String playerBlackName = "BlackPlayer";
        String playerWhiteName = "WhitePlayer";
        int byoyomiOverruns = 1;
        int byoyomiTimeLimit = 20;
        new PlayerHandler(playerBlackName, playerWhiteName, byoyomiOverruns, byoyomiTimeLimit);

        String expectedOutput = "Number of Byoyomi time overruns: " + byoyomiOverruns + "\r\n" +
                "Byoyomi time limit: " + byoyomiTimeLimit + "\r\n";

        assertEquals(expectedOutput, outContent.toString());

        // reset the System.out
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
    }

}

