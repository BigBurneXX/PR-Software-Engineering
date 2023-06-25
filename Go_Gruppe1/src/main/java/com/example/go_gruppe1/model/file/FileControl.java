package com.example.go_gruppe1.model.file;

import javafx.stage.FileChooser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileControl {
    private File outputFile;
    private final List<Move> movesLog = new ArrayList<>();
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private int fileNameCounter = 0;

    /**
     * @param oldFileName old file name
     * @param player1Name black player name
     * @param player2Name white player name
     * @param boardSize size of board
     * @param komi advantage for white
     * @param handicaps advantage for black
     * @param byoyomiOverruns # of byoyomi time periods
     * @param byoyomiTimeLimit time of byoyomi period
     *
     * creates file with game information in json format
     */
    public void createFile(String oldFileName, String player1Name, String player2Name,
                           int boardSize, double komi, int handicaps, int byoyomiOverruns, int byoyomiTimeLimit) {
        try {
            String newFileName = oldFileName.endsWith(".json") ?
                    oldFileName.endsWith("_" + (fileNameCounter - 1) + ".json") ?
                            oldFileName.substring(0, oldFileName.length() - (6 + String.valueOf((fileNameCounter - 1)).length())) + "_" + fileNameCounter + ".json" :
                            oldFileName.substring(0, oldFileName.length() - 5) + "_" + fileNameCounter + ".json" :
                    player1Name + "_" + player2Name + ".json";
            outputFile = new File(newFileName);
            if (outputFile.createNewFile()) {
                writeStartInfo(player1Name, player2Name, boardSize, komi, handicaps, byoyomiOverruns, byoyomiTimeLimit);
                terminalInfo("File " + outputFile.getName() + " created.");
            } else {
                terminalInfo("File " + outputFile.getName() + " already exists!");
                fileNameCounter++;
                createFile(outputFile.getName(), player1Name, player2Name, boardSize, komi, handicaps, byoyomiOverruns, byoyomiTimeLimit);
            }
        } catch (IOException e) {
            terminalInfo("File " + outputFile.getName() + " creation failed!");
            e.printStackTrace();
        }
    }

    /**
     * @param player1Name black player name
     * @param player2Name white player name
     * @param boardSize size of board
     * @param komi advantage for white
     * @param handicaps advantage for black
     * @param byoyomiOverruns # of byoyomi time periods
     * @param byoyomiTimeLimit time of byoyomi period
     *
     * writes initial information to file
     */
    private void writeStartInfo(String player1Name, String player2Name, int boardSize, double komi, int handicaps,
                                int byoyomiOverruns, int byoyomiTimeLimit) {
        FileData fileData = new FileData(player1Name, player2Name, boardSize, komi, handicaps, byoyomiOverruns, byoyomiTimeLimit, movesLog);

        try (FileWriter fileWriter = new FileWriter(outputFile.getAbsolutePath())) {
            String json = gson.toJson(fileData);
            fileWriter.write(json);
            fileWriter.flush();

            //terminalInfo("JSON data has been written to the file successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param row location of set stone
     * @param col location of set stone
     * @param text location of set stone (alphabetical)
     * <p>
     * writes move to list
     * if player passes row = -1, col = 'p'
     * if player resigns row = -2, col = 'r'
     */
    public void writeMoves(int row, char col, String text) {
        movesLog.add(new Move(row, col, text));
        updateMovesLog(row, col, text);
    }

    /**
     * @param row location of set stone
     * @param col location of set stone
     * @param text location of set stone (alphabetical)
     *
     * updates move list in json file
     */
    private void updateMovesLog(int row, char col, String text) {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(outputFile.getAbsolutePath()))) {
            Type type = new TypeToken<FileData>() {
            }.getType();
            FileData fileData = gson.fromJson(reader, type);

            if (fileData != null) {
                fileData.moves().add(new Move(row, col, text));

                try (FileWriter fileWriter = new FileWriter(outputFile.getAbsolutePath())) {
                    gson.toJson(fileData, fileWriter);
                    fileWriter.flush();

                    terminalInfo("JSON data has been updated and written to the file successfully.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param newFile file to be loaded
     * @return data of loaded file
     *
     * loads file and returns data
     */
    public FileData loadFile(File newFile) {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(newFile.getAbsolutePath()))) {
            Type type = new TypeToken<FileData>() {
            }.getType();
            return gson.fromJson(reader, type);
        } catch (IOException e) {
            terminalInfo("An IO Exception was thrown when trying to load file " + newFile.getName());
        }
        return null;
    }

    /**
     * saves json document to selected directory
     */
    public void saveFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save to location");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json"));
        File selectedDirectory = fileChooser.showSaveDialog(null);
        if (selectedDirectory != null) {
            terminalInfo("Selected directory: " + selectedDirectory.getAbsolutePath());
            try {
                Files.move(outputFile.toPath(), selectedDirectory.toPath());
                terminalInfo("File moved successfully from " + outputFile.getAbsolutePath() + " to " + selectedDirectory.getAbsolutePath());
                outputFile = selectedDirectory;
            } catch (IOException e) {
                terminalInfo("an IO Exception was thrown when trying to save file to " + selectedDirectory.getName());
            }
        } else {
            terminalInfo("No directory selected");
        }
    }

    /**
     * @param data information
     *
     * prints information to console for debugging
     */
    private void terminalInfo(String data) {
        System.out.println(data);
    }
}