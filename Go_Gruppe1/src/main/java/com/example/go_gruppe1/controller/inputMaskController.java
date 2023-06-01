package com.example.go_gruppe1.controller;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class inputMaskController {

    @FXML
    private GridPane inputPane;

    @FXML
    private RadioButton size9, size13;

    @FXML
    private TextField player1, player2, handicaps, byoyomiNumber, byoyomiTime;

    @FXML
    private Spinner<Double> komiSpinner;

    @FXML
    private Label title;

    @FXML
    private Text sizeText, namesText, komiText, handicapText,byoyomiText;

    @FXML
    private Button start, load;

    public void initiateDisplay() {
        initiateSpinner();
        initiateLabels();
        initiateButtons();
    }

    public void onStartGameClick(ActionEvent event) throws IOException {
        switchToBoardMask(event, false);
    }

    public void onLoadGameClick(ActionEvent event) throws IOException {
        switchToBoardMask(event, true);
    }

    protected void initiateSpinner() {
        komiSpinner.setEditable(false);

        SpinnerValueFactory<Double> valueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 9.5, 0, 0.5);
        komiSpinner.setValueFactory(valueFactory);
        komiSpinner.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", inputPane.heightProperty().multiply(0.02).asString()
        ));
    }

    protected void initiateLabels() {
        double binding = 0.04;
        title.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", inputPane.heightProperty().multiply(0.1).asString()
        ));

        sizeText.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", inputPane.heightProperty().multiply(binding).asString()
        ));

        namesText.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", inputPane.heightProperty().multiply(binding).asString()
        ));

        komiText.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", inputPane.heightProperty().multiply(binding).asString()
        ));

        handicapText.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", inputPane.heightProperty().multiply(binding).asString()
        ));

        byoyomiText.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", inputPane.heightProperty().multiply(binding).asString()
        ));
    }

    private void initiateButtons() {
        start.prefWidthProperty().bind(inputPane.heightProperty().multiply(15));
        start.styleProperty().bind(Bindings.concat(
                "-fx-text-fill: #483C32; ",
                "-fx-font-weight: bold; ",
                "-fx-font-size: ", inputPane.heightProperty().multiply(0.035).asString()
        ));
        start.toFront();

        load.prefWidthProperty().bind(inputPane.heightProperty().multiply(15));
        load.styleProperty().bind(Bindings.concat(
                "-fx-text-fill: #483C32; ",
                "-fx-font-weight: bold; ",
                "-fx-font-size: ", inputPane.heightProperty().multiply(0.035).asString()
        ));
        load.toFront();
    }



    private void switchToBoardMask(ActionEvent event, boolean wantToLoad) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/boardMaskGUI.fxml"));
        Parent root = loader.load();

        boardMaskController boardMask = loader.getController();
        boardMask.setSize(inputPane.getWidth(), inputPane.getHeight());

        boardMask.initiateDisplay(player1.getText(), player2.getText(), komiSpinner.getValue().toString(), handicaps.getText(), getBoardSize());
        boardMask.initiateTimeRules(byoyomiNumber.getText(), byoyomiTime.getText());
        if(wantToLoad)
            boardMask.onOpenFileClick();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setMinWidth(630);
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