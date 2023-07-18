package com.example.go_gruppe1.model.command;

import javafx.scene.paint.Color;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class GameTest {
    private Game game;

    @Before
    public void setUp() {
        game = new Game(5); // Set up a game with board size 5
    }

    @Test
    public void testExecuteCommand() {
        Command command = new PlaceStoneCommand(game.getBoard(), 0, 1, Color.BLACK);
        Command command1 = new PlaceStoneCommand(game.getBoard(), 1, 0, Color.BLACK);
        Command command2 = new PlaceStoneCommand(game.getBoard(), 1, 2, Color.BLACK);
        Command command3 = new PlaceStoneCommand(game.getBoard(), 2, 1, Color.BLACK);
        Command commandSuicide = new PlaceStoneCommand(game.getBoard(), 1, 1, Color.WHITE);

        Command command4 = new PlaceStoneCommand(game.getBoard(), 0, 2, Color.WHITE);
        Command command5 = new PlaceStoneCommand(game.getBoard(), 1, 3, Color.WHITE);
        Command command6 = new PlaceStoneCommand(game.getBoard(), 2, 2, Color.WHITE);
        Command ko1 = new PlaceStoneCommand(game.getBoard(), 1, 1, Color.WHITE);
        Command ko2 = new PlaceStoneCommand(game.getBoard(), 1, 2, Color.BLACK);

        assertEquals(0, game.executeCommand(command));
        game.executeCommand(command1);
        game.executeCommand(command2);
        game.executeCommand(command3);
        assertEquals(1, game.executeCommand(commandSuicide));
        game.executeCommand(command4);
        game.executeCommand(command5);
        game.executeCommand(command6);
        assertEquals(0, game.executeCommand(ko1));
        assertEquals(2, game.executeCommand(ko2));
    }

    @Test
    public void testUndoLastMove() {
        // Set up a new game
        Game game = new Game(3); // Change size as needed

        // Create commands to place stones
        Command command1 = new PlaceStoneCommand(game.getBoard(), 0, 1, Color.YELLOW);
        Command command2 = new PlaceStoneCommand(game.getBoard(), 1, 0, Color.YELLOW);
        Command command3 = new PlaceStoneCommand(game.getBoard(), 1, 1, Color.GREEN);

        // Execute the commands
        game.executeCommand(command1);
        game.executeCommand(command2);
        game.executeCommand(command3);

        // Assert the stones are placed correctly
        assertEquals(Color.YELLOW, game.getBoard().getBoard()[0][1]);
        assertEquals(Color.YELLOW, game.getBoard().getBoard()[1][0]);
        assertEquals(Color.GREEN, game.getBoard().getBoard()[1][1]);

        // Undo the last move
        game.undoLastMove();

        // Assert that the last stone was removed (undo operation is performed correctly)
        assertNull(game.getBoard().getBoard()[1][1]);

        // Undo the second move
        game.undoLastMove();

        // Assert that the second stone was removed
        assertNull(game.getBoard().getBoard()[1][0]);

        // Undo the first move
        game.undoLastMove();

        // Assert that the first stone was removed
        assertNull(game.getBoard().getBoard()[0][1]);
    }


    @Test
    public void testRedoLastMove() {
        // Set up a new game
        Game game = new Game(3); // Change size as needed

        // Create commands to place stones
        Command command1 = new PlaceStoneCommand(game.getBoard(), 0, 1, Color.YELLOW);
        Command command2 = new PlaceStoneCommand(game.getBoard(), 1, 0, Color.YELLOW);
        Command command3 = new PlaceStoneCommand(game.getBoard(), 1, 1, Color.GREEN);

        // Execute the commands
        game.executeCommand(command1);
        game.executeCommand(command2);
        game.executeCommand(command3);

        // Undo the last move
        game.undoLastMove();

        // Assert that the last stone was removed
        assertNull(game.getBoard().getBoard()[1][1]);

        // Redo the last move
        game.redoLastMove();

        // Assert that the last stone was restored
        assertEquals(Color.GREEN, game.getBoard().getBoard()[1][1]);
    }

    @Test
    public void testGetDescription() {
        // Initial description
        String initialDescription = game.getDescription().get();

        // Create a command and execute it
        Command command = new PlaceStoneCommand(game.getBoard(), 0, 1, Color.YELLOW, "This is a test1");
        game.executeCommand(command);

        // Check that the description is updated
        assertNotEquals(initialDescription, game.getDescription().get()); // Description should be updated after command execution
        assertEquals(command.getDescription(), game.getDescription().get()); // Description should match the executed command's description

        // Undo the last move
        game.undoLastMove();

        // Check that the description is the same as the command's description (since your code sets it to the command's description even when undoing)
        assertEquals(command.getDescription(), game.getDescription().get()); // Description should match the command's description even after undoing
    }
    @Test
    public void testUndoRedoWithEmptyStacks() {
        // Call undo and redo on a new game (stacks are empty)
        Game game = new Game(3);
        game.undoLastMove();  // this should not raise any exception
        game.redoLastMove();  // this should not raise any exception
    }
    
    @Test
    public void testGetDescriptionBeforeAnyCommand() {
        assertEquals("Something!!", game.getDescription().get());
    }

    @Test
    public void testUndoRedoOrder() {
        Command command1 = new PlaceStoneCommand(game.getBoard(), 0, 0, Color.BLACK);
        Command command2 = new PlaceStoneCommand(game.getBoard(), 0, 1, Color.WHITE);

        game.executeCommand(command1);
        game.executeCommand(command2);

        game.undoLastMove();
        game.undoLastMove();

        game.redoLastMove();

        // After undoing both moves and redoing one, the first stone should be back
        assertEquals(Color.BLACK, game.getBoard().getBoard()[0][0]);
        assertNull(game.getBoard().getBoard()[0][1]); // The second stone should still be absent
    }

    @Test
    public void testExecuteCommandSequence() {
        Command command1 = new PlaceStoneCommand(game.getBoard(), 0, 0, Color.BLACK);
        Command command2 = new PlaceStoneCommand(game.getBoard(), 0, 1, Color.WHITE);
        Command command3 = new PlaceStoneCommand(game.getBoard(), 1, 0, Color.BLACK);
        Command command4 = new PlaceStoneCommand(game.getBoard(), 1, 1, Color.WHITE);

        game.executeCommand(command1);
        game.executeCommand(command2);
        game.executeCommand(command3);
        game.executeCommand(command4);

        game.undoLastMove();
        game.redoLastMove();

        // After undoing and redoing the last move, the last stone should be present again
        assertEquals(Color.WHITE, game.getBoard().getBoard()[1][1]);
    }
}