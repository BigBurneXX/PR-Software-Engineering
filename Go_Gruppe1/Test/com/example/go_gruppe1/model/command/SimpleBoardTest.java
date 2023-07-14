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

    @Test
    public void testCalcScores() {
        // Set up the initial state for testing
        double komi = 0.5;
        board.setStone(1, 1, Color.WHITE);
        board.setStone(0, 1, Color.BLACK);
        board.setStone(1, 0, Color.BLACK);
        board.setStone(1, 2, Color.BLACK);
        board.setStone(2, 1, Color.BLACK);
        board.calcScores(komi);

        // Perform assertions to check the expected behavior
        assertEquals(6, board.getTotal(Color.BLACK));
        assertEquals(0.5, board.getTotal(Color.WHITE));
    }
/*
    @Test
    public void testIsExclusivelySurrounded() {
        // Set up the initial state for testing
        int row = 0;
        int col = 0;
        Color color = Color.BLACK;
        boolean expectedResult = true;

        // Perform the actual method call
        boolean result = board.isExclusivelySurrounded(row, col, color);

        // Perform assertions to check the expected behavior
        assertEquals(expectedResult, result);
        // Add more assertions as needed
    }

    @Test
    public void testCheckNeighbours() {
        // Set up the initial state for testing
        int row = 0;
        int col = 0;
        Color color = Color.BLACK;
        boolean expectedResult = true;

        // Perform the actual method call
        boolean result = yourObject.checkNeighbours(row, col, color);

        // Perform assertions to check the expected behavior
        assertEquals(expectedResult, result);
        // Add more assertions as needed
    }

    @Test
    public void testFindLibertyArea() {
        // Set up the initial state for testing
        int row = 0;
        int col = 0;
        ArrayList<Position> libertyArea = new ArrayList<>();
        boolean[][] visited = new boolean[size][size];
        // Set up the visited matrix and libertyArea as needed

        // Perform the actual method call
        yourObject.findLibertyArea(row, col, libertyArea, visited);

        // Perform assertions to check the expected behavior
        // Add assertions based on the expected behavior of the method
    }*/
}

