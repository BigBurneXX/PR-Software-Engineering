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
    private Text sizeText, namesText, komiText, handicapText, byoyomiText;

    @FXML
    private Button start, load;

    @FXML
    public void onStartGameClick(ActionEvent event) throws IOException {
        switchToBoardMask(event, false);
    }

    @FXML
    public void onLoadGameClick(ActionEvent event) throws IOException {
        switchToBoardMask(event, true);
    }

    /**
     * initiates all contents of the input mask
     */
    public void initiateDisplay() {
        initiateSpinner();
        initiateLabels();
        initiateButtons();
    }

    /**
     * initiates komi, handicap and byoyomi spinners
     */
    private void initiateSpinner() {
        createSpinnerFactory(komiSpinner, 9.5, "Komi");

        SpinnerValueFactory.IntegerSpinnerValueFactory handicapValueFactory =  createSpinnerFactory(handicapSpinner, 5, "Handicaps");
        //more handicaps are allowed for 19x19
        size19.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue)
                handicapValueFactory.setMax(9);
        });

        createSpinnerFactory(timePeriodSpinner, Integer.MAX_VALUE, "TimeOverruns");
        SpinnerValueFactory.IntegerSpinnerValueFactory durationValueFactory = createSpinnerFactory(durationSpinner, Integer.MAX_VALUE, "TimeLimit");
        timePeriodSpinner.valueProperty().addListener((observable, oldValue, newValue) -> durationValueFactory.setMin(newValue > 0 ? 30 : 0));
    }

    /**
     * @param spinner spinner to be initiated for INTEGER values
     * @param max max value of spinner
     * @param name name of spinner
     * @return SpinnerValueFactory for initialization of Spinner
     */
    private SpinnerValueFactory.IntegerSpinnerValueFactory createSpinnerFactory(Spinner<Integer> spinner, int max, String name){
        bindFont(spinner, 0.02);

        SpinnerValueFactory.IntegerSpinnerValueFactory spinnerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, max, 0, 1);
        spinner.setValueFactory(spinnerValueFactory);
        spinnerValueFactory.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue == null)
                spinnerValueFactory.setValue(0);
            else if(oldValue > spinnerValueFactory.getMax()){
                createAlert(name, spinnerValueFactory.getMax());
            }
        });

        return spinnerValueFactory;
    }

    /**
     * @param spinner spinner to be initiated for DOUBLE values
     * @param max max value of spinner
     * @param name name of spinner
     * @return SpinnerValueFactory for initialization of Spinner
     */
    private SpinnerValueFactory.DoubleSpinnerValueFactory createSpinnerFactory(Spinner<Double> spinner, double max, String name){
        bindFont(spinner, 0.02);

        SpinnerValueFactory.DoubleSpinnerValueFactory spinnerValueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, max, 0, 0.5);
        spinner.setValueFactory(spinnerValueFactory);
        spinnerValueFactory.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue == null)
                spinnerValueFactory.setValue(0.0);
            else if(oldValue > spinnerValueFactory.getMax())
                createAlert(name, spinnerValueFactory.getMax());
        });

        return spinnerValueFactory;
    }

    /**
     * @param name type of alert
     * @param max max value for input alert
     */
    private void createAlert(String name, double max){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(name + "-Warning");
        alert.setHeaderText(name + " cannot be greater than " + max + "!");
        alert.setContentText(name + " is/are set to possible maximum: " + max);
        alert.show();
    }

    /**
     * initiates labels and making them resizable
     */
    private void initiateLabels() {
        double binding = 0.04;
        bindFont(title, 0.1);
        bindFont(sizeText, binding);
        bindFont(namesText, binding);
        bindFont(komiText, binding);
        bindFont(handicapText, binding);
        bindFont(byoyomiText, binding);
    }

    /**
     * initiates start and load button
     */
    private void initiateButtons() {
        initiateButton(start);
        initiateButton(load);
    }

    /**
     * @param button button to be bound
     *
     * make buttons resizable
     */
    private void initiateButton(Button button){
        button.prefWidthProperty().bind(inputPane.heightProperty().multiply(15));
        button.styleProperty().bind(Bindings.concat(
                "-fx-text-fill: #483C32; ",
                "-fx-font-weight: bold; ",
                "-fx-font-size: ", inputPane.heightProperty().multiply(0.035).asString()
        ));
        button.toFront();
    }

    /**
     * @param event event for switching back to board mask
     * @param wantToLoad true if user wants to load a game
     * @throws IOException
     *
     * switches to board game mask (passes all necessary information)
     */
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

    /**
     * @return size of board
     */
    public int getBoardSize() {
        if (size9.isSelected())
            return 9;
        else if (size13.isSelected())
            return 13;
        else
            return 19;
    }

    /**
     * @param width width of window
     * @param height height of window
     *
     * sets window size
     */
    protected void setSize(double width, double height) {
        inputPane.setPrefHeight(height);
        inputPane.setPrefWidth(width);
        inputPane.setMinSize(600, 580);
    }

    /**
     * @param n node to be bound
     * @param multiplier factor by which node should be bound
     *
     * binds font size to window height
     */
    private void bindFont(Node n, double multiplier){
        n.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", inputPane.heightProperty().multiply(multiplier).asString()
        ));
    }
}