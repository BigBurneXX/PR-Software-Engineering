package com.example.go_gruppe1.model.file;

import javafx.stage.FileChooser;

import java.io.File;

public class FileHandler {
    private final FileControl fileControl = new FileControl();

    public FileHandler(String playerBlack, String playerWhite, int boardSize, double komi, int handicaps, int byoyomiOverruns, int byoyomiTimeLimit){
        fileControl.createFile("", playerBlack, playerWhite, boardSize, komi, handicaps, byoyomiOverruns, byoyomiTimeLimit);
    }

    public void save(){
        fileControl.saveFile();
    }

    public FileData open(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null)
            return fileControl.loadFile(selectedFile);
        return null;
    }

    public void pass(){
        fileControl.writeMoves(-1, 'p', "");
    }

    public void resign(){
        fileControl.writeMoves(-2, 'r', "");
    }

    public void write(int row, char col, String text){
        fileControl.writeMoves(row, col, text);
    }
}
