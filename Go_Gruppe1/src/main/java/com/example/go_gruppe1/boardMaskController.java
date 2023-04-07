package com.example.go_gruppe1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;

public class boardMaskController {

    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private GridPane boardPaneText;

    @FXML
    private BorderPane topRegion;

    @FXML
    private MenuItem fileSave, fileNewGame, fileLoadGame;

    @FXML
    private RadioMenuItem modePlay, modeNavigate;

    @FXML
    private BorderPane boardPane;

    @FXML
    private GridPane board, rightRegion;
    @FXML
    private Label pl1, pl2, komiBoard, handicapsBoard, blackTrapped, whiteTrapped;

    @FXML
    private Region leftRegion, bottomRegion;

    @FXML
    private StackPane circlePane;

    public void displayPlayerNames(String p1, String p2) {
        if (p1.isEmpty()) {
            if (!p2.equals("Player 1")) ; //names cannot be the same
            p1 = "Player 1";
        }
        if (p2.isEmpty()) {
            if (!p1.equals("Player 2")) ; //names cannot be the same
            p2 = "Player 2";
        }

        pl1.setText(p1 + " (Black)");
        pl2.setText(p2 + " (White)");
    }

    //slightly changing the logic
    public void displayKomi(String komiAdvantage) {
        komiBoard.setText("Komi: " +
                (!komiAdvantage.isEmpty() && Integer.valueOf(komiAdvantage) > 0 ? komiAdvantage : "0")
        );
    }

    //slightly changing the logic
    public void displayHandicaps(String handicaps) {
        handicapsBoard.setText("Handicaps: " +
                (!handicaps.isEmpty() && Integer.valueOf(handicaps) > 0 ? handicaps : "0")
        );
    }

    public void setSize(double width, double height) {
        boardPane.setPrefHeight(height);
        boardPane.setPrefWidth(width);
    }

    //slightly changing the logic
    public boolean getMode(ActionEvent event) {
        return modePlay.isSelected();
    }

    //slightly changing the logic
    public void displayBlackTrapped(String black) {
        blackTrapped.setText("Trapped: " +
                (!black.isEmpty() && Integer.valueOf(black) > 0 ? black : "0")
        );
    }

    //slightly changing the logic
    public void displayWhiteTrapped(String white) {
        whiteTrapped.setText("Trapped: " +
                (!white.isEmpty() && Integer.valueOf(white) > 0 ? white : "0")
        );
    }

    public double getWidth() {
        return boardPane.getWidth();
    }

    public double getHeight() {
        return boardPane.getHeight();
    }

    public void switchToInputMask() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/inputMaskGUI.fxml"));
        root = loader.load();

        inputMaskController inputMask = loader.getController();
        inputMask.setSize(getWidth(), getHeight());

        Node source = topRegion.getTop();
        stage = (Stage) source.getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void drawBoard(int size) {
        //bind bottom and upper region to 25% of window width
        bottomRegion.prefHeightProperty().bind(boardPane.heightProperty().multiply(0.2));
        topRegion.prefHeightProperty().bind(boardPane.heightProperty().multiply(0.2));

        //set left, right and center (board) region to 50% of window width
        leftRegion.prefHeightProperty().bind(boardPane.heightProperty().multiply(0.6));
        rightRegion.prefHeightProperty().bind(boardPane.heightProperty().multiply(0.6));
        board.prefHeightProperty().bind(boardPane.heightProperty().multiply(0.6));

        //bind center width to 50% of window width, so it's a square
        board.prefWidthProperty().bind(boardPane.heightProperty().multiply(0.6));

        //bind left and right width to remaining width of the window of what's left from taking 50% of the height
        leftRegion.prefWidthProperty().bind(boardPane.widthProperty().subtract(board.prefWidthProperty()).divide(2));
        rightRegion.prefWidthProperty().bind(boardPane.widthProperty().subtract(board.prefWidthProperty()).divide(2));

        //create grid
        board.getColumnConstraints().clear();
        board.getRowConstraints().clear();
        rightRegion.getColumnConstraints().clear();
        rightRegion.getRowConstraints().clear();
        for (int i = 0; i <= size; i++) {
            ColumnConstraints colConstraints = new ColumnConstraints();
            colConstraints.setPercentWidth(100.0 / size);
            board.getColumnConstraints().add(colConstraints);
            if (i < 5) {
                rightRegion.getColumnConstraints().add(colConstraints);
            }

            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPercentHeight(100.0 / size);
            board.getRowConstraints().add(rowConstraints);
            rightRegion.getRowConstraints().add(rowConstraints);
        }

        //add color to board
        for(int row = 0; row < size; row ++) {
            for(int col = 0; col < size; col++) {
                Pane cell = new Pane();
                cell.setStyle("-fx-background-color:  #C4A484; -fx-border-color: #483C32");
                board.add(cell, row, col);
            }
        }

        //add board labelling
        for (int row = 0; row < size; row++) {
            //numbers on the right
            Pane numberCell = new Pane();
            numberCell.setStyle("-fx-background-color:  #F2F3F5; -fx-border-color: transparent");
            board.add(numberCell, size, row);

            Label number = new Label(String.valueOf(row + 1));
            number.setCenterShape(true);
            number.setFont(Font.font("Baskerville Old Face", FontWeight.BOLD, 13));
            number.setStyle("-fx-text-fill: #C4A484");
            board.setHalignment(number, HPos.RIGHT);
            board.setValignment(number, VPos.CENTER);
            number.setStyle("-fx-font-size: 15");
            board.add(number, size, row);

            //letters on the bottom
            Pane letterCell = new Pane();
            letterCell.setStyle("-fx-background-color:  #F2F3F5; -fx-border-color: transparent");
            board.add(letterCell, row, size);

            char[] alphabet = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S'};

            Label letter = new Label();
            letter.setText(String.valueOf(alphabet[row]));
            letter.setCenterShape(true);
            letter.setFont(Font.font("Baskerville Old Face", FontWeight.BOLD, 13));
            letter.setStyle("-fx-text-fill: #C4A484");
            board.setHalignment(letter, HPos.CENTER);
            board.setValignment(letter, VPos.BOTTOM);
            letter.setStyle("-fx-font-size: 15");
            board.add(letter, row, size);
        }

        //get rid of color of remaining bottom right cell
        Pane lastCell = new Pane();
        lastCell.setStyle("-fx-background-color:  #F2F3F5; -fx-border-color: transparent");
        board.add(lastCell, size, size);


        for(int row = 0; row <= size; row++) {
            for(int col = 0; col <= size; col++) {
                Circle circle = new Circle(10, Color.BLACK);

                board.add(circle, row, col);

                circle.setTranslateX(-8);
                circle.setTranslateY(-15);
            }
        }


    }


}