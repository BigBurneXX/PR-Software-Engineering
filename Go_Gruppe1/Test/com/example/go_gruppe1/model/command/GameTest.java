package com.example.go_gruppe1.model.command;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    private Game game;

    @BeforeEach
    void setUp() {
        game = new Game(5);
    }

    @Test
    void executeCommand() {
        assertDoesNotThrow(() -> game.executeCommand(null));
    }

    @Test
    void undoLastMove() {
        assertDoesNotThrow(game::undoLastMove);
    }

    @Test
    void redoLastMove() {
        assertDoesNotThrow(game::redoLastMove);
    }

    @Test
    void getBoard() {
        // getBoard method should return a SimpleBoard instance
        assertTrue(game.getBoard() instanceof SimpleBoard);
        // The board should be initialized and not null
        assertNotNull(game.getBoard().getBoard());
        // The board size should match the one provided during the game initialization
        assertEquals(5, game.getBoard().getBoard().length);
    }
}