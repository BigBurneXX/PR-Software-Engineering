package com.example.go_gruppe1;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class boardMaskController {

    @FXML
    public ToggleGroup mode;

    @FXML
    private TextField renameFileName;

    @FXML
    private Label modeAndMoveDisplay, sampleSolutionDisplay;

    @FXML
    private BorderPane topRegion;

    @FXML
    private RadioMenuItem modePlay, modeNavigate;

    @FXML
    private BorderPane boardPane;

    @FXML
    private GridPane board, topGrid;
    @FXML
    private Label pl1, pl2, komiBoard, handicapsBoard, blackTrapped, whiteTrapped;

    @FXML
    private VBox bottomRegion;

    @FXML
    private StackPane circlePane;

    @FXML
    private Pane leftRegion, rightRegion;

    private Color lastColor = Color.BLACK;

    @FXML
    private Polygon leftArrow, rightArrow;

    @FXML
    private Button passButton, resignButton;

    private int boardSize;
    private String player1Name;
    private String player2Name;

    private int blackTrappedStones = 0;

    private int whiteTrappedStones = 0;

    private int handicaps;
    private double komi;

    private final char[] alphabet = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T'};

    private BoardLogicControl boardLogicControl;

    private final FileControl fileControl = new FileControl();

    @FXML
    public void onSaveFileClick() {
        //file functionality is disabled for now
        fileControl.saveFile();
    }

    @FXML
    public void onRenameFileClick() {
        //file functionality is disabled for now
        fileControl.renameFile(renameFileName.getText());
    }

    @FXML
    public void onLoadFileClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            //file functionality is disabled for now
            fileControl.loadFile(selectedFile);
        }
    }

    @FXML
    public void onModePlayClick() {
        leftArrow.setVisible(false);
        rightArrow.setVisible(false);
        passButton.setVisible(true);
        resignButton.setVisible(true);
        modeAndMoveDisplay.setFont(Font.font("Baskerville Old Face", FontWeight.BOLD, 15));
        modeAndMoveDisplay.prefHeightProperty().bind(bottomRegion.heightProperty().multiply(0.25));

        if (lastColor == Color.BLACK) {
            modeAndMoveDisplay.setText(pl1.getText() + "'s turn!");
        } else {
            modeAndMoveDisplay.setText(pl2.getText() + "'s turn!");
        }
    }

    @FXML
    public void onModeNavigateClick() {
        leftArrow.setVisible(true);
        rightArrow.setVisible(true);
        //TODO logic for arrow clicks
        /*rightArrow.setOnMouseClicked();
        leftArrow.setOnMouseClicked();
        */
        passButton.setVisible(false);
        resignButton.setVisible(false);
        modeAndMoveDisplay.setText("Navigate mode activated");
        modeAndMoveDisplay.setFont(Font.font("Baskerville Old Face", FontWeight.BOLD, board.getHeight() * 0.10));
        modeAndMoveDisplay.prefHeightProperty().bind(bottomRegion.heightProperty().multiply(0.25));
    }

    //first all ActionEventControllers then other controllers
    protected void setSampleSolutionDisplay(String text) {
        sampleSolutionDisplay.setText(text);
    }

    protected void setSize(double width, double height) {
        boardPane.setPrefHeight(height);
        boardPane.setPrefWidth(width);
        boardPane.setMinSize(600, 580);
    }

    protected double getWidth() {
        return boardPane.getWidth();
    }

    protected double getHeight() {
        return boardPane.getHeight();
    }

    public void switchToInputMask() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/inputMaskGUI.fxml"));
        Parent root = loader.load();

        inputMaskController inputMask = loader.getController();
        inputMask.setSize(getWidth(), getHeight());
        System.out.println(this.getWidth());
        System.out.println(this.getHeight());

        Node source = topRegion.getTop();
        Stage stage = (Stage) source.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setMinWidth(600);
        stage.setMinHeight(650);
        stage.show();
    }

    protected void switchToNewGame(String player1Name, String player2Name, double komi, Long handicaps, Long boardSize) throws IOException{
        /*load game*/
    }
    protected void initiateDisplay(String player1Name, String player2Name, String komi, String handicaps, int boardSize) {
        this.boardSize = boardSize;
        displayPlayerNames(player1Name, player2Name);
        displayKomi(komi);
        displayHandicaps(handicaps);
        displayTrappedStone(0, blackTrapped);
        displayTrappedStone(0, whiteTrapped);
        onModePlayClick();
        modePlay.setSelected(true);
        this.boardSize = boardSize;
        boardLogicControl = new BoardLogicControl(this, boardSize);
        drawBoard();
    }

    private void displayPlayerNames(String p1, String p2) {
        player1Name = p1.isEmpty() ? "Player 1" : p1;
        player2Name = p2.isEmpty() ? "Player 2" : p2;

        pl1.setText(player1Name + " (Black)");
        pl2.setText(player2Name + " (White)");
    }

    private void displayKomi(String komiAdvantage) {
        //only numeric values can be entered
        try {
            double d = Double.parseDouble(komiAdvantage);
            komi = d < 0 ? 0 : d;
            //only values greater than 0 are valid
            komiBoard.setText("Komi: " + komi);
        } catch (NumberFormatException nfe) {
            komiBoard.setText("Komi: 0");
            if (!komiAdvantage.isEmpty()) {
                sampleSolutionDisplay.setText(sampleSolutionDisplay.getText() + "\nInvalid komi input -> handicaps set to 0");
                System.out.println("Invalid komi input -> komi set to 0");
            }
        }
    }

    private void displayHandicaps(String handicaps) {
        //only numeric values can be entered
        try {
            this.handicaps = Integer.parseInt(handicaps);
            System.out.println(this.handicaps);
            //only values greater than 0 are valid
            if (boardSize == 9 || boardSize == 13) {
                if (this.handicaps < 0 || this.handicaps > 5) {
                    handicapsBoard.setText("Handicaps: " + "0");
                    this.handicaps = 0;
                } else {
                    handicapsBoard.setText("Handicaps: " + this.handicaps);
                }
            } else if (boardSize == 19) {
                if (this.handicaps < 0 || this.handicaps > 9) {
                    handicapsBoard.setText("Handicaps: " + "0");
                    this.handicaps = 0;
                } else {
                    handicapsBoard.setText("Handicaps: " + this.handicaps);
                }
            }
        } catch (NumberFormatException nfe) {
            handicapsBoard.setText("Handicaps: 0");
            if (!handicaps.isEmpty()) {
                sampleSolutionDisplay.setText(sampleSolutionDisplay.getText() + "\nInvalid handicap input -> handicaps set to 0");
                System.out.println("Invalid handicap input -> handicaps set to 0");
            }
        }
    }

    private void displayTrappedStone(int numberTrapped, Label trappedLabel) {
        if (numberTrapped >= 0) {
            trappedLabel.setText("Trapped: " + numberTrapped);
        } else
            System.out.println("Invalid trapped stones input -> number of trapped stones cannot be < 0");
    }

    //initially, play mode is displayed
    private void drawBoard() {
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
        for (int i = 0; i <= boardSize; i++) {
            ColumnConstraints colConstraints = new ColumnConstraints();
            colConstraints.setPercentWidth(100.0 / boardSize);
            board.getColumnConstraints().add(colConstraints);

            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPercentHeight(100.0 / boardSize);
            board.getRowConstraints().add(rowConstraints);
        }

        boardLabelling();

        //add color to board
        for (int col = 1; col < boardSize; col++) {
            for (int row = 1; row < boardSize; row++) {
                Pane cell = new Pane();
                cell.setStyle("-fx-background-color:  #C4A484; -fx-border-color: #483C32");
                board.add(cell, col, row);
            }
        }

        addStones();

        drawHandicaps();
        drawNavigationArrows();
        drawPassButton();
        drawResignButton();

        //creating output file
        //For now creating a file is deactivated, otherwise there would be too much files created while coding
        fileControl.createFile(this, "", player1Name, player2Name, boardSize, komi, handicaps);
    }

    private void boardLabelling() {
        //add color and labelling but without borders
        for (int i = 0; i <= boardSize; i++) {
            //top color
            Pane topLetterCell = new Pane();
            topLetterCell.setStyle("-fx-background-color:  #C4A484");
            board.add(topLetterCell, i, 0);

            //right color
            if (i != 0 && i != boardSize) {
                Pane rightCell = new Pane();
                rightCell.setStyle("-fx-background-color:  #C4A484");
                board.add(rightCell, boardSize, i);
            }

            //bottom color
            Pane bottomLetterCell = new Pane();
            bottomLetterCell.setStyle("-fx-background-color:  #C4A484");
            board.add(bottomLetterCell, i, boardSize);

            //right labelling
            if (i != 0) {
                Label rightNumberCell = new Label();
                rightNumberCell.setText(String.valueOf(i));
                rightNumberCell.setCenterShape(true);
                rightNumberCell.setFont(Font.font("Baskerville Old Face", FontWeight.BOLD, 13));
                rightNumberCell.setStyle("-fx-text-fill: #483C32; -fx-font-size: 15");
                board.setHalignment(rightNumberCell, HPos.RIGHT);
                board.setValignment(rightNumberCell, VPos.TOP);
                rightNumberCell.translateYProperty().bind(rightNumberCell.heightProperty().divide(2).multiply(-1));
                topLetterCell.toBack();
                bottomLetterCell.toBack();
                board.add(rightNumberCell, boardSize, i);
            }

            //left color
            if (i != 0 && i != boardSize) {
                Pane leftNumberCell = new Pane();
                leftNumberCell.setStyle("-fx-background-color:  #C4A484");
                board.add(leftNumberCell, 0, i);
            }

            if (i < boardSize) {
                Label letter = new Label();
                letter.setText(String.valueOf(alphabet[i]));
                letter.setCenterShape(true);
                letter.setFont(Font.font("Baskerville Old Face", FontWeight.BOLD, 13));
                letter.setStyle("-fx-text-fill: #C4A484");
                board.setHalignment(letter, HPos.RIGHT);
                board.setValignment(letter, VPos.BOTTOM);
                letter.setStyle("-fx-font-size: 15");
                letter.translateXProperty().bind(bottomLetterCell.widthProperty().multiply(-0.8));
                board.add(letter, i + 1, boardSize);
            }


        }

    }


    //add circles for stones
    private void addStones() {
        for (int row = 1; row <= boardSize; row++) {
            for (int col = 1; col <= boardSize; col++) {
                Circle circle = new Circle(10, Color.TRANSPARENT);
                board.add(circle, row, col);

                //make stones resizable and adjust x and y properties
                circle.radiusProperty().bind(boardPane.heightProperty().multiply(0.8).divide(boardSize).divide(4));
                circle.translateYProperty().bind(boardPane.heightProperty().multiply(0.6).divide(boardSize * 2.4).multiply(-1));
                circle.translateXProperty().bind(boardPane.heightProperty().multiply(0.8).divide(boardSize * 3.9).multiply(-1));

                //initial start ... needs additional logic if handicaps are used
                if (handicaps <= 0) {
                    modeAndMoveDisplay.setText(pl1.getText() + "'s turn!");
                } else {
                    modeAndMoveDisplay.setText(pl2.getText() + "'s turn!");
                    lastColor = Color.WHITE;
                }
                modeAndMoveDisplay.setFont(Font.font("Baskerville Old Face", FontWeight.BOLD, 15));

                //color for hovering
                Color hoverBlack = Color.valueOf("#00000070");
                Color hoverWhite = Color.valueOf("#FFFFFF70");
                //when the mouse is clicked the circle will be filled with a white or black colour depending on whose turn it is
                circle.setOnMouseClicked(e -> {
                    if (modePlay.isSelected()) {
                        if (circle.getFill() == hoverWhite || circle.getFill() == hoverBlack)
                            setStone(circle);
                    }
                });

                //when the mouse is hovering over a transparent circle this circle is coloured white or black
                //side note: these colours are a little different from the white and black that a circle is filled with
                //           when clicked so that .equals will return false
                circle.setOnMouseEntered(e -> {
                    if (modePlay.isSelected()) {
                        if (circle.getFill() == Color.TRANSPARENT) {
                            if (lastColor == Color.BLACK)
                                circle.setFill(hoverBlack);
                            else
                                circle.setFill(hoverWhite);
                        }
                    }
                });

                //when the mouse is no longer hovering over the circle the colour is removed
                circle.setOnMouseExited(e -> {
                    if (modePlay.isSelected()) {
                        if (circle.getFill() == hoverWhite || circle.getFill() == hoverBlack)
                            circle.setFill(Color.TRANSPARENT);
                    }
                });
            }
        }
    }

    private void drawHandicaps() {
        for (Node n : board.getChildren()) {
            if (n instanceof Circle) {
                int row = board.getRowIndex(n);
                int col = board.getColumnIndex(n);

                if(boardSize == 9) {
                    if (handicaps >= 1) {
                        if (row == 3 && col == 7) {
                            Circle c = (Circle) n;
                            c.setFill(Color.BLACK);
                        }
                    }

                    if (handicaps >= 2) {
                        if (row == 7 && col == 3) {
                            Circle c = (Circle) n;
                            c.setFill(Color.BLACK);
                        }
                    }

                    if (handicaps >= 3) {
                        if (row == 7 && col == 7) {
                            Circle c = (Circle) n;
                            c.setFill(Color.BLACK);
                        }
                    }

                    if (handicaps >= 4) {
                        if (row == 3 && col == 3) {
                            Circle c = (Circle) n;
                            c.setFill(Color.BLACK);
                        }
                    }

                    if (handicaps == 5) {
                        if (row == 5 && col == 5) {
                            Circle c = (Circle) n;
                            c.setFill(Color.BLACK);
                        }
                    }
                } else if(boardSize == 13) {
                    if (handicaps >= 1) {
                        if (row == 4 && col == 10) {
                            Circle c = (Circle) n;
                            c.setFill(Color.BLACK);
                        }
                    }

                    if (handicaps >= 2) {
                        if (row == 10 && col == 4) {
                            Circle c = (Circle) n;
                            c.setFill(Color.BLACK);
                        }
                    }

                    if (handicaps >= 3) {
                        if (row == 10 && col == 10) {
                            Circle c = (Circle) n;
                            c.setFill(Color.BLACK);
                        }
                    }

                    if (handicaps >= 4) {
                        if (row == 4 && col == 4) {
                            Circle c = (Circle) n;
                            c.setFill(Color.BLACK);
                        }
                    }

                    if (handicaps == 5) {
                        if (row == 7 && col == 7) {
                            Circle c = (Circle) n;
                            c.setFill(Color.BLACK);
                        }
                    }
                } else {
                    if (handicaps >= 1) {
                        if (row == 4 && col == 16) {
                            Circle c = (Circle) n;
                            c.setFill(Color.BLACK);
                        }
                    }

                    if (handicaps >= 2) {
                        if (row == 16 && col == 4) {
                            Circle c = (Circle) n;
                            c.setFill(Color.BLACK);
                        }
                    }

                    if (handicaps >= 3) {
                        if (row == 16 && col == 16) {
                            Circle c = (Circle) n;
                            c.setFill(Color.BLACK);
                        }
                    }

                    if (handicaps >= 4) {
                        if (row == 4 && col == 4) {
                            Circle c = (Circle) n;
                            c.setFill(Color.BLACK);
                        }
                    }

                    if (handicaps >= 5) {
                        if (row == 10 && col == 10) {
                            Circle c = (Circle) n;
                            c.setFill(Color.BLACK);
                        }
                    }

                    if (handicaps >= 6) {
                        if (row == 10 && col == 4) {
                            Circle c = (Circle) n;
                            c.setFill(Color.BLACK);
                        }
                    }

                    if (handicaps >= 7) {
                        if (row == 10 && col == 16) {
                            Circle c = (Circle) n;
                            c.setFill(Color.BLACK);
                        }
                    }

                    if (handicaps >= 8) {
                        if (row == 4 && col == 10) {
                            Circle c = (Circle) n;
                            c.setFill(Color.BLACK);
                        }
                    }

                    if (handicaps == 9) {
                        if (row == 16 && col == 10) {
                            Circle c = (Circle) n;
                            c.setFill(Color.BLACK);
                        }
                    }
                }



            }
        }


        modeAndMoveDisplay.setFont(Font.font("Baskerville Old Face", FontWeight.BOLD, 15));
    }

    //should be improved (code for left and right are very similar)
    private void drawNavigationArrows() {
        leftArrow.translateXProperty().bind(leftRegion.widthProperty().divide(2));
        leftArrow.translateYProperty().bind(leftRegion.heightProperty().divide(2));

        rightArrow.translateXProperty().bind(rightRegion.widthProperty().divide(2));
        rightRegion.translateYProperty().bind(rightRegion.heightProperty().divide(2));
    }

    private void drawPassButton() {
        passButton.setFont(Font.font("Baskerville Old Face", FontWeight.BOLD, 13));
        passButton.prefWidthProperty().bind(boardPane.widthProperty().multiply(0.08));

        passButton.setOnMouseEntered(e -> passButton.setStyle("-fx-background-color: #C4A484; -fx-border-color: #483C32"));
        passButton.setOnMouseExited(e -> passButton.setStyle("-fx-background-color: transparent; -fx-border-color: #483C32"));

        //pass logic
        passButton.setOnMouseClicked(e -> {
            if (lastColor == Color.BLACK) {
                modeAndMoveDisplay.setText(pl1.getText() + " passed! - " + pl2.getText() + "'s turn");
                lastColor = Color.WHITE;
            } else {
                modeAndMoveDisplay.setText(pl2.getText() + " passed! - " + pl1.getText() + "'s turn");
                lastColor = Color.BLACK;
            }
            //file functionality is disabled for now
            fileControl.writeAction("passed");
        });
    }

    private void drawResignButton() {
        resignButton.setFont(Font.font("Baskerville Old Face", FontWeight.BOLD, 13));
        resignButton.prefWidthProperty().bind(boardPane.widthProperty().multiply(0.08));
        resignButton.setOnMouseEntered(e -> resignButton.setStyle("-fx-background-color: #C4A484; -fx-border-color: #483C32"));
        resignButton.setOnMouseExited(e -> resignButton.setStyle("-fx-background-color: transparent; -fx-border-color: #483C32"));

        //resign logic
        resignButton.setOnMouseClicked(e -> {
            if (lastColor == Color.BLACK) {
                modeAndMoveDisplay.setText(pl1.getText() + " resigned! - " + pl2.getText() + " won!");
            } else {
                modeAndMoveDisplay.setText(pl2.getText() + " resigned! - " + pl1.getText() + " won!");
            }
            //file functionality is disabled for now
            fileControl.writeAction("resigned");
        });
    }

    public void setStone(Circle c) {
        modeAndMoveDisplay.setFont(Font.font("Baskerville Old Face", FontWeight.BOLD, 15));
        int row = -1;
        int col = -1;
        for (Node n : board.getChildren()) {
            if (n instanceof Circle && n.equals(c)) {
                row = board.getRowIndex(n);
                col = board.getColumnIndex(n);
                String stonePosition = String.valueOf(row-1) + String.valueOf(alphabet[col-1]);
                //boardLogicControl.setStoneToList(lastColor, row-1, col-1);
                //boardLogicControl.setStone(lastColor, row, col);
                fileControl.writeMoves(stonePosition);
                System.out.println();
                System.out.println(" " + (row) + alphabet[col - 1]);
            }
        }

        c.setFill(lastColor);
        boardLogicControl.setStoneToList(lastColor, row - 1, col - 1);
        if (lastColor == Color.WHITE) {
            lastColor = Color.BLACK;
            modeAndMoveDisplay.setText(pl1.getText() + "'s turn!");
        } else {
            lastColor = Color.WHITE;
            modeAndMoveDisplay.setText(pl2.getText() + "'s turn!");
        }
    }

    protected void deleteStoneGroup(StoneGroup toDelete) {
        if (toDelete.getColour() == Color.WHITE) {
            blackTrappedStones += toDelete.getPosition().size();
            displayTrappedStone(blackTrappedStones, blackTrapped);
        }else {
            whiteTrappedStones += toDelete.getPosition().size();
            displayTrappedStone(whiteTrappedStones, whiteTrapped);
        }

        //finds the circle for every position of stoneGroup toDelete and sets the visibility to TRANSPARENT
        for (Position p : toDelete.getPosition()) {
            //System.out.println("to delete: " + (p.row()+1) + alphabet[p.col()]);
            for (Node n : board.getChildren())
                if (n instanceof Circle c && board.getRowIndex(n) == (p.row() + 1) && board.getColumnIndex(n) == (p.col() + 1)) {
                    c.setFill(Color.TRANSPARENT);
                }
        }
    }
}


