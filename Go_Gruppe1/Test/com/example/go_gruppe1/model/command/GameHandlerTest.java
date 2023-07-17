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
        assertEquals(0, gameHandler.addMove(0, 1, Color.BLACK));
        assertEquals(0, gameHandler.addMove(1, 0, Color.BLACK));
        assertEquals(1, gameHandler.addMove(0, 0, Color.WHITE)); // Cannot place a stone on an occupied position
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
        GameHandler gameHandler = new GameHandler(10);

        // creating a 5x5 board with Black territory
        for (int i = 0; i < 5; i++) {
            gameHandler.addMove(i, 0, Color.BLACK, "Black territory");
            gameHandler.addMove(i, 4, Color.BLACK, "Black territory");
        }
        for (int i = 1; i < 4; i++) {
            gameHandler.addMove(0, i, Color.BLACK, "Black territory");
            gameHandler.addMove(4, i, Color.BLACK, "Black territory");
        }

        // creating a 3x3 board inside Black territory with White territory
        for (int i = 1; i < 4; i++) {
            gameHandler.addMove(i, 1, Color.WHITE, "White territory");
            gameHandler.addMove(i, 3, Color.WHITE, "White territory");
        }
        gameHandler.addMove(1, 2, Color.WHITE, "White territory");
        gameHandler.addMove(3, 2, Color.WHITE, "White territory");

       // int blackTerritoryScore = gameHandler.getTerritoryScore(Color.BLACK);
        //int whiteTerritoryScore = gameHandler.getTerritoryScore(Color.WHITE);

        // The black territory should have 12 empty spaces inside.
        // The white territory should have 1 empty space inside.
      //  assertEquals(12, blackTerritoryScore);
       // assertEquals(1, whiteTerritoryScore);
    }

    @Test
    public void testGetDescription() {
        gameHandler = new GameHandler(3);
        gameHandler.addMove(1, 1, Color.BLACK, "This is a test!");
        assertEquals("This is a test!", gameHandler.getDescription().getValue());
    }
}
