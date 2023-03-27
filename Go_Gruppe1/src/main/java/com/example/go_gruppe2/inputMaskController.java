package com.example.go_gruppe2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class inputMaskController {

    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private Label boardSizeLabel;

    @FXML
    private RadioButton size9, size11, size13, size19;

    @FXML
    private Button startButton;

    @FXML
    private TextField player1, player2;

    public void switchToBoardMask(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("boardMaskGUI.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    public void switchToInputMask(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("inputMaskGUI.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public int getBoardSize(ActionEvent event) throws IOException {
        if(size9.isSelected()) {
            return 9;
        } else if(size11.isSelected()) {
            return 11;
        } else if(size13.isSelected()) {
            return 13;
        } else {
            return 19;
        }
    }


}