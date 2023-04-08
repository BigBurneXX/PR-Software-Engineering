package com.example.go_gruppe1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;

public class boardMaskController {

    public ToggleGroup mode;

    @FXML
    private Label modeAndMoveDisplay, sampleSolutionDisplay;

    @FXML
    private BorderPane topRegion;

    @FXML
    private MenuItem fileSave, fileNewGame, fileLoadGame;

    @FXML
    private RadioMenuItem modePlay, modeNavigate;

    @FXML
    private BorderPane boardPane;

    @FXML
    private GridPane board;
    @FXML
    private Label pl1, pl2, komiBoard, handicapsBoard, blackTrapped, whiteTrapped;

    @FXML
    private VBox bottomRegion;

    @FXML
    private StackPane circlePane;

    @FXML
    private Pane leftRegion, rightRegion;

    private Color lastColor = Color.BLACK;

    private Polygon leftArrow, rightArrow;

    public void displayPlayerNames(String p1, String p2) {
        p1 = p1.isEmpty() ? "Player 1" : p1;
        p2 = p2.isEmpty() ? "Player 2" : p2;

        pl1.setText(p1 + " (Black)");
        pl2.setText(p2 + " (White)");
    }

    public void displayKomi(String komiAdvantage) {
        komiBoard.setText("Komi: " +
                (!komiAdvantage.isEmpty() && Integer.valueOf(komiAdvantage) > 0 ? komiAdvantage : "0")
        );
    }

    public void displayHandicaps(String handicaps) {
        handicapsBoard.setText("Handicaps: " +
                (!handicaps.isEmpty() && Integer.valueOf(handicaps) > 0 ? handicaps : "0")
        );
    }

    public void setSize(double width, double height) {
        boardPane.setPrefHeight(height);
        boardPane.setPrefWidth(width);
    }

    public boolean getMode(ActionEvent event) {
        return modePlay.isSelected();
    }

    public void onModeNavigateClick() {
        modeAndMoveDisplay.setText("Navigate mode activated");
        modeAndMoveDisplay.setFont(Font.font("Baskerville Old Face", FontWeight.BOLD, board.getHeight() * 0.10));
        modeAndMoveDisplay.prefHeightProperty().bind(bottomRegion.heightProperty().multiply(0.25));

        // create leftArrow
        leftArrow = new Polygon(
                0.0, 0.0,
                -30.0, 15.0,
                0.0, 30.0
        );
        leftArrow.setFill(Color.web("#483C32"));
        leftArrow.setStrokeWidth(1.5);

        // add leftArrow to leftRegion
        leftRegion.getChildren().add(leftArrow);
        leftArrow.translateXProperty().bind(leftRegion.widthProperty().divide(2));
        leftArrow.translateYProperty().bind(leftRegion.heightProperty().divide(2));

        // create rightArrow
        rightArrow = new Polygon(
                0.0, 0.0,
                30.0, 15.0,
                0.0, 30.0
        );
        rightArrow.setFill(Color.web("#483C32"));
        rightArrow.setStrokeWidth(1.5);

        // add leftArrow to leftRegion
        rightRegion.getChildren().add(rightArrow);
        rightArrow.translateXProperty().bind(rightRegion.widthProperty().divide(2));
        rightRegion.translateYProperty().bind(rightRegion.heightProperty().divide(2));

        leftArrow.setScaleX(2);
        leftArrow.setScaleY(2);

        rightArrow.setScaleX(2);
        rightArrow.setScaleY(2);
    }

    public void onModePlayClick() {
        
    }

    protected void playActivate(){
        modePlay.setSelected(true);
    }

    public void displayBlackTrapped(String black) {
        blackTrapped.setText("Trapped: " +
                (!black.isEmpty() && Integer.valueOf(black) > 0 ? black : "0")
        );
    }

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
        Parent root = loader.load();

        inputMaskController inputMask = loader.getController();
        inputMask.setSize(getWidth(), getHeight());

        Node source = topRegion.getTop();
        Stage stage = (Stage) source.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void drawBoard(int size) {
        //set padding, so stones are not covered by top region
        board.setPadding(new Insets(20, 0, 0, 0));

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
        for (int i = 0; i <= size; i++) {
            ColumnConstraints colConstraints = new ColumnConstraints();
            colConstraints.setPercentWidth(100.0 / size);
            board.getColumnConstraints().add(colConstraints);

            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPercentHeight(100.0 / size);
            board.getRowConstraints().add(rowConstraints);
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

            char[] alphabet = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S'};

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

        //add color to board
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                Pane cell = new Pane();
                cell.setStyle("-fx-background-color:  #C4A484; -fx-border-color: #483C32");
                board.add(cell, row, col);
            }
        }

        //add circles for stones
        for (int row = 0; row <= size; row++) {
            for (int col = 0; col <= size; col++) {
                Circle circle = new Circle(10, Color.TRANSPARENT);
                board.add(circle, row, col);

                //make stones resizable and adjust x and y properties
                circle.radiusProperty().bind(boardPane.heightProperty().multiply(0.6).divide(size).divide(4));
                circle.translateYProperty().bind(boardPane.heightProperty().multiply(0.6).divide(size * 2.4).multiply(-1));
                circle.translateXProperty().bind(boardPane.heightProperty().multiply(0.6).divide(size * 3.9).multiply(-1));


                //when the mouse is clicked the circle will be filled with a white or black colour depending on whose turn it is
                circle.setOnMouseClicked(e -> {
                    if(modePlay.isSelected()) {
                        if (circle.getFill() == Color.SNOW || circle.getFill().equals(Color.valueOf("#000001")))
                            setStone(circle);
                    }
                });

                //when the mouse is hovering over a transparent circle this circle is coloured white or black
                //side note: these colours are a little different from the white and black that a circle is filled with
                //           when clicked so that .equals will return false
                circle.setOnMouseEntered(e -> {
                    if(modePlay.isSelected()) {
                        if (circle.getFill() == Color.TRANSPARENT) {
                            if (lastColor == Color.BLACK)
                                circle.setFill(Color.valueOf("#000001"));
                            else
                                circle.setFill(Color.SNOW);
                        }
                    }
                });

                //when the mouse is no longer hovering over the circle the colour is removed
                circle.setOnMouseExited(e -> {
                    if(modePlay.isSelected()) {
                        if (circle.getFill() == Color.SNOW || circle.getFill().equals(Color.valueOf("#000001")))
                            circle.setFill(Color.TRANSPARENT);
                    }
                });
            }
        }


    }

    public void setStone(Circle c) {
        if(lastColor == Color.WHITE){
            c.setFill(lastColor);
            lastColor = Color.BLACK;
            modeAndMoveDisplay.setText("Black's turn!");
        } else {
            c.setFill(lastColor);
            lastColor = Color.WHITE;
            modeAndMoveDisplay.setText("White's turn!");
        }
    }

    /*public boolean isFirstStone() {
        for (Node node : board.getChildren()) {
            if (node instanceof Circle) {
                if(!((Circle) node).getFill().equals(Color.TRANSPARENT)) {
                    return false;
                }
            }
        }
        return true;
    }*/
}


