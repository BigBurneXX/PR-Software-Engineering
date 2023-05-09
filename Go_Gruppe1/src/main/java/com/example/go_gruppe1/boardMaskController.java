package com.example.go_gruppe1;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
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
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static javafx.scene.paint.Color.*;

public class boardMaskController {
    /*
      ================================================================================================================

                                            GUI component declaration

      ================================================================================================================
     */

    @FXML
    private GridPane board;

    /*
      ----------------------------------------------------------------------------------------------------------------
                                            upper region - GUI component declaration
      ----------------------------------------------------------------------------------------------------------------
     */

    @FXML
    private BorderPane topRegion;
    @FXML
    public ToggleGroup mode;
    @FXML
    private Label pl1, pl2, komiBoard, handicapsBoard, blackTrapped, whiteTrapped, timerBlack, timerWhite;
    @FXML
    private Button passButton, resignButton;
    @FXML
    private RadioMenuItem modePlay, modeNavigate;

    /*
      ----------------------------------------------------------------------------------------------------------------
                                        left and right region - GUI component declaration
      ----------------------------------------------------------------------------------------------------------------
     */

    @FXML
    private Pane leftRegion, rightRegion;
    @FXML
    private Polygon leftArrow, rightArrow;

    /*
      ----------------------------------------------------------------------------------------------------------------
                                            center region - GUI component declaration
      ----------------------------------------------------------------------------------------------------------------
     */

    @FXML
    private BorderPane boardPane;
    @FXML
    private StackPane circlePane;

    /*
      ----------------------------------------------------------------------------------------------------------------
                                            bottom region - GUI component declaration
      ----------------------------------------------------------------------------------------------------------------
     */

    @FXML
    private VBox bottomRegion;
    @FXML
    private Label modeAndMoveDisplay, sampleSolutionDisplay;

    /*
      ================================================================================================================

                                            logic component declaration

      ================================================================================================================
     */

    /*
      ----------------------------------------------------------------------------------------------------------------
                                        controllers
      ----------------------------------------------------------------------------------------------------------------
     */
    private BoardLogicControl boardLogicControl;
    private final FileControl fileControl = new FileControl();

    /*
      ----------------------------------------------------------------------------------------------------------------
                                        constants
      ----------------------------------------------------------------------------------------------------------------
     */
    private final char[] ALPHABET = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T'};
    private int BOARD_SIZE;
    private String PLAYER1NAME;
    private String PLAYER2NAME;
    private int HANDICAPS;
    private double KOMI;
    private long START_TIME;

    /*
      ----------------------------------------------------------------------------------------------------------------
                                        global variables
      ----------------------------------------------------------------------------------------------------------------
     */
    private Circle[][] circlesOfBoard;
    private Color lastColor = BLACK;
    private int blackTrappedStones = 0;
    private int whiteTrappedStones = 0;




    /*
      ================================================================================================================

                                            file onActionMethods

      ================================================================================================================
     */

    public void onNewGameClick() throws IOException {
        terminalInfo("Starting new game... \n[log end]");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/inputMaskGUI.fxml"));
        Parent root = loader.load();

        inputMaskController inputMask = loader.getController();
        inputMask.setSize(getWidth(), getHeight());

        Node source = topRegion.getTop();
        Stage stage = (Stage) source.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setMinWidth(600);
        stage.setMinHeight(650);
        stage.show();
    }

    @FXML
    public void onSaveFileAsClick() {
        terminalInfo("Saving file...");
        fileControl.saveFile();
    }

