package com.example.go_gruppe1.model;

import com.example.go_gruppe1.controller.boardMaskController;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class FileControl {
    private File outputFile;
    private boardMaskController controller;
    private final List<Move> movesLog = new ArrayList<>();
    private final List<Color[][]> boardLog = new ArrayList<>();
    private final JSONParser parser = new JSONParser();
    private int moveCounter = 0;
    private int fileNameCounter = 0;

    public void createFile(boardMaskController controller, String oldFileName, String player1Name, String player2Name,
                           int boardSize, double komi, int handicaps, int byoyomiNumberOfTimes, int byoyomiTimeLimit){
        this.controller = controller;
        try{
            String newFileName = oldFileName.endsWith(".json") ?
                    oldFileName.endsWith("_" + (fileNameCounter-1) + ".json") ?
                            oldFileName.substring(0, oldFileName.length() - (6 + String.valueOf((fileNameCounter-1)).length())) + "_" + fileNameCounter + ".json" :
                            oldFileName.substring(0, oldFileName.length() -5) + "_" + fileNameCounter + ".json":
                    player1Name + "_" + player2Name + ".json";
            outputFile = new File(newFileName);
            if (outputFile.createNewFile()) {
                writeStartInfo(player1Name,player2Name,boardSize,komi, handicaps, byoyomiNumberOfTimes, byoyomiTimeLimit);
                terminalInfo("File " + outputFile.getName() + " created.");
            }else {
                terminalInfo("File " + outputFile.getName() + " already exists!");
                fileNameCounter++;
                createFile(controller, outputFile.getName(), player1Name, player2Name, boardSize, komi, handicaps, byoyomiNumberOfTimes, byoyomiTimeLimit);
            }
        } catch (IOException e ){
            terminalInfo("File " + outputFile.getName() + " creation failed!");
            e.printStackTrace();
        }
    }

    private void writeStartInfo(String player1Name, String player2Name, int boardSize, double komi, int handicaps,
                                int byoyomiNumberOfTimes, int byoyomiTimeLimit){
        Map<String, Object> fileMap = new HashMap<>();
        fileMap.put("player1Name", player1Name);
        fileMap.put("player2Name", player2Name);
        fileMap.put("boardSize", boardSize);
        fileMap.put("komi", komi);
        fileMap.put("handicaps", handicaps);
        fileMap.put("byoyomiNumberOfTimes", String.valueOf(byoyomiNumberOfTimes));
        fileMap.put("byoyomiTimeLimit", String.valueOf(byoyomiTimeLimit));
        fileMap.put("moves", movesLog);
        fileMap.put("boardChanges", boardLog);

        try (FileWriter fileWriter = new FileWriter(outputFile.getAbsolutePath())) {
            fileWriter.write(new JSONObject(fileMap).toJSONString());
            fileWriter.flush();

            terminalInfo("JSON data has been written to the file successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //if player passes row = -1, col = 'p'
    //if player resigns row = -2, col = 'r'
    public void writeMoves(int row, char col, String text){
        movesLog.add(new Move(row, col, text));
        updateMovesLog(row, col, text);
    }

    protected void updateMovesLog(int row, char col, String text){
        // Read the existing JSON file
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(outputFile.getAbsolutePath()))) {
            // Parse the JSON file incrementally
            JSONParser parser = new JSONParser();
            Object fileContents = parser.parse(reader);

            // Retrieve the existing "moves" array
            JSONObject jsonObject = (JSONObject) fileContents;
            JSONArray movesArray = (JSONArray) jsonObject.get("moves");

            // Create a new move record and add it to the "moves" array
            Map<String, Object> newMoveMap = new HashMap<>();
            newMoveMap.put(moveCounter + "row", row);
            newMoveMap.put(moveCounter + "col", String.valueOf(col));
            newMoveMap.put(moveCounter + "text", text);
            movesArray.add(new JSONObject(newMoveMap));
            moveCounter++;

            // Write the updated JSON object back to the file
            try (FileWriter fileWriter = new FileWriter(outputFile.getAbsolutePath())) {
                fileWriter.write(jsonObject.toJSONString());
                fileWriter.flush();

                terminalInfo("JSON data has been updated and written to the file successfully.");
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public void loadFile(File newFile){
        try(BufferedReader reader = Files.newBufferedReader(Paths.get(newFile.getAbsolutePath()))) {
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            JSONArray jsonArray = (JSONArray) jsonObject.get("moves");
            List<Move> movesLoaded = new ArrayList<>();
            AtomicInteger counter = new AtomicInteger();
            if(jsonArray != null)
                jsonArray.iterator().forEachRemaining(element -> {
                    JSONObject toAdd = (JSONObject) element;
                    movesLoaded.add(new Move(((Long) toAdd.get(counter + "row")).intValue(), toAdd.get(counter + "col").toString().charAt(0),
                            (String) toAdd.get(counter + "text")));
                    counter.getAndIncrement();
                });
            controller.switchToNewGame((String) jsonObject.get("player1Name"), (String) jsonObject.get("player2Name"), jsonObject.get("komi").toString(),
                    jsonObject.get("handicaps").toString(), ((Long) jsonObject.get("boardSize")).intValue(), movesLoaded, (String) jsonObject.get("byoyomiNumberOfTimes"),
                    (String) jsonObject.get("byoyomiTimeLimit"));
        } catch (ParseException | IOException e) {
            terminalInfo("an IO Exception was thrown when trying to load file " + newFile.getName());
        }
    }

    public void saveFile(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save to location");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json"));
        File selectedDirectory = fileChooser.showSaveDialog(null);
        if (selectedDirectory != null) {
            terminalInfo("Selected directory: " + selectedDirectory.getAbsolutePath());
            try {
                Files.move(outputFile.toPath(),selectedDirectory.toPath());
                terminalInfo("File moved successfully from " + outputFile.getAbsolutePath() + " to " + selectedDirectory.getAbsolutePath());
                outputFile = selectedDirectory;
            } catch (IOException e) {
                terminalInfo("an IO Exception was thrown when trying to save file to " + selectedDirectory.getName());
            }
        } else {
            terminalInfo("No directory selected");
        }
    }

    private void terminalInfo(String data){
        System.out.println(data);
    }
}