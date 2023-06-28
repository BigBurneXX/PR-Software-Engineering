package com.example.go_gruppe1.model.command;

import javafx.scene.paint.Color;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SimpleBoardTest {
    private SimpleBoard board;

    @Before
    public void setUp() {
        board = new SimpleBoard(3);
    }

    @Test
    public void testSimpleBoardCopyConstructor() {
        // Create an original SimpleBoard object
        SimpleBoard originalBoard = new SimpleBoard(3);
        originalBoard.setStone(0, 0, Color.BLACK);
        originalBoard.setStone(1, 0, Color.WHITE);
        originalBoard.setStone(0, 1, Color.WHITE);

        // Create a copy using the copy constructor
        SimpleBoard copiedBoard = new SimpleBoard(originalBoard);

        // Assert that the copied board has the same properties as the original board
        assertEquals(originalBoard.getBoard().length, copiedBoard.getBoard().length);
        assertEquals(originalBoard.getTrapped(Color.BLACK), copiedBoard.getTrapped(Color.BLACK));
        assertEquals(originalBoard.getTrapped(Color.WHITE), copiedBoard.getTrapped(Color.WHITE));

        // Assert that the copied board's array is a deep copy
        assertArrayEquals(originalBoard.getBoard(), copiedBoard.getBoard());
    }

    @Test
    public void testSetStone() {
        assertEquals(0, board.setStone(0, 0, Color.BLACK));
        board.setStone(1, 0, Color.WHITE);
        board.setStone(0, 1, Color.WHITE);
        board.setStone(1, 2, Color.WHITE);
        board.setStone(2, 1, Color.WHITE);
        assertEquals(1, board.setStone(1, 1, Color.BLACK));
    }

    @Test
    public void testCheckStoneGroups(){
        board.setStone(0, 1, Color.BLACK);
        board.setStone(1, 0, Color.BLACK);
        board.setStone(1, 2, Color.BLACK);
        board.setStone(2, 1, Color.BLACK);
        assertEquals(0, board.setStone(1, 1, Color.BLACK));
    }

    @Test
    public void testGetBoard() {
        assertNull(board.getBoard()[0][0]);
        board.setStone(1, 1, Color.BLACK);
        assertEquals(Color.BLACK, board.getBoard()[1][1]);
    }

    @Test
    public void testGetTrapped() {
        assertEquals(0, board.getTrapped(Color.BLACK));
        assertEquals(0, board.getTrapped(Color.WHITE));
        board.setStone(0, 0, Color.BLACK);
        board.setStone(1, 0, Color.WHITE);
        board.setStone(0, 1, Color.WHITE);
        assertEquals(1, board.getTrapped(Color.WHITE));
        assertEquals(0, board.getTrapped(Color.BLACK));
    }
}

