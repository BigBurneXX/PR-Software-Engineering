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
    private RadioButton size9, size13, size19;

    @FXML
    private TextField player1, player2;

    @FXML
    private Spinner<Double> komiSpinner;

    @FXML
    private Spinner<Integer> handicapSpinner, timePeriodSpinner, durationSpinner;

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

    @FXML
    public void onStartGameClick(ActionEvent event) throws IOException {
        switchToBoardMask(event, false);
    }

    @FXML
    public void onLoadGameClick(ActionEvent event) throws IOException {
        switchToBoardMask(event, true);
    }

    private void initiateSpinner() {
        //komi
        komiSpinner.setEditable(false);

        SpinnerValueFactory<Double> komiValueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 9.5, 0, 0.5);
        komiSpinner.setValueFactory(komiValueFactory);
        komiSpinner.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", inputPane.heightProperty().multiply(0.02).asString()
        ));

        //handicaps
        handicapSpinner.setEditable(false);

        SpinnerValueFactory.IntegerSpinnerValueFactory handicapValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 5, 0, 1);
        handicapSpinner.setValueFactory(handicapValueFactory);
        handicapSpinner.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", inputPane.heightProperty().multiply(0.02).asString()
        ));

        //more handicaps are allowed for 19x19
        size19.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                handicapValueFactory.setMax(9);
            } else {
                handicapValueFactory.setMax(5);
            }
        });

        //byoyomi
        timePeriodSpinner.setEditable(true);

        SpinnerValueFactory<Integer> timePeriodValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0, 1);
        timePeriodSpinner.setValueFactory(timePeriodValueFactory);
        timePeriodSpinner.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", inputPane.heightProperty().multiply(0.02).asString()
        ));

        durationSpinner.setEditable(true);

        SpinnerValueFactory.IntegerSpinnerValueFactory durationValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0, 1);
        durationSpinner.setValueFactory(durationValueFactory);
        durationSpinner.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", inputPane.heightProperty().multiply(0.02).asString()
        ));

        timePeriodSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue > 0) {
                durationValueFactory.setMin(30);
                durationValueFactory.setValue(30);
            } else {
                durationValueFactory.setMin(0);
                durationValueFactory.setValue(0);
            }
        });

    }

    private void initiateLabels() {
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

        String player1Name = player1.getText().isEmpty() ? "Player 1" : player1.getText();
        String player2Name = player2.getText().isEmpty() ? "Player 2" : player2.getText();
        double komi = komiSpinner.getValue();
        int handicaps = handicapSpinner.getValue();
        int boardSize = getBoardSize();
        int byoyomiOverruns = timePeriodSpinner.getValue();
        int byoyomiTimeLimit = durationSpinner.getValue();

        boardMask.initiateDisplay(player1Name, player2Name, komi, handicaps, boardSize, byoyomiOverruns, byoyomiTimeLimit);

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
            return 9;
        } else if (size13.isSelected()) {
            return 13;
        } else {
            return 19;
        }
    }

    protected void setSize(double width, double height) {
        inputPane.setPrefHeight(height);
        inputPane.setPrefWidth(width);
        inputPane.setMinSize(600, 580);
    }
}