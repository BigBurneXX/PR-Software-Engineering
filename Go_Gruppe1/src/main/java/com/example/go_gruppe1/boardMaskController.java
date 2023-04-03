package com.example.go_gruppe1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;

public class boardMaskController {

    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private GridPane boardPaneText;

    @FXML
    private Pane boardPane;
    @FXML
    private Label pl1, pl2, komiBoard, handicapsBoard, blackTrapped, whiteTrapped;
    @FXML
    private ToggleButton play, navigate;
    @FXML
    private Button startButton;

    @FXML
    private Rectangle board;
    public void displayPlayerNames(String p1, String p2) {
        if(p1.isEmpty()) {
            if(!p2.equals("Player 1")); //names cannot be the same
            p1 = "Player 1";
        }
        if(p2.isEmpty()) {
            if(!p1.equals("Player 2")); //names cannot be the same
            p2 = "Player 2";
        }

        pl1.setText(p1 + " (Black)");
        pl2.setText(p2 + " (White)");
    }

    public void displayKomi(String komiAdvantage) {
        komiBoard.setText("Komi: 0");

        if(!komiAdvantage.isEmpty() && Integer.valueOf(komiAdvantage) > 0) {
            komiBoard.setText("Komi: " + komiAdvantage);
        }
    }

    public void displayHandicaps(String handicaps) {
        handicapsBoard.setText("Handicaps: 0");

        if(!handicaps.isEmpty() && Integer.valueOf(handicaps) > 0) {
            handicapsBoard.setText("Handicaps: " + handicaps);
        }
    }

    public void setSize(double width, double height) {
        boardPane.setPrefHeight(height);
        boardPane.setPrefWidth(width);
        boardPaneText.prefWidthProperty().bind(boardPane.widthProperty());
        boardPaneText.prefHeightProperty().bind(boardPane.heightProperty().multiply(0.25));
    }


    public boolean getMode(ActionEvent event) {
        if(play.isSelected()) {
            return true;
        } else {
            return false;
        }
    }

    public void displayBlackTrapped(String black) {
        blackTrapped.setText("Trapped: 0");

        if((!black.isEmpty() && Integer.valueOf(black)  > 0)) {
            blackTrapped.setText("Trapped: " + black);
        }
    }

    public void displayWhiteTrapped(String white) {
        whiteTrapped.setText("Trapped: 0");

        if((!white.isEmpty() && Integer.valueOf(white)  > 0)) {
            blackTrapped.setText("Trapped: " + white);
        }
    }

    public double getWidth() {
        return boardPane.getWidth();
    }

    public double getHeight() {
        return boardPane.getHeight();
    }

    public void switchToInputMask(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/inputMaskGUI.fxml"));
        root = loader.load();

        inputMaskController inputMask = loader.getController();
        inputMask.setSize(getWidth(), getHeight());

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    //does not fully work yet
    public void drawBoard(int size) {
        double boardSize = Math.min(boardPane.getPrefWidth(), boardPane.getPrefHeight()) * 0.4;
        double xOffset = (boardPane.getWidth() - boardSize);
        double yOffset = (boardPane.getHeight() - boardSize) / 2;

        //board.setX(xOffset);
        //board.setY(yOffset);
        board.setWidth(boardSize);
        board.setHeight(boardSize);

        board.widthProperty().bind(boardPane.widthProperty().multiply(0.4));
        board.heightProperty().bind(boardPane.widthProperty().multiply(0.4));
    }



























}
