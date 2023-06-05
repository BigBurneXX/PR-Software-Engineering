package com.example.go_gruppe1.controller;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
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
    private ButtonBar buttonBar;

    private Button saveAs, newGame, exit;

    private int reasonForWinning = 0;

    protected void setReasonForWinning(int reason) {
        if (reason > 0 && reason <= 3) {
            reasonForWinning = reason;
        }
    }

    protected void initiateDisplay(String winner, String other, long total, int trapped, String komiOrHandicaps,
                                   double komiOrHandicap, int byoyomi, int boyoyomiNumber, int time) {
        setName(winner, other);
        setTotalPoints(total);
        setTrapped(trapped);
        setExtraPoints(komiOrHandicaps);
        setExtraPointsValue(komiOrHandicap);
        setByoyomi(byoyomi, boyoyomiNumber, time);
    }

    private void initateButtons() {

    }

    private void setName(String winner, String other) {
        if (reasonForWinning == 1) {
            name.setText(winner + " won!");
        } else if (reasonForWinning == 2) {
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

    private void setTotalPoints(long total) {
        totalPoints.setText("Total Points: " + total);
        totalPoints.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", pane.heightProperty().multiply(0.03).asString()
        ));
    }

    private void setTrapped(int trapped) {
        this.trapped.setText("Trapped: " + trapped);
        this.trapped.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", pane.heightProperty().multiply(0.025).asString()
        ));
    }

    private void setExtraPoints(String komiOrHandicaps) {
        extraPointsValue.setText(komiOrHandicaps + " ");
    }

    private void setExtraPointsValue(double komiOrHandicaps) {
        String komiOrHandicap = extraPointsValue.getText();
        extraPointsValue.setText(komiOrHandicap + komiOrHandicaps);
        extraPointsValue.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", pane.heightProperty().multiply(0.025).asString()
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
                "-fx-font-size: ", pane.heightProperty().multiply(0.025).asString()
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
        pane.setMinSize(300, 300);
    }

    protected double getWidth() {
        return pane.getWidth();
    }

    protected double getHeight() {
        return pane.getHeight();
    }
}
