package com.example.go_gruppe1;

import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class FileControl {
    private File outputFile;
    private boardMaskController controller;

    private int fileNameCounter = 0;
    private int movesCounter = 0;

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
                //String startInfo = player1Name + " vs. " + player2Name + "\nBoardSize: " + boardSize + "\n" + komi;
                //writeToPosition(startInfo);
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
        writeToPosition(jsonObject.toJSONString());
    }

    protected void writeMoves(String data){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(movesCounter, data);
        movesCounter++;
        writeToPosition(jsonObject.toJSONString());
    }

    protected void writeAction(String data){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(data,data);
        writeToPosition(jsonObject.toJSONString());
    }
    protected void writeToPosition(String data) {
        try {
            FileWriter writer = new FileWriter(outputFile.getName(), true);
            writer.append(data);
            writer.close();
        }catch (IOException e){
            System.out.println("an IO Exception was thrown when trying to write to " + outputFile.getName());
        }
    }

    protected void renameFile(String newName){
        File f = new File(newName + ".json");
        if(outputFile.renameTo(f)){
            controller.setSampleSolutionDisplay("File successfully renamed!");
            System.out.println("\nThe name of the output file has successfully been changed to " + outputFile.getName());
        } else {
            System.out.println("Renaming unsuccessfully, check if a file with the same name already exists in the selected directory and try again");
            controller.setSampleSolutionDisplay("Renaming unsuccessfully, check if a file with the same name already exists in the selected directory and try again");
        }
    }

    protected void loadFile(File newFile){
        JSONParser jsonParser = new JSONParser();
        try {
            String fileContents = new String(Files.readAllBytes(Paths.get(outputFile.getAbsolutePath())));
            JSONObject jsonObject = (JSONObject) jsonParser.parse(fileContents);
            controller.switchToNewGame((String) jsonObject.get("player1Name"), (String) jsonObject.get("player2Name"), (Double) jsonObject.get("komi"),
                    (Long) jsonObject.get("handicaps"), (Long) jsonObject.get("boardSize"));
        } catch (ParseException | IOException e) {
            System.out.println("an IO Exception was thrown when trying to load file " + newFile.getName());
        }
    }

    //TODO Save as
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