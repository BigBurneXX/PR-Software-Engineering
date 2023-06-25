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
    GridPane pane;

    @FXML
    private Label name, totalPoints, trapped, komi, territory;
    @FXML
    private Button saveAs, newGame, exit;

    private final FileControl fileControl = new FileControl();

    protected void initiateDisplay(String winner, long territory, int trapped, double komiValue){
        declareWinner(winner + " won!");
        setScore(territory, trapped);
        setTerritory(territory);
        setTrapped(trapped);
        if(komiValue == 0.0)
            komi.setVisible(false);
        else
            setKomi(komiValue);
        initiateButtons();
    }

    protected void initiateDisplay(String winner, String other, boolean hasResigned){
        setName(winner, other, hasResigned);
        makeButtonsInvisible();
        initiateButtons();
    }

    protected void initiateDisplay(){
        makeButtonsInvisible();
        declareWinner("It's a draw");
    }

    private void makeButtonsInvisible(){
        totalPoints.setVisible(false);
        territory.setVisible(false);
        trapped.setVisible(false);
        komi.setVisible(false);
    }

    private void initiateButtons() {
        initiateButton(newGame);
        initiateButton(saveAs);
        initiateButton(exit);
    }

    private void initiateButton(Button button){
        button.prefWidthProperty().bind(pane.heightProperty().multiply(0.2));
        button.styleProperty().bind(Bindings.concat(
                "-fx-text-fill: #483C32; ",
                "-fx-font-weight: bold; ",
                "-fx-font-size: ", pane.heightProperty().multiply(0.02).asString()
        ));
        button.toFront();
    }

    private void setName(String winner, String other, boolean hasResigned) {
        String cause = hasResigned ? " resigned" : "'s time is up";
        declareWinner(other + cause + ". " + winner + " won!");
    }

    private void declareWinner(String declaration){
        name.setText(declaration);
        name.setWrapText(true);
        name.setTextOverrun(OverrunStyle.CENTER_ELLIPSIS);
        GridPane.setVgrow(name, Priority.ALWAYS);
        bindFont(name, 0.046);
    }

    private void setScore(long total, int trapped) {
        int score = (int) total + trapped;
        totalPoints.setText("Total Points: " + score);
        bindFont(totalPoints, 0.042);
    }

    private void setTerritory(long territoryValue){
        territory.setText("Territory: " + territoryValue);
        bindFont(territory, 0.035);
    }

    private void setTrapped(int trapped) {
        this.trapped.setText("Trapped: " + trapped);
        bindFont(this.trapped, 0.035);
    }

    private void setKomi(double value) {
        komi.setText("Komi: " + value);
        bindFont(komi, 0.035);
    }

    @FXML
    public void onSaveFileAsClick() {
        System.out.println("Saving file...");
        fileControl.saveFile();
    }

    @FXML
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

    @FXML
    public void onExitGameClick() {
        Platform.exit();
    }

    protected void setSize(double width, double height) {
        pane.setPrefHeight(height);
        pane.setPrefWidth(width);
        pane.setMinSize(600, 580);
    }

    double getWidth() {
        return pane.getWidth();
    }

    double getHeight() {
        return pane.getHeight();
    }

    private void bindFont(Node n, double multiplier){
        n.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", pane.heightProperty().multiply(multiplier).asString()
        ));
    }
}
