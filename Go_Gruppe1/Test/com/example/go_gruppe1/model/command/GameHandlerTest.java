package com.example.go_gruppe1.model.command;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameHandlerTest {
    GameHandler gameHandler = new GameHandler(5); // creating a game with board size 5

    @Test
    void addMove() {
        assertTrue(gameHandler.addMove(0, 0, Color.BLACK)); // Adding a stone at position (0,0), this should return true as the stone is successfully added
        assertEquals(Color.BLACK, gameHandler.getBoard().getBoard()[0][0]); // check if the stone is correctly placed on the board

        assertFalse(gameHandler.addMove(0, 0, Color.WHITE)); // Adding a stone at a filled position, this should return false as the position is already taken
        assertEquals(Color.BLACK, gameHandler.getBoard().getBoard()[0][0]); // the stone at position (0,0) should still be black
    }

    @Test
    void redo() {
        gameHandler.addMove(0, 0, Color.BLACK); // Adding a stone at position (0,0)
        gameHandler.undo(); // Undo the last move, the stone at (0,0) should be removed
        assertNull(gameHandler.getBoard().getBoard()[0][0]); // There should be no stone at position (0,0)

        gameHandler.redo(); // Redo the last undone move, the stone at (0,0) should be placed back
        assertEquals(Color.BLACK, gameHandler.getBoard().getBoard()[0][0]); // The stone at position (0,0) should be black
    }

    @Test
    void undo() {
        gameHandler.addMove(0, 0, Color.BLACK); // Adding a stone at position (0,0)
        gameHandler.undo(); // Undo the last move, the stone at (0,0) should be removed

        assertNull(gameHandler.getBoard().getBoard()[0][0]); // There should be no stone at position (0,0)
    }

    @Test
    void getBoard() {
        gameHandler.addMove(0, 0, Color.BLACK); // Adding a stone at position (0,0)

        // getBoard should return a board with the stone at (0,0)
        assertNotNull(gameHandler.getBoard());
        assertEquals(Color.BLACK, gameHandler.getBoard().getBoard()[0][0]);
    }

    @Test
    void getTerritoryScore() {
        // Assume a configuration where black has a territory
        gameHandler.addMove(0, 0, Color.BLACK);
        gameHandler.addMove(0, 1, Color.BLACK);
        gameHandler.addMove(1, 0, Color.BLACK);
        gameHandler.addMove(1, 1, Color.BLACK);

        // The territory of black should be 1 (the inside of the square)
        assertEquals(1, gameHandler.getTerritoryScore(Color.BLACK));

        // The territory of white should be 0 as there are no white stones on the board
        assertEquals(0, gameHandler.getTerritoryScore(Color.WHITE));
    }
}
