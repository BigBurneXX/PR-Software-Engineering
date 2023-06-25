package com.example.go_gruppe1.model.file;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FileHandlerTest {
    private FileHandler fileHandler;

    @BeforeEach
    public void setup() {
        // Initialize the FileHandler with test data
        String playerBlack = "John";
        String playerWhite = "Jane";
        int boardSize = 19;
        double komi = 6.5;
        int handicaps = 2;
        int byoyomiOverruns = 3;
        int byoyomiTimeLimit = 60;
        fileHandler = new FileHandler(playerBlack, playerWhite, boardSize, komi, handicaps, byoyomiOverruns, byoyomiTimeLimit);
    }

    @Test
    public void testSave() {
        // Call the save method
        fileHandler.save();
    }

    @Test
    public void testOpen() {
        // Call the open method
        FileData fileData = fileHandler.open();

        assertNotNull(fileData);
    }

    @Test
    public void testPass() {
        // Call the pass-method
        fileHandler.pass();
    }

    @Test
    public void testResign() {
        // Call the resign-method
        fileHandler.resign();
    }

    @Test
    public void testWrite() {
        // Call the write-method
        int row = 5;
        char col = 'A';
        String text = "Stone";
        fileHandler.write(row, col, text);
    }
}