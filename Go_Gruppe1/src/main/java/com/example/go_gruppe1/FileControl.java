package com.example.go_gruppe1;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileControl {
    private File outputFile;
    private boardMaskController controller;

    protected void createFile(boardMaskController controller, String oldFileName, String player1Name, String player2Name, int boardSize, String komi){
        this.controller = controller;
        try{
            String newFileName = oldFileName.endsWith(".txt") ?
                    oldFileName.substring(0, oldFileName.length() -4) + "_1.txt" : player1Name + "_" + player2Name + ".txt";
            outputFile = new File(newFileName);
            if (outputFile.createNewFile()) {
                String startInfo = player1Name + " vs. " + player2Name + "\nBoardSize: " + boardSize + "\n" + komi;
                writeToPosition(startInfo);
                System.out.println("File " + outputFile.getName() + " created.");
            }else {
                System.out.println("File " + outputFile.getName() + " already exists!");
                createFile(controller, outputFile.getName(), player1Name, player2Name, boardSize, komi);
            }
        } catch (IOException e ){
            System.out.println("File " + outputFile.getName() + " creation failed!");
            e.printStackTrace();
        }
    }

    protected void writeToPosition(String data) {
        try {
            FileWriter writer = new FileWriter(outputFile.getName(), true);
            writer.append(data);
            writer.close();
        }catch (IOException e){
            System.out.println("something went wrong when writing to the outputfile");
        }
    }

    protected void renameFile(String newName){
        File f = new File(newName + ".txt");
        if(outputFile.renameTo(f)){
            controller.setSampleSolutionDisplay("File successfully renamed!");
            System.out.println("\nThe name of the output file has successfully been changed to " + outputFile.getName());
        } else {
            System.out.println("Renaming unsuccessfully, check if a file with the same name already exists in the selected directory and try again");
            controller.setSampleSolutionDisplay("Renaming unsuccessfully, check if a file with the same name already exists in the selected directory and try again");
        }
    }

    protected void loadFile(File newFile){
        //still needs implementing
    }
}
