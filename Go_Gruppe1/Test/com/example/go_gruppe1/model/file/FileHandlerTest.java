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
        try {
            // Assuming that saveFile creates or modifies a file
            File beforeSave = new File("filename.json"); // Replace with your actual filename or path
            long beforeSaveTimestamp = beforeSave.lastModified();

            // wait a second to make sure there is a difference in the timestamp
            Thread.sleep(1000);

            fileHandler.save();

            File afterSave = new File("filename.json"); // Replace with your actual filename or path
            long afterSaveTimestamp = afterSave.lastModified();

            assertTrue(afterSaveTimestamp > beforeSaveTimestamp, "File was not saved");
        } catch (Exception e) {
            fail("save() method threw an exception: " + e.getMessage());
        }
    }

    @Test
    public void testOpen() {
        // Ensure there is a file that the method can open
        File testFile = new File("testFile.json"); // Replace with your actual filename or path

        if (!testFile.exists()) {
            // Generate a file for the method to open
            try (PrintWriter out = new PrintWriter(new FileOutputStream(testFile))) {
                out.println("{}");
            } catch (FileNotFoundException e) {
                fail("Failed to create a file for the open() method to test with: " + e.getMessage());
            }
        }

        try {
            FileData fileData = fileHandler.open();
            assertNotNull(fileData, "Failed to open file");
        } catch (Exception e) {
            fail("open() method threw an exception: " + e.getMessage());
        }
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