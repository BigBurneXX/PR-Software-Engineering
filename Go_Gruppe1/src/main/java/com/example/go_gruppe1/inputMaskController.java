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
    private ToggleGroup boardSize;

    @FXML
    private TextField player1, player2, komi, handicaps, byoyomiNumber, byoyomiTime;

    public void switchToBoardMask(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/boardMaskGUI.fxml"));
        Parent root = loader.load();

        boardMaskController boardMask = loader.getController();
        boardMask.setSize(this.getWidth(), this.getHeight());
        System.out.println(this.getWidth());
        System.out.println(this.getHeight());

        boardMask.initiateDisplay(player1.getText(), player2.getText(), komi.getText(), handicaps.getText(), getBoardSize());
        boardMask.initiateTimeRules(byoyomiNumber.getText(), byoyomiTime.getText());

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);


        stage.setScene(scene);
        stage.setMinWidth(600);
        stage.setMinHeight(650);

        //for larger boards, the window needs to be bigger
        if(getBoardSize() == 19) {
            stage.setMinHeight(800);
        }



        stage.show();
    }

    //doesn't fully work yet
    public void onLoadGameClick() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/boardMaskGUI.fxml"));
        boardMaskController boardMask = loader.getController();
        boardMask.onOpenFileClick();
    }

    public int getBoardSize() {
        if (size9.isSelected()) {
            return 9;
        } else if (size13.isSelected()) {
            return 13;
        } else {
            return 19;
        }
    }

    private double getWidth() {
        return inputPane.getWidth();
    }

    private double getHeight() {
        return inputPane.getHeight();
    }
    protected void setSize(double width, double height) {
        inputPane.setPrefHeight(height);
        inputPane.setPrefWidth(width);
        inputPane.setMinSize(600, 580);
    }

    public void size19Clicked(ActionEvent event) {
        handicaps.setPromptText("0 - 9");
    }

    public void size13Clicked(ActionEvent event) {
        handicaps.setPromptText("0 - 5");
    }

    public void size9Clicked(ActionEvent event) {
        handicaps.setPromptText("0 - 5");
    }
}