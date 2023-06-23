package com.example.go_gruppe1.model.file;

import javafx.stage.FileChooser;

import java.io.File;

public class FileHandler {
    private final FileControl fileControl = new FileControl();

    /**
     * @param playerBlack black player
     * @param playerWhite white player
     * @param boardSize size of board
     * @param komi komi advantage for white
     * @param handicaps advantage for black
     * @param byoyomiOverruns # of byoyomi time periods
     * @param byoyomiTimeLimit time of byoyomi period
     *
     * initiates file handler with necessary game information
     */
    public FileHandler(String playerBlack, String playerWhite, int boardSize, double komi, int handicaps, int byoyomiOverruns, int byoyomiTimeLimit){
        fileControl.createFile("", playerBlack, playerWhite, boardSize, komi, handicaps, byoyomiOverruns, byoyomiTimeLimit);
    }

    /**
     * saves file
     */
    public void save(){
        fileControl.saveFile();
    }

    /**
     * @return data of file
     *
     * opens file and returns information
     */
    public FileData open(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null)
            return fileControl.loadFile(selectedFile);
        return null;
    }

    /**
     * writes info to file when pass move is made
     */
    public void pass(){
        fileControl.writeMoves(-1, 'p', "");
    }

    /**
     * writes info to file when resign move is made
     */
    public void resign(){
        fileControl.writeMoves(-2, 'r', "");
    }

    /**
     * @param row location of set stone
     * @param col location of set stone
     * @param text location of set stone (alphabetical)
     *
     * writes to file when stone is set or resign/pass is drawn
     */
    public void write(int row, char col, String text){
        fileControl.writeMoves(row, col, text);
    }
}
