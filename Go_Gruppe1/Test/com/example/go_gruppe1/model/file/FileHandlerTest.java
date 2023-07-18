package com.example.go_gruppe1.model.file;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;

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
/*
    @Test
    void testSave() {
        //File file = this.fileControl.getOutputFile();
        // other test code
    }*/

    @Test
    public void testSave() throws NoSuchFieldException, IllegalAccessException {
        // Create a temporary file for testing
        File tempFile = new File("temp_file.json");

        // Create a FileHandler instance with some dummy data
        FileHandler fileHandler = new FileHandler("BlackPlayer", "WhitePlayer", 19, 6.5, 0, 5, 30);
/*
        // Assign the temporary file to the FileControl object within the FileHandler
        fileHandler.getFileControl().setFile(tempFile);
*/
        // Call the save method
        fileHandler.save();

        // Zugriff auf das private Attribut "name" durch Reflection
        Field outputFileField = FileControl.class.getDeclaredField("outputFile");
        outputFileField.setAccessible(true);
        File output = (File) outputFileField.get(fileControl);

        // Assert that the file exists
        assertTrue(output.exists());

        // Cleanup: Delete the temporary file
        tempFile.delete();
    }
/*
    @Test
    void testOpen() {
        //File file = this.fileControl.getOutputFile();
        // other test code
    }
*/
    @Test
    public void testOpen() {
        // Create a temporary file for testing
        File tempFile = new File("temp_file.json");

        // Create a FileHandler instance with some dummy data
        FileHandler fileHandler = new FileHandler("BlackPlayer", "WhitePlayer", 19, 6.5, 0, 5, 30);

        // Assign the temporary file to the FileControl object within the FileHandler
        //fileHandler.getFileControl().setFile(tempFile);

        // Call the open method
        FileData fileData = fileHandler.open();

        // Assert that the returned FileData is not null
        assertNotNull(fileData);

        // Assert that the data retrieved from the file is correct (you can add more specific checks based on your actual data structure)
        //assertEquals("BlackPlayer", fileData.playerBlack());
        //assertEquals("WhitePlayer", fileData.playerWhite());
        assertEquals(19, fileData.boardSize());
        assertEquals(6.5, fileData.komi());
        assertEquals(0, fileData.handicaps());
        assertEquals(5, fileData.byoyomiOverruns());
        assertEquals(30, fileData.byoyomiTimeLimit());

        // Cleanup: Delete the temporary file
        tempFile.delete();
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

    @Test
    public void testGetFileControl() {
        fileHandler.getFileControl();
    }
}