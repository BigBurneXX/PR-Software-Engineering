package com.example.go_gruppe1;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;

public class boardMaskController {

    @FXML
    private Label pl1, pl2, sizeOfBoard, komiBoard, handicapsBoard;
    public void displayPlayerNames(String p1, String p2) {
        if(p1.isEmpty()) {
            if(!p2.equals("Player 1")); //names cannot be the same
            p1 = "Player 1";
        }
        if(p2.isEmpty()) {
            if(!p1.equals("Player 2")); //names cannot be the same
            p2 = "Player 2";
        }

        pl1.setText("Black is: " + p1);
        pl2.setText("White is: " + p2);
    }

    public void displayBoardSize(String size) {
        sizeOfBoard.setText("Boardsize: " + size + "x" + size);
    }

    public void displayKomi(String komiAdvantage) {
        komiBoard.setText("Komi: 0");

        if(Integer.valueOf(komiAdvantage) > 0) {
            komiBoard.setText("Komi: " + komiAdvantage);
        }
    }

    public void displayHandicaps(String handicaps) {
        handicapsBoard.setText("Handicaps: 0");

        if(Integer.valueOf(handicaps) > 0) {
            handicapsBoard.setText("Handicaps: " + handicaps);
        }
    }
}
