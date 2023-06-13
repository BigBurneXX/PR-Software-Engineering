package com.example.go_gruppe1.model.command;

import javafx.scene.paint.Color;

import java.util.Stack;

public class Game {
    private SimpleBoard board;
    private final Stack<Command> undoStack;
    private final Stack<Command> redoStack;

    public Game(int boardSize) {
        this.board = new SimpleBoard(boardSize);
        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();
    }

    public boolean executeCommand(Command command) {
        undoStack.push(command);
        redoStack.clear();
        return command.execute();
    }

    public void undoLastMove() {
        if (!undoStack.isEmpty()) {
            Command command = undoStack.pop();
            command.undo();
            board = new SimpleBoard(command.getBoard());
            redoStack.push(command);
        }
    }

    public void redoLastMove() {
        if (!redoStack.isEmpty()) {
            Command command = redoStack.pop();
            command.execute();
            board = command.getBoard();
            undoStack.push(command);
        }
    }

    public SimpleBoard getBoard(){
        return board;
    }

    /*protected double calculateScore(Color c, int blackTrappedStones, int whiteTrappedStones) {
        double score = 0.0;

        // Create a separate visited matrix to track visited intersections
        boolean[][] visited = new boolean[board.getSize()][board.getSize()];

        // Traverse the board and calculate the score
        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                if (!visited[i][j] && this.getBoard().getBoard()[i][j] == c) {
                    // Calculate the score for the current group
                    double groupScore = calculateGroupScore(i, j, c, visited, blackTrappedStones, whiteTrappedStones);

                    // Add the group's score to the total score
                    score += groupScore;
                }
            }
        }

        return score;
    }

    // Helper method to calculate the score for a group of stones
    private double calculateGroupScore(int row, int col, Color c, boolean[][] visited, int blackTrappedStones, int whiteTrappedStones) {
        double groupScore = 0.0;

        // Check if the current intersection is within the board bounds and not visited
        if (row >= 0 && row < board.getSize() && col >= 0 && col < board.getSize() && !visited[row][col]) {
            visited[row][col] = true;

            // Check if the current stone is alive or dead
            if (isAlive(row, col, visited)) {
                groupScore += 1.0; // Each alive stone contributes 1 to the score
            } else {
                // If the stone is dead, remove it from the board and add to trapped stones
                this.getBoard().getBoard()[row][col] = Color.TRANSPARENT;
                if (c == Color.BLACK) {
                    blackTrappedStones++;
                } else if (c == Color.WHITE) {
                    whiteTrappedStones++;
                }
            }

            // Recursively calculate the score for neighboring stones
            groupScore += calculateGroupScore(row + 1, col, c, visited, blackTrappedStones, whiteTrappedStones); // Check down
            groupScore += calculateGroupScore(row - 1, col, c, visited, blackTrappedStones, whiteTrappedStones); // Check up
            groupScore += calculateGroupScore(row, col + 1, c, visited, blackTrappedStones, whiteTrappedStones); // Check right
            groupScore += calculateGroupScore(row, col - 1, c, visited, blackTrappedStones, blackTrappedStones); // Check left
        }

        return groupScore;
    }

    // Helper method to check if a stone group is alive
    // Helper method to check if a stone group is alive using flood fill
    private boolean isAlive(int row, int col, boolean[][] visited) {
        Color stoneColor = board.getBoard()[row][col];

        // Create a separate matrix to track the visited intersections during flood fill
        boolean[][] groupVisited = new boolean[board.getSize()][board.getSize()];

        // Perform flood fill starting from the specified stone
        calculateGroupScore(row, col, stoneColor, visited);

        // Check if any empty intersection is reached during flood fill
        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                if (groupVisited[i][j] && this.getBoard().getBoard()[i][j] == Color.TRANSPARENT) {
                    return true; // At least one empty intersection found, the group is alive
                }
            }
        }

        return false; // No empty intersection found, the group is dead
    }*/
}

