package com.example.go_gruppe1.model.file;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.*;

public class FileHandlerTest {
    private FileHandler fileHandler;
    private FileControl fileControl;

    @BeforeEach
    public void setup() {
        // Initialize the FileHandler with test data
        this.fileControl = new FileControl();
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
    void testSave() {
        File file = this.fileControl.getOutputFile();
        // other test code
    }

    @Test
    void testOpen() {
        File file = this.fileControl.getOutputFile();
        // other test code
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