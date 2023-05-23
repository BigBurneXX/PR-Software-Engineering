package com.example.go_gruppe1;

import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
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

    public FileControl(){}
    protected void createFile(boardMaskController controller, String oldFileName, String player1Name, String player2Name, int boardSize, double komi, int handicaps){
        this.controller = controller;
        try{
            String newFileName = oldFileName.endsWith(".json") ?
                    oldFileName.endsWith("_" + (fileNameCounter-1) + ".json") ?
                            oldFileName.substring(0, oldFileName.length() - (6 + String.valueOf((fileNameCounter-1)).length())) + "_" + fileNameCounter + ".json" :
                            oldFileName.substring(0, oldFileName.length() -5) + "_" + fileNameCounter + ".json":
                    player1Name + "_" + player2Name + ".json";
            outputFile = new File(newFileName);
            if (outputFile.createNewFile()) {
                writeStartInfo(player1Name,player2Name,boardSize,komi, handicaps);
                System.out.println("File " + outputFile.getName() + " created.");
            }else {
                System.out.println("File " + outputFile.getName() + " already exists!");
                fileNameCounter++;
                createFile(controller, outputFile.getName(), player1Name, player2Name, boardSize, komi, handicaps);
            }
        } catch (IOException e ){
            System.out.println("File " + outputFile.getName() + " creation failed!");
            e.printStackTrace();
        }
    }

    private void writeStartInfo(String player1Name, String player2Name, int boardSize, double komi, int handicaps){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("player1Name", player1Name);
        jsonObject.put("player2Name", player2Name);
        jsonObject.put("boardSize", boardSize);
        jsonObject.put("komi", komi);
        jsonObject.put("handicaps", handicaps);
        jsonObject.put("moves", movesLog);
        jsonObject.put("boardChanges", boardLog);
        try (FileWriter fileWriter = new FileWriter(outputFile.getAbsolutePath())) {
            fileWriter.write(jsonObject.toJSONString());
            fileWriter.flush();
            System.out.println("JSON data has been written to the file successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void writeMoves(int row, char col, String text){
        movesLog.add(new Move(row, col, text));
        updateMovesLog(row, col, text);
    }

    protected void updateMovesLog(int row, char col, String text){
        try {
            // Parse the JSON file
            //JSONParser parser = new JSONParser();
            JSONObject jsonObject;
            String fileContents = new String(Files.readAllBytes(Paths.get(outputFile.getAbsolutePath())));
            jsonObject = (JSONObject) parser.parse(fileContents);

            // Retrieve the existing "moves" array
            JSONArray movesArray = (JSONArray) jsonObject.get("moves");

            // Create a new move record and add it to the "moves" array
            JSONObject newMove = new JSONObject();
            newMove.put(moveCounter + "row", row);
            newMove.put(moveCounter + "col", String.valueOf(col));
            newMove.put(moveCounter + "text", text);
            movesArray.add(newMove);
            moveCounter++;

            // Write the updated JSON object back to the file
            try (FileWriter fileWriter = new FileWriter(outputFile.getAbsolutePath())) {
                fileWriter.write(jsonObject.toJSONString());
                fileWriter.flush();

                System.out.println("JSON data has been updated and written to the file successfully.");
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    protected void writeAction(String data){
        /* implementation of pass and resign? */
        /* resign maybe with boolean value? */
    }

    protected void loadFile(File newFile){
        try {
            String fileContents = new String(Files.readAllBytes(Paths.get(newFile.getAbsolutePath())));
            JSONObject jsonObject = (JSONObject) parser.parse(fileContents);
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
                    jsonObject.get("handicaps").toString(), ((Long) jsonObject.get("boardSize")).intValue(), movesLoaded);
        } catch (ParseException | IOException e) {
            System.out.println("an IO Exception was thrown when trying to load file " + newFile.getName());
        }
    }

    protected void saveFile(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save to location");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json"));
        File selectedDirectory = fileChooser.showSaveDialog(null);
        if (selectedDirectory != null) {
            System.out.println("Selected directory: " + selectedDirectory.getAbsolutePath());
            try {
                Files.move(Paths.get(outputFile.getAbsolutePath()), Paths.get(selectedDirectory.getAbsolutePath()));
                System.out.println("File moved successfully from " + outputFile.getAbsolutePath() + " to " + selectedDirectory.getAbsolutePath());
                outputFile = selectedDirectory;
            } catch (IOException e) {
                System.out.println("an IO Exception was thrown when trying to save file to " + outputFile.getName());
            }
        } else {
            System.out.println("No directory selected");
        }
    }
}