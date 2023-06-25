package com.example.go_gruppe1.model.command;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SimpleBoardTest {
    private SimpleBoard board;

    @BeforeEach
    void setUp() {
        board = new SimpleBoard(3); // Let's say we create a 3x3 board for testing
    }

    @Test
    void setStone() {
        assertFalse(board.setStone(0, 0, Color.BLACK)); // returns false if the stone was placed successfully
        // and it's not trapped
        assertEquals(Color.BLACK, board.getBoard()[0][0]); // get the stone color at position (0,0)

        // If there is a stone in the position, the method should return false because the spot is already taken
        assertFalse(board.setStone(0, 0, Color.WHITE)); // should not overwrite the existing black stone
        assertEquals(Color.BLACK, board.getBoard()[0][0]); // the stone at (0,0) should still be black
    }

    @Test
    void getBoard() {
        board.setStone(0, 0, Color.BLACK);
        assertEquals(Color.BLACK, board.getBoard()[0][0]);
    }

    @Test
    void getTrapped() {
        board.setStone(0, 0, Color.BLACK);
        assertEquals(0, board.getTrapped(Color.BLACK));

        // Assuming the behavior of the game, if a stone is replaced, it should be considered trapped
        board.setStone(0, 0, Color.WHITE);
        assertEquals(1, board.getTrapped(Color.BLACK));
    }
}
