package com.example.go_gruppe1;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class winnerMaskController {

    @FXML
    private BorderPane pane;

    @FXML
    private Label name, totalPoints, trapped, extraPoints, extraPointsValue, byoyomi, byoyomiLabel;

    @FXML
    private Pane left, right, center, bottom;

    private int reasonForWinning = 0;
    protected void setReasonForWinning(int reason) {
        if(reason > 0 && reason <= 3) {
            reasonForWinning = reason;
        }
    }

    protected void setName(String winner, String other) {
        if(reasonForWinning == 1) {
            name.setText(winner + " won!");
        } else if(reasonForWinning == 2) {
            name.setText(other  + " resigned. " + winner + " won!");
        } else {
            name.setText(other + "'s time is up. " + winner + " won!");
        }
    }

    protected void setTotalPoints(long total) {
        totalPoints.setText(String.valueOf(total));
    }

    protected void setTrapped(int trapped) {
        this.trapped.setText(String.valueOf(trapped));
    }

    protected void setExtraPoints(String komiOrHandicaps) {
        extraPoints.setText(komiOrHandicaps);
    }

    protected void setExtraPointsValue(double komiOrHandicaps) {
        extraPointsValue.setText(String.valueOf(komiOrHandicaps));
    }

    protected void setByoyomi(int totalNumber, int number, int time) {
        if(totalNumber == 0) {
            byoyomiLabel.setVisible(false);
            byoyomi.setVisible(false);
        } else {
            byoyomi.setText(number + "/" + totalNumber + " time periods à " + time + " s left");
            byoyomi.toFront();
        }
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
