package com.example.go_gruppe1.controller;

import com.example.go_gruppe1.model.file.FileControl;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import java.io.IOException;

public class winnerMaskController {

    @FXML
    private GridPane pane;

    @FXML
    private Label name, totalPoints, trapped, extraPointsValue, byoyomi;
    @FXML
    private Button saveAs, newGame, exit;

    private int reasonForWinning = 0;

    private final FileControl fileControl = new FileControl();

    @FXML
    public void onSaveFileAsClick() {
        System.out.println("Saving file...");
        fileControl.saveFile();
    }
    protected void setReasonForWinning(int reason) {
        if (reason > 0 && reason <= 3) {
            reasonForWinning = reason;
        }
    }

    protected void initiateDisplay(String winner, long score, int trapped, double komi){
        setName(winner);
        setTrapped(trapped);
        setScore(score);
        if(komi == 0.0)
            extraPointsValue.setVisible(false);
        else
            setKomi(komi);
        byoyomi.setVisible(false);
        initiateButtons();
    }

    protected void initiateDisplay(String winner, String other, boolean hasResigned){
        setName(winner, other);
        totalPoints.setVisible(false);
        trapped.setVisible(false);
        extraPointsValue.setVisible(false);
        byoyomi.setVisible(false);
        initiateButtons();
    }

    /*
    protected void initiateDisplay(String winner, String other, long total, int trapped, String komiOrHandicaps,
                                   double komiOrHandicapValue, int byoyomi, int byoyomiNumber, int time) {
        setName(winner, other);
        setTotalPoints(total);
        setTrapped(trapped);
        setExtraPointsValue(komiOrHandicaps, komiOrHandicapValue);
        setByoyomi(byoyomi, byoyomiNumber, time);
        initiateButtons();
    }*/

    private void initiateButtons() {
        newGame.prefWidthProperty().bind(pane.heightProperty().multiply(0.2));
        newGame.styleProperty().bind(Bindings.concat(
                "-fx-text-fill: #483C32; ",
                "-fx-font-weight: bold; ",
                "-fx-font-size: ", pane.heightProperty().multiply(0.02).asString()
        ));
        newGame.toFront();

        saveAs.prefWidthProperty().bind(pane.heightProperty().multiply(0.2));
        saveAs.styleProperty().bind(Bindings.concat(
                "-fx-text-fill: #483C32; ",
                "-fx-font-weight: bold; ",
                "-fx-font-size: ", pane.heightProperty().multiply(0.02).asString()
        ));
        saveAs.toFront();

        exit.prefWidthProperty().bind(pane.heightProperty().multiply(0.2));
        exit.styleProperty().bind(Bindings.concat(
                "-fx-text-fill: #483C32; ",
                "-fx-font-weight: bold; ",
                "-fx-font-size: ", pane.heightProperty().multiply(0.02).asString()
        ));
        exit.toFront();
    }

    public void setName(String winner, String other) {
        if (reasonForWinning == 2) {
            name.setText(other + " resigned. " + winner + " won!");
        } else {
            name.setText(other + "'s time is up. " + winner + " won!");
        }

        name.setWrapText(true);
        name.setTextOverrun(OverrunStyle.CENTER_ELLIPSIS);
        GridPane.setVgrow(name, Priority.ALWAYS);

        name.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", pane.heightProperty().multiply(0.046).asString()
        ));
    }

    public void setName(String winner){
        if(winner.equals("Draw")) {
            name.setText("It's a draw!");
            totalPoints.setVisible(false);
            trapped.setVisible(false);
            extraPointsValue.setVisible(false);
            byoyomi.setVisible(false);
            initiateButtons();
        } else
            name.setText(winner + " won!");

        name.setWrapText(true);
        name.setTextOverrun(OverrunStyle.CENTER_ELLIPSIS);
        GridPane.setVgrow(name, Priority.ALWAYS);

        name.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", pane.heightProperty().multiply(0.046).asString()
        ));
    }

    private void setScore(long total) {
        totalPoints.setText("Total Points: " + total);
        totalPoints.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", pane.heightProperty().multiply(0.042).asString()
        ));
    }

    private void setTrapped(int trapped) {
        this.trapped.setText("Trapped: " + trapped);
        this.trapped.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", pane.heightProperty().multiply(0.035).asString()
        ));
    }

    private void setKomi(double value) {
        extraPointsValue.setText("Komi: " + value);
        extraPointsValue.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", pane.heightProperty().multiply(0.035).asString()
        ));
    }

    private void setByoyomi(int totalNumber, int number, int time) {
        if (totalNumber == 0) {
            byoyomi.setVisible(false);
        } else {
            byoyomi.setText(number + "/" + totalNumber + " time periods à " + time + " s left");
            byoyomi.toFront();
        }

        byoyomi.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", pane.heightProperty().multiply(0.035).asString()
        ));
    }

    public void onNewGameClick(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/inputMaskGUI.fxml"));
        Parent root = loader.load();

        inputMaskController inputMask = loader.getController();
        inputMask.setSize(getWidth(), getHeight());
        inputMask.initiateDisplay();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setMinWidth(630);
        stage.setMinHeight(500);
        stage.centerOnScreen();
        stage.show();
    }

    public void onExitGameClick() {
        Platform.exit();
    }

    protected void setSize(double width, double height) {
        pane.setPrefHeight(height);
        pane.setPrefWidth(width);
        pane.setMinSize(600, 580);
    }

    protected double getWidth() {
        return pane.getWidth();
    }

    protected double getHeight() {
        return pane.getHeight();
    }
}
