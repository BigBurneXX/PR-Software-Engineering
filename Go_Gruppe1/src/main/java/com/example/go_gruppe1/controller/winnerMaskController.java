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

    /**
     * @param winner player that has won
     * @param trapped trapped stones by winner
     * @param komiValue advantage for white
     *
     * initiates all labels in the case that the end of the game has been reached due to consecutive passing
     */
    protected void initiateDisplay(String winner, long total, int trapped, double komiValue){
        declareWinner(winner + " won!");
        setScore(total);
        setTerritory(total - trapped);
        setTrapped(trapped);
        if(komiValue == 0.0)
            komi.setVisible(false);
        else
            setKomi(komiValue);
        initiateButtons();
    }

    /**
     * @param winner player that has won
     * @param other opponent player
     * @param hasResigned reason for winning
     *
     * initiates mask without labels
     */
    protected void initiateDisplay(String winner, String other, boolean hasResigned){
        setName(winner, other, hasResigned);
        makeButtonsInvisible();
        initiateButtons();
    }

    /**
     * initiates display in case of a draw (consecutive passing and same points)
     */
    protected void initiateDisplay(){
        makeButtonsInvisible();
        declareWinner("It's a draw");
    }

    /**
     * sets all labels but names invisible
     */
    private void makeButtonsInvisible(){
        totalPoints.setVisible(false);
        territory.setVisible(false);
        trapped.setVisible(false);
        komi.setVisible(false);
    }

    /**
     * initiates buttons
     */
    private void initiateButtons() {
        initiateButton(newGame);
        initiateButton(saveAs);
        initiateButton(exit);
    }

    /**
     * @param button button to be initiated
     *
     * initiates button and binds size
     */
    private void initiateButton(Button button){
        button.prefWidthProperty().bind(pane.heightProperty().multiply(0.2));
        button.styleProperty().bind(Bindings.concat(
                "-fx-text-fill: #483C32; ",
                "-fx-font-weight: bold; ",
                "-fx-font-size: ", pane.heightProperty().multiply(0.02).asString()
        ));
        button.toFront();
    }

    /**
     * @param winner player that has won
     * @param other opponent player
     * @param hasResigned reason for winning
     *
     * initiates player name labels (resigned if hasResigned true, byoyomi winning if hasResigned false)
     */
    private void setName(String winner, String other, boolean hasResigned) {
        String cause = hasResigned ? " resigned" : "'s time is up";
        declareWinner(other + cause + ". " + winner + " won!");
    }

    /**
     * @param declaration winner and reason
     *
     * binds name label size
     */
    private void declareWinner(String declaration){
        name.setText(declaration);
        name.setWrapText(true);
        name.setTextOverrun(OverrunStyle.CENTER_ELLIPSIS);
        GridPane.setVgrow(name, Priority.ALWAYS);
        bindFont(name, 0.046);
    }

    /**
     * @param total score
     *
     * set points label and binds label size
     */
    private void setScore(long total) {
        totalPoints.setText("Total Points: " + total);
        bindFont(totalPoints, 0.042);
    }

    /**
     * @param territoryValue captured score
     *
     * set captured territory label and binds label size
     */
    private void setTerritory(long territoryValue){
        territory.setText("Territory: " + territoryValue);
        bindFont(territory, 0.035);
    }

    /**
     * @param trapped # of trapped stones
     *
     * set trapped stones label and binds label size
     */
    private void setTrapped(int trapped) {
        this.trapped.setText("Trapped: " + trapped);
        bindFont(this.trapped, 0.035);
    }

    /**
     * @param value komi advantage
     *
     * set komi label and binds label size
     */
    private void setKomi(double value) {
        komi.setText("Komi: " + value);
        bindFont(komi, 0.035);
    }

    /**
     * button logic for saving game file
     */
    @FXML
    public void onSaveFileAsClick() {
        System.out.println("Saving file...");
        fileControl.saveFile();
    }

    /**
     * @param event action event
     * @throws IOException
     *
     * button logic for starting new game
     */
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

    /**
     * button logic for exit button
     */
    @FXML
    public void onExitGameClick() {
        Platform.exit();
    }

    /**
     * @param width width of window
     * @param height height of window
     *
     * sets window size
     */
    protected void setSize(double width, double height) {
        pane.setPrefHeight(height);
        pane.setPrefWidth(width);
        pane.setMinSize(600, 580);
    }

    /**
     * @return width of window
     */
    double getWidth() {
        return pane.getWidth();
    }

    /**
     * @return height of window
     */
    double getHeight() {
        return pane.getHeight();
    }

    /**
     * @param n node to be bound
     * @param multiplier defines how large node should be bound
     *
     * binds nodes to window height for resizing
     */
    private void bindFont(Node n, double multiplier){
        n.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", pane.heightProperty().multiply(multiplier).asString()
        ));
    }
}