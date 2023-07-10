package com.example.go_gruppe1.model.file;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

public class FileControlTest {
    private FileControl fileControl;
    private File outputFile;

    @BeforeEach
    public void setUp() throws IOException {
        fileControl = new FileControl();
        outputFile = File.createTempFile("testFile", null);
        // Delete the temp file, we will recreate it in testCreateFile
        outputFile.delete();
        System.out.println(outputFile.getAbsolutePath());
    }

    @Test
    public void testCreateFile() throws NoSuchFieldException, IllegalAccessException {
        String fileNameWithoutExtension = outputFile.getName().replaceFirst("[.][^.]+$", "");
        fileControl.createFile(fileNameWithoutExtension, "BlackPlayer", "WhitePlayer", 19, 6.5, 0, 3, 30);

        // Use reflection to get outputFile from fileControl
        Field field = FileControl.class.getDeclaredField("outputFile");
        field.setAccessible(true);
        File outputFileInFileControl = (File) field.get(fileControl);

        // Assert that outputFile in fileControl exists
        assertTrue(outputFileInFileControl.exists());

        // Load the data from outputFile in fileControl
        FileData fileData = fileControl.loadFile(outputFileInFileControl);
        assertNotNull(fileData);
        assertEquals("BlackPlayer", fileData.player1Name());
        assertEquals("WhitePlayer", fileData.player2Name());
        assertEquals(19, fileData.boardSize());
        assertEquals(6.5, fileData.komi(), 0.001);  // for floating point comparison, provide a delta
        assertEquals(0, fileData.handicaps());
        assertEquals(3, fileData.byoyomiOverruns());
        assertEquals(30, fileData.byoyomiTimeLimit());
        assertEquals(0, fileData.moves().size());
    }


    @Test
    public void testWriteMoves() {
        fileControl.createFile(outputFile.getName(), "BlackPlayer", "WhitePlayer", 19, 6.5, 0, 3, 30);

        fileControl.writeMoves(3, 'c', "Move");
        fileControl.writeMoves(-1, 'p', "");
        fileControl.writeMoves(-2, 'r', "");

        FileData fileData = fileControl.loadFile(outputFile);
        assertNotNull(fileData);
        assertEquals(3, fileData.moves().size());

        Move move1 = fileData.moves().get(0);
        assertEquals(3, move1.row());
        assertEquals('c', move1.col());
        assertEquals("Move", move1.text());

        Move move2 = fileData.moves().get(1);
        assertEquals(-1, move2.row());
        assertEquals('p', move2.col());
        assertEquals("", move2.text());

        Move move3 = fileData.moves().get(2);
        assertEquals(-2, move3.row());
        assertEquals('r', move3.col());
        assertEquals("", move3.text());
    }

    @Test
    public void testLoadFile() {
        FileData expectedFileData = new FileData("BlackPlayer", "WhitePlayer", 19, 6.5, 0, 3, 30, new ArrayList<>());
        writeJsonToFile(outputFile, expectedFileData);

        FileData fileData = fileControl.loadFile(outputFile);
        assertNotNull(fileData);
        assertEquals(expectedFileData.player1Name(), fileData.player1Name());
        assertEquals(expectedFileData.player2Name(), fileData.player2Name());
        assertEquals(expectedFileData.boardSize(), fileData.boardSize());
        assertEquals(expectedFileData.komi(), fileData.komi());
        assertEquals(expectedFileData.handicaps(), fileData.handicaps());
        assertEquals(expectedFileData.byoyomiOverruns(), fileData.byoyomiOverruns());
        assertEquals(expectedFileData.byoyomiTimeLimit(), fileData.byoyomiTimeLimit());
        assertEquals(expectedFileData.moves().size(), fileData.moves().size());
    }

    @Test
    public void testSaveFile() throws IOException, NoSuchFieldException, IllegalAccessException {
        fileControl.createFile("tempFile.json", "BlackPlayer", "WhitePlayer", 19, 6.5, 0, 3, 30);

        File targetDirectory = Files.createTempDirectory("target").toFile();
        File destinationFile = new File(targetDirectory, "newLocation.json");

        // Use Java Reflection to set a new value for outputFile in fileControl
        Field outputFileField = fileControl.getClass().getDeclaredField("outputFile");
        outputFileField.setAccessible(true); // allows us to access private fields
        outputFileField.set(fileControl, destinationFile);

        fileControl.saveFile();

        // The outputFile field is private, so you have to retrieve it again to perform the assertions
        File outputFile = (File) outputFileField.get(fileControl);

        assertFalse(outputFile.exists());
        assertTrue(destinationFile.exists());
    }

    private FileData loadFileData(File file) {
        try {
            String json = new String(Files.readAllBytes(file.toPath()));
            return new Gson().fromJson(json, FileData.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void writeJsonToFile(File file, FileData fileData) {
        try (FileWriter fileWriter = new FileWriter(file.getAbsolutePath())) {
            String json = new Gson().toJson(fileData);
            fileWriter.write(json);
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
