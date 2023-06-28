package com.example.go_gruppe1.model.command;

import javafx.scene.paint.Color;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PlaceStoneCommandTest {
    private SimpleBoard board;

    @Before
    public void setUp() {
        // Create a new board for each test
        board = new SimpleBoard(5);
    }

    @Test
    public void testExecute() {
        // Create a new PlaceStoneCommand
        int row = 2;
        int col = 3;
        Color color = Color.BLACK;
        PlaceStoneCommand command = new PlaceStoneCommand(board, row, col, color);

        // Execute the command and check the return value
        command.execute();

        // Verify that the stone was placed correctly on the board
        assertEquals(color, command.getBoard().getBoard()[row][col]);
    }

    @Test
    public void testUndo() {
        // Create a new PlaceStoneCommand
        int row = 4;
        int col = 2;
        Color color = Color.WHITE;
        PlaceStoneCommand command = new PlaceStoneCommand(board, row, col, color);
        command.execute();
        board = command.getBoard();

        PlaceStoneCommand command1 = new PlaceStoneCommand(board, row-1, col-1, color);
        assertEquals(color, command.getBoard().getBoard()[row][col]);

        // Execute the command
        command1.execute();

        // Undo the command
        command1.undo();

        // Verify that the stone was removed from the board
        assertNull(command1.getBoard().getBoard()[row-1][col-1]);
        assertEquals(color, command1.getBoard().getBoard()[row][col]);
    }

    @Test
    public void testGetBoard() {
        // Create a new PlaceStoneCommand
        int row = 0;
        int col = 0;
        Color color = Color.RED;
        PlaceStoneCommand command = new PlaceStoneCommand(board, row, col, color);

        // Verify that the board returned by getBoard() is the same as the original board
        assertSame(board, command.getBoard());
    }

    @Test
    public void testGetDescription(){
        PlaceStoneCommand command = new PlaceStoneCommand(board, 2, 2, Color.YELLOW, "This is a test!");
        assertEquals("This is a test!", command.getDescription());
    }
}
