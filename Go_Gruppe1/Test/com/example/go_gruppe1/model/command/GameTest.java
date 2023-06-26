package com.example.go_gruppe1.model.command;

import javafx.scene.paint.Color;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameTest {
    private Game game;

    @Before
    public void setUp() {
        game = new Game(5); // Set up a game with board size 5
    }

    @Test
    public void testExecuteCommand() {
        Command command = new PlaceStoneCommand(game.getBoard(), 0, 1, Color.YELLOW);
        Command command1 = new PlaceStoneCommand(game.getBoard(), 1, 0, Color.YELLOW);
        Command command2 = new PlaceStoneCommand(game.getBoard(), 1, 2, Color.YELLOW);
        Command command3 = new PlaceStoneCommand(game.getBoard(), 2, 1, Color.YELLOW);
        Command commandSuicide = new PlaceStoneCommand(game.getBoard(), 1, 1, Color.GRAY);

        assertEquals(0, game.executeCommand(command));
        game.executeCommand(command1);
        game.executeCommand(command2);
        game.executeCommand(command3);
        assertEquals(1, game.executeCommand(commandSuicide));
    }

    @Test
    public void testUndoLastMove() {
        Command command = new PlaceStoneCommand(game.getBoard(), 0, 1, Color.YELLOW);
        Command command1 = new PlaceStoneCommand(game.getBoard(), 1, 0, Color.YELLOW);
        Command command2 = new PlaceStoneCommand(game.getBoard(), 1, 1, Color.GREEN);

        game.executeCommand(command);
        game.executeCommand(command1);
        game.executeCommand(command2);

        game.undoLastMove(); // Undo the last move

        // Assert that the undo operation is performed correctly
        assertNull(game.getBoard().getBoard()[1][1]);
        assertEquals(Color.YELLOW, game.getBoard().getBoard()[1][0]);
    }

    @Test
    public void testRedoLastMove() {
        Command command = new PlaceStoneCommand(game.getBoard(), 0, 1, Color.YELLOW);
        Command command1 = new PlaceStoneCommand(game.getBoard(), 1, 0, Color.YELLOW);
        Command command2 = new PlaceStoneCommand(game.getBoard(), 1, 1, Color.GREEN);

        game.executeCommand(command);
        game.executeCommand(command1);
        game.executeCommand(command2);

        game.undoLastMove(); // Undo the last move

        game.redoLastMove(); // Redo the last move
        assertEquals(Color.YELLOW, game.getBoard().getBoard()[0][1]);
        assertEquals(Color.YELLOW, game.getBoard().getBoard()[1][0]);
        assertEquals(Color.GREEN, game.getBoard().getBoard()[1][1]);
    }
}
