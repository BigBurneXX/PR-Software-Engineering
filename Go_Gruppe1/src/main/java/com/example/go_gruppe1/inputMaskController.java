package com.example.go_gruppe1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

public class inputMaskController {

    @FXML
    private GridPane inputPane;

    @FXML
    private RadioButton size9, size13, size19;

    @FXML
    private TextField player1, player2, komi, handicaps, byoyomiNumber, byoyomiTime;

    public void onStartGameClick(ActionEvent event) throws IOException {
        switchToBoardMask(event, false);
    }

    public void onLoadGameClick(ActionEvent event) throws IOException {
        switchToBoardMask(event, true);
    }

    private void switchToBoardMask(ActionEvent event, boolean wantToLoad) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/boardMaskGUI.fxml"));
        Parent root = loader.load();

        boardMaskController boardMask = loader.getController();
        boardMask.setSize(inputPane.getWidth(), inputPane.getHeight());

        boardMask.initiateDisplay(player1.getText(), player2.getText(), komi.getText(), handicaps.getText(), getBoardSize());
        boardMask.initiateTimeRules(byoyomiNumber.getText(), byoyomiTime.getText());
        if(wantToLoad)
            boardMask.onOpenFileClick();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setMinWidth(600);
        stage.setMinHeight((getBoardSize() == 19 ? 800: 650));
        stage.show();
    }

    public int getBoardSize() {
        if (size9.isSelected()) {
            handicaps.setPromptText("0 - 5");
            return 9;
        } else if (size13.isSelected()) {
            handicaps.setPromptText("0 - 5");
            return 13;
        } else {
            handicaps.setPromptText("0 - 9");
            return 19;
        }
    }

    protected void setSize(double width, double height) {
        inputPane.setPrefHeight(height);
        inputPane.setPrefWidth(width);
        inputPane.setMinSize(600, 580);
    }
}