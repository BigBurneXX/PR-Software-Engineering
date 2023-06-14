package com.example.go_gruppe1.model;

import com.example.go_gruppe1.controller.boardMaskController;
import javafx.stage.FileChooser;

import java.io.File;

public class FileHandler {
    private final FileControl fileControl = new FileControl();

    public FileHandler(boardMaskController controller, String playerBlack, String playerWhite, int boardSize, double komi, int handicaps, int byoyomiNumber, int byoyomiTime){
        fileControl.createFile(controller, "", playerBlack, playerWhite, boardSize, komi, handicaps, byoyomiNumber, byoyomiTime);
    }

    public void save(){
        fileControl.saveFile();
    }

    public void open(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            fileControl.loadFile(selectedFile);
        }
    }

    public void write(int row, char col, String text){
        fileControl.writeMoves(row, col, text);
    }
}
