package com.example.go_gruppe1.model.command;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlaceStoneCommandTest {
    // Assumption: SimpleBoard has a constructor that accepts size of the board
    SimpleBoard board = new SimpleBoard(5); // Creating a 5x5 board

    @Test
    void execute() {
        PlaceStoneCommand placeStoneCommand = new PlaceStoneCommand(board, 0, 0, Color.BLACK);

        // Execute the command, this should return true as the stone is successfully placed
        assertTrue(placeStoneCommand.execute());
        // After execution, the stone should be placed on the board
        assertEquals(Color.BLACK, placeStoneCommand.getBoard().getBoard()[0][0]);
    }

    @Test
    void undo() {
        PlaceStoneCommand placeStoneCommand = new PlaceStoneCommand(board, 0, 0, Color.BLACK);

        // Execute the command
        placeStoneCommand.execute();
        // The stone should be placed on the board
        assertEquals(Color.BLACK, placeStoneCommand.getBoard().getBoard()[0][0]);

        // Undo the command
        placeStoneCommand.undo();
        // The stone should be removed from the board
        assertNull(placeStoneCommand.getBoard().getBoard()[0][0]);
    }

    @Test
    void getBoard() {
        PlaceStoneCommand placeStoneCommand = new PlaceStoneCommand(board, 0, 0, Color.BLACK);
        // The getBoard() method should return the same board as the one we initialized
        assertEquals(board, placeStoneCommand.getBoard());
    }
}