    @FXML
    public void onOpenFileClick() {
        terminalInfo("Loading file... \n[log end]");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            //file functionality is disabled for now
            fileControl.loadFile(selectedFile);
        }
    }
    protected void switchToNewGame(String player1Name, String player2Name, double komi, Long handicaps, Long boardSize) throws IOException{
        /*load game*/
    }


    public void onExitGameClick() {
        terminalInfo("Exiting file... \n[log end]");
        Platform.exit();
    }

    /*
      ================================================================================================================

                                            mode onActionMethods

      ================================================================================================================
     */

    @FXML
    public void onModePlayClick() {
        terminalInfo("play mode activated!");
        leftArrow.setVisible(false);
        rightArrow.setVisible(false);
        passButton.setVisible(true);
        resignButton.setVisible(true);
        modeAndMoveDisplay.setFont(Font.font("Baskerville Old Face", FontWeight.BOLD, 15));
        modeAndMoveDisplay.prefHeightProperty().bind(bottomRegion.heightProperty().multiply(0.25));

        modeAndMoveDisplay.setText((lastColor == BLACK ? pl1.getText() : pl2.getText()) + "'s turn!");
    }

    @FXML
    public void onModeNavigateClick() {
        terminalInfo("navigation mode activated!");
        leftArrow.setVisible(true);
        rightArrow.setVisible(true);
        //TODO logic for arrow clicks
        /*rightArrow.setOnMouseClicked();
          leftArrow.setOnMouseClicked();*/

        passButton.setVisible(false);
        resignButton.setVisible(false);
        modeAndMoveDisplay.setText("Navigation mode activated");
        modeAndMoveDisplay.setFont(Font.font("Baskerville Old Face", FontWeight.BOLD, board.getHeight() * 0.10));
        modeAndMoveDisplay.prefHeightProperty().bind(bottomRegion.heightProperty().multiply(0.25));
    }

    /*
      ================================================================================================================

                                            getter and setter

      ================================================================================================================
     */

    protected void setSampleSolutionDisplay(String text) {
        sampleSolutionDisplay.setText(text);
        terminalInfo("sample solution display set to" + text);
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


    /*
      ================================================================================================================

                                            initialize methods

      ================================================================================================================
     */
    protected void initiateDisplay(String player1Name, String player2Name, String komi, String handicaps, int boardSize) {
        BOARD_SIZE = boardSize;
        terminalInfo("starting a new game\nboard size set to: " + boardSize);
        boardLogicControl = new BoardLogicControl(this, boardSize);
        displayPlayerNames(player1Name, player2Name);
        displayKomi(komi);
        displayHandicaps(handicaps);
        displayTrappedStone(0, blackTrapped);
        displayTrappedStone(0, whiteTrapped);
        onModePlayClick();
        modePlay.setSelected(true);
        circlesOfBoard = new Circle[BOARD_SIZE+1][BOARD_SIZE+1];
        drawBoard();
    }

    private void displayPlayerNames(String p1, String p2) {
        PLAYER1NAME = p1.isEmpty() ? "Player 1" : p1;
        PLAYER2NAME = p2.isEmpty() ? "Player 2" : p2;

        pl1.setText(PLAYER1NAME + " (Black)");
        pl2.setText(PLAYER2NAME + " (White)");
        terminalInfo("player1 set to: " + PLAYER1NAME + "\nplayer2 set to: " + PLAYER2NAME);
    }

    private void displayKomi(String komiAdvantage) {
        //only numeric values can be entered
        try {
            double d = Double.parseDouble(komiAdvantage);
            //only values greater than 0 are valid
            KOMI = d < 0 ? 0 : d;
            komiBoard.setText("Komi: " + KOMI);
            terminalInfo("komi: " + KOMI);
        } catch (NumberFormatException nfe) {
            KOMI = 0;
            komiBoard.setText("Komi: " + KOMI);
            if (!komiAdvantage.isEmpty()) {
                sampleSolutionDisplay.setText(sampleSolutionDisplay.getText() + "\nInvalid komi input -> komi set to 0");
                terminalInfo("invalid komi input");
            }
            terminalInfo("komi set to: " + KOMI);
        }
    }

    private void displayHandicaps(String handicaps) {
        //only numeric values can be entered
        try {
            HANDICAPS = Integer.parseInt(handicaps);
            //only values greater than 0 are valid
            if (BOARD_SIZE == 9 || BOARD_SIZE == 13) {
                if (HANDICAPS < 0 || HANDICAPS > 5) {
                    handicapsBoard.setText("Handicaps: " + "0");
                    HANDICAPS = 0;
                } else {
                    handicapsBoard.setText("Handicaps: " + HANDICAPS);
                }
            } else if (BOARD_SIZE == 19) {
                if (HANDICAPS < 0 || HANDICAPS > 9) {
                    handicapsBoard.setText("Handicaps: " + "0");
                    HANDICAPS = 0;
                } else {
                    handicapsBoard.setText("Handicaps: " + HANDICAPS);
                }
            }
            terminalInfo("handicaps: " + HANDICAPS);
        } catch (NumberFormatException nfe) {
            HANDICAPS = 0;
            handicapsBoard.setText("Handicaps: " + HANDICAPS);
            if (!handicaps.isEmpty()) {
                sampleSolutionDisplay.setText(sampleSolutionDisplay.getText() + "\nInvalid handicap input -> handicaps set to 0");
                terminalInfo("invalid handicap input");
            }
            terminalInfo("handicaps set to: " + HANDICAPS);
        }
    }

    private void displayTrappedStone(int numberTrapped, Label trappedLabel) {
            trappedLabel.setText("Trapped: " + numberTrapped);
            terminalInfo(trappedLabel.getId() + " set to " + numberTrapped);
    }


    /*
      ----------------------------------------------------------------------------------------------------------------
                                        draw board and all elements in it
      ----------------------------------------------------------------------------------------------------------------
     */

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
        for (int i = 0; i <= BOARD_SIZE; i++) {
            ColumnConstraints colConstraints = new ColumnConstraints();
            colConstraints.setPercentWidth(100.0 / BOARD_SIZE);
            board.getColumnConstraints().add(colConstraints);

            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPercentHeight(100.0 / BOARD_SIZE);
            board.getRowConstraints().add(rowConstraints);
        }

        boardLabelling();

        //add color to board
        for (int col = 1; col < BOARD_SIZE; col++) {
            for (int row = 1; row < BOARD_SIZE; row++) {
                Pane cell = new Pane();
                cell.setStyle("-fx-background-color:  #C4A484; -fx-border-color: #483C32");
                board.add(cell, col, row);
            }
        }

        addCirclesToBoard();
        drawHandicaps();
        drawNavigationArrows();
        drawPassButton();
        drawResignButton();
        initTimer();

        //creating output file
        //For now creating a file is deactivated, otherwise there would be too much files created while coding
        fileControl.createFile(this, "", PLAYER1NAME, PLAYER2NAME, BOARD_SIZE, KOMI, HANDICAPS);
    }

    /*
      ================================================================================================================

                                            draw board helper functions

      ================================================================================================================
     */

    private void boardLabelling() {
        //add color and labelling but without borders
        for (int i = 0; i <= BOARD_SIZE; i++) {
            //top color
            Pane topLetterCell = new Pane();
            topLetterCell.setStyle("-fx-background-color:  #C4A484");
            board.add(topLetterCell, i, 0);

            //right color
            if (i != 0 && i != BOARD_SIZE) {
                Pane rightCell = new Pane();
                rightCell.setStyle("-fx-background-color:  #C4A484");
                board.add(rightCell, BOARD_SIZE, i);
            }

            //bottom color
            Pane bottomLetterCell = new Pane();
            bottomLetterCell.setStyle("-fx-background-color:  #C4A484");
            board.add(bottomLetterCell, i, BOARD_SIZE);

            //right labelling
            if (i != 0) {
                Label rightNumberCell = new Label();
                rightNumberCell.setText(String.valueOf(i));
                rightNumberCell.setCenterShape(true);
                rightNumberCell.setFont(Font.font("Baskerville Old Face", FontWeight.BOLD, 13));
                rightNumberCell.setStyle("-fx-text-fill: #483C32; -fx-font-size: 15");
                GridPane.setHalignment(rightNumberCell, HPos.RIGHT);
                GridPane.setValignment(rightNumberCell, VPos.TOP);
                rightNumberCell.translateYProperty().bind(rightNumberCell.heightProperty().divide(2).multiply(-1));
                topLetterCell.toBack();
                bottomLetterCell.toBack();
                board.add(rightNumberCell, BOARD_SIZE, i);
            }

            //left color
            if (i != 0 && i != BOARD_SIZE) {
                Pane leftNumberCell = new Pane();
                leftNumberCell.setStyle("-fx-background-color:  #C4A484");
                board.add(leftNumberCell, 0, i);
            }

            if (i < BOARD_SIZE) {
                Label letter = new Label();
                letter.setText(String.valueOf(ALPHABET[i]));
                letter.setCenterShape(true);
                letter.setFont(Font.font("Baskerville Old Face", FontWeight.BOLD, 13));
                letter.setStyle("-fx-text-fill: #C4A484");
                GridPane.setHalignment(letter, HPos.RIGHT);
                GridPane.setValignment(letter, VPos.BOTTOM);
                letter.setStyle("-fx-font-size: 15");
                letter.translateXProperty().bind(bottomLetterCell.widthProperty().multiply(-0.8));
                board.add(letter, i + 1, BOARD_SIZE);
            }


        }

    }


    //adding circles to all potential locations on the board
    private void addCirclesToBoard() {
        for (int row = 1; row <= BOARD_SIZE; row++) {
            for (int col = 1; col <= BOARD_SIZE; col++) {
                Circle circle = new Circle(10, TRANSPARENT);
                board.add(circle, row, col);
                circlesOfBoard[row][col] = circle;

                //make stones resizable and adjust X and Y properties
                circle.radiusProperty().bind(boardPane.heightProperty().multiply(0.8).divide(BOARD_SIZE).divide(4));
                circle.translateYProperty().bind(boardPane.heightProperty().multiply(0.6).divide(BOARD_SIZE * 2.4).multiply(-1));
                circle.translateXProperty().bind(boardPane.heightProperty().multiply(0.8).divide(BOARD_SIZE * 3.9).multiply(-1));

                //initial start
                if (HANDICAPS <= 0) {
                    modeAndMoveDisplay.setText(pl1.getText() + "'s turn!");
                } else {
                    modeAndMoveDisplay.setText(pl2.getText() + "'s turn!");
                    lastColor = WHITE;
                }
                modeAndMoveDisplay.setFont(Font.font("Baskerville Old Face", FontWeight.BOLD, 15));

                //color for hovering
                final Color HOVER_BLACK = Color.valueOf("#00000070");
                final Color HOVER_WHITE = Color.valueOf("#FFFFFF70");

                //when the mouse is clicked the circle will be filled with a white or black colour depending on whose turn it is
                circle.setOnMouseClicked(e -> {
                    if (modePlay.isSelected())
                        if (circle.getFill() == HOVER_WHITE || circle.getFill() == HOVER_BLACK)
                            setStone(circle);

                });

                //when the mouse is hovering over a transparent circle this circle is coloured white or black
                //side note: these colours are a little different from the white and black that a circle is filled with
                //           when clicked so that .equals will return false
                circle.setOnMouseEntered(e -> {
                    if (modePlay.isSelected()) {
                        if (circle.getFill() == TRANSPARENT) {
                            if (lastColor == BLACK)
                                circle.setFill(HOVER_BLACK);
                            else
                                circle.setFill(HOVER_WHITE);
                        }
                    }
                });

                //when the mouse is no longer hovering over the circle the colour is removed
                circle.setOnMouseExited(e -> {
                    if (modePlay.isSelected()) {
                        if (circle.getFill() == HOVER_WHITE || circle.getFill() == HOVER_BLACK)
                            circle.setFill(TRANSPARENT);
                    }
                });
            }
        }
    }

    private void drawHandicaps() {
        int dif;
        if(BOARD_SIZE == 9)
            dif = 2;
        else
            dif = 3;
        int lowerValue = dif + 1;
        int higherValue = BOARD_SIZE - dif;
        int midValue = BOARD_SIZE/2 + 1;

        if (HANDICAPS >= 1) {
            circlesOfBoard[lowerValue][higherValue].setFill(BLACK);
            boardLogicControl.setStoneToList(BLACK, lowerValue - 1, higherValue - 1);
        }
        if (HANDICAPS >= 2) {
            circlesOfBoard[higherValue][lowerValue].setFill(BLACK);
            boardLogicControl.setStoneToList(BLACK, higherValue - 1, lowerValue - 1);
        }

        if (HANDICAPS >= 3) {
            circlesOfBoard[higherValue][higherValue].setFill(BLACK);
            boardLogicControl.setStoneToList(BLACK, higherValue - 1, higherValue - 1);
        }

        if (HANDICAPS >= 4) {
            circlesOfBoard[lowerValue][lowerValue].setFill(BLACK);
            boardLogicControl.setStoneToList(BLACK, lowerValue - 1, lowerValue - 1);
        }

        if (HANDICAPS >= 5) {
            circlesOfBoard[midValue][midValue].setFill(BLACK);
            boardLogicControl.setStoneToList(BLACK, midValue - 1, midValue - 1);
        }

        if (HANDICAPS >= 6) {
            circlesOfBoard[midValue][lowerValue].setFill(BLACK);
            boardLogicControl.setStoneToList(BLACK, midValue - 1, lowerValue - 1);
        }

        if (HANDICAPS >= 7) {
            circlesOfBoard[midValue][higherValue].setFill(BLACK);
            boardLogicControl.setStoneToList(BLACK, midValue - 1, higherValue - 1);
        }

        if (HANDICAPS >= 8) {
            circlesOfBoard[lowerValue][midValue].setFill(BLACK);
            boardLogicControl.setStoneToList(BLACK, lowerValue - 1, midValue - 1);
        }

        if (HANDICAPS == 9) {
            circlesOfBoard[higherValue][midValue].setFill(BLACK);
            boardLogicControl.setStoneToList(BLACK, higherValue - 1, midValue - 1);
        }
        modeAndMoveDisplay.setFont(Font.font("Baskerville Old Face", FontWeight.BOLD, 15));
    }

    private void drawNavigationArrows() {
        leftArrow.translateXProperty().bind(leftRegion.widthProperty().divide(2));
        leftArrow.translateYProperty().bind(leftRegion.heightProperty().divide(2));

        rightArrow.translateXProperty().bind(rightRegion.widthProperty().divide(2));
        rightRegion.translateYProperty().bind(rightRegion.heightProperty().divide(2));
    }

    private void drawPassButton() {
        passButton.prefWidthProperty().bind(boardPane.widthProperty().multiply(0.08));

        passButton.setOnMouseEntered(e -> passButton.setStyle("-fx-background-color: #C4A484; -fx-border-color: #483C32"));
        passButton.setOnMouseExited(e -> passButton.setStyle("-fx-background-color: transparent; -fx-border-color: #483C32"));

        //pass logic
        passButton.setOnMouseClicked(e -> {
            if (lastColor == BLACK) {
                modeAndMoveDisplay.setText(pl1.getText() + " passed! - " + pl2.getText() + "'s turn");
                lastColor = WHITE;
            } else {
                modeAndMoveDisplay.setText(pl2.getText() + " passed! - " + pl1.getText() + "'s turn");
                lastColor = BLACK;
            }
            //file functionality is disabled for now
            fileControl.writeAction("passed");
        });
    }

    private void drawResignButton() {
        resignButton.prefWidthProperty().bind(boardPane.widthProperty().multiply(0.08));
        resignButton.setOnMouseEntered(e -> resignButton.setStyle("-fx-background-color: #C4A484; -fx-border-color: #483C32"));
        resignButton.setOnMouseExited(e -> resignButton.setStyle("-fx-background-color: transparent; -fx-border-color: #483C32"));

        //resign logic
        resignButton.setOnMouseClicked(e -> {
            if (lastColor == BLACK) {
                modeAndMoveDisplay.setText(pl1.getText() + " resigned! - " + pl2.getText() + " won!");
            } else {
                modeAndMoveDisplay.setText(pl2.getText() + " resigned! - " + pl1.getText() + " won!");
            }
            //file functionality is disabled for now
            fileControl.writeAction("resigned");
        });
    }

    private void initTimer() {
        START_TIME = System.currentTimeMillis();
        Timeline timerTimeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateTimer()));
        timerTimeline.setCycleCount(Animation.INDEFINITE);
        timerTimeline.play();
    }


    private void updateTimer() {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - START_TIME;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(elapsedTime);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedTime) - TimeUnit.MINUTES.toSeconds(minutes);

        timerBlack.setText(String.format("%02d:%02d", minutes, seconds));
        timerWhite.setText(String.format("%02d:%02d", minutes, seconds));
    }

    /*
      ================================================================================================================

                                            other helper functions

      ================================================================================================================
     */

    public void setStone(Circle c) {
        modeAndMoveDisplay.setFont(Font.font("Baskerville Old Face", FontWeight.BOLD, 15));
        int row;
        int col;
        for (Node n : board.getChildren()) {
            if (n instanceof Circle && n.equals(c)) {
                row = GridPane.getRowIndex(n);
                col = GridPane.getColumnIndex(n);
                String stonePosition = (row-1) + "" + (ALPHABET[col-1]);
                fileControl.writeMoves(stonePosition);
                terminalInfo("Stone placed at: " + row + ALPHABET[col-1]);
                circlesOfBoard[row][col] = c;
                c.setFill(lastColor);
                boardLogicControl.setStoneToList(lastColor, row - 1, col - 1);

                if (lastColor == WHITE) {
                    lastColor = BLACK;
                    modeAndMoveDisplay.setText(pl1.getText() + "'s turn!");
                } else {
                    lastColor = WHITE;
                    modeAndMoveDisplay.setText(pl2.getText() + "'s turn!");
                }
                break;
            }
        }
        terminalInfo("Error: System was unable to located circle!");
    }

    protected void deleteStoneGroup(StoneGroup toDelete) {
        if (toDelete.getColour() == WHITE) {
            blackTrappedStones += toDelete.getPosition().size();
            displayTrappedStone(blackTrappedStones, blackTrapped);
        }else {
            whiteTrappedStones += toDelete.getPosition().size();
            displayTrappedStone(whiteTrappedStones, whiteTrapped);
        }

        //finds the circle for every position of stoneGroup toDelete and sets the visibility to TRANSPARENT
        for (Position p : toDelete.getPosition()) {
            Circle c = circlesOfBoard[p.row()+1][p.col()+1];
            if(c.getFill() == TRANSPARENT)
                terminalInfo("Error: no stone found at " + (p.row()+1) + ALPHABET[p.col()]);
            else {
                circlesOfBoard[p.row() + 1][p.col() + 1].setFill(TRANSPARENT);
                terminalInfo("Stone deleted: " + (p.row() + 1) + ALPHABET[p.col()]);
            }
        }
    }

    /*
      ================================================================================================================

                                            other system functions

      ================================================================================================================
     */

    private void terminalInfo(String data){
        System.out.println(data);
    }
}