package com.example.go_gruppe1.model.command;

import javafx.scene.paint.Color;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameHandlerTest {
    private GameHandler gameHandler;

    @Before
    public void setUp() {
        gameHandler = new GameHandler(9); // Set the board size for testing
    }

    @Test
    public void testAddMove() {
        assertFalse(gameHandler.addMove(0, 1, Color.BLACK));
        assertFalse(gameHandler.addMove(1, 0, Color.BLACK));
        assertTrue(gameHandler.addMove(0, 0, Color.WHITE)); // Cannot place a stone on an occupied position
    }

    @Test
    public void testUndo() {
        gameHandler.addMove(0, 1, Color.YELLOW);
        gameHandler.addMove(1, 0, Color.YELLOW);
        gameHandler.addMove(1, 1, Color.GREEN);

        gameHandler.undo();
        // Assert that the undo operation is performed correctly
        assertNull(gameHandler.getBoard().getBoard()[1][1]);
        assertEquals(Color.YELLOW, gameHandler.getBoard().getBoard()[1][0]);
    }

    @Test
    public void testRedo() {
        gameHandler.addMove(0, 1, Color.YELLOW);
        gameHandler.addMove(1, 0, Color.YELLOW);
        gameHandler.addMove(1, 1, Color.GREEN);

        gameHandler.undo();
        gameHandler.redo();

        // Assert that the redo operation is performed correctly
        assertEquals(Color.YELLOW, gameHandler.getBoard().getBoard()[0][1]);
        assertEquals(Color.YELLOW, gameHandler.getBoard().getBoard()[1][0]);
        assertEquals(Color.GREEN, gameHandler.getBoard().getBoard()[1][1]);
    }

    @Test
    public void testGetBoard() {
        assertNotNull(gameHandler.getBoard());
    }

    @Test
    public void testGetTerritoryScore() {
        gameHandler.addMove(0, 0, Color.BLACK);
        gameHandler.addMove(0, 1, Color.WHITE);
        gameHandler.addMove(1, 0, Color.WHITE);

        int blackTerritoryScore = gameHandler.getTerritoryScore(Color.BLACK);
        int whiteTerritoryScore = gameHandler.getTerritoryScore(Color.WHITE);

        assertEquals(0, blackTerritoryScore);
        assertEquals(1, whiteTerritoryScore);
    }
}
