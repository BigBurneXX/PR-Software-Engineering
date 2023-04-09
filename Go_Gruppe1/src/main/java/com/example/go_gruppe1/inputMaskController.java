package com.example.go_gruppe1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class inputMaskController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private GridPane inputPane;

    @FXML
    private RadioButton size9, size11, size13, size19;

    @FXML
    private ToggleGroup boardSize;

    @FXML
    private Button startButton;

    @FXML
    private TextField player1, player2, komi, handicaps;

    public void switchToBoardMask(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/boardMaskGUI.fxml"));
        root = loader.load();

        boardMaskController boardMask = loader.getController();
        boardMask.setSize(getWidth(), getHeight());
        boardMask.displayPlayerNames(player1.getText(), player2.getText());
        boardMask.displayKomi(komi.getText());
        boardMask.displayHandicaps(handicaps.getText());
        boardMask.displayBlackTrapped("");
        boardMask.displayWhiteTrapped("");
        boardMask.playActivate();
        boardMask.drawBoard(getBoardSize(new ActionEvent()));

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public int getBoardSize(ActionEvent event) {
        if (size9.isSelected()) {
            return 9;
        } else if (size11.isSelected()) {
            return 11;
        } else if (size13.isSelected()) {
            return 13;
        } else {
            return 19;
        }
    }

    public double getWidth() {
        return inputPane.getWidth();
    }

    public double getHeight() {
        return inputPane.getHeight();
    }

    public void setSize(double width, double height) {
        inputPane.setPrefHeight(height);
        inputPane.setPrefWidth(width);
    }
}