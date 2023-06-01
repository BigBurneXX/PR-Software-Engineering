package com.example.go_gruppe1.controller;

import com.example.go_gruppe1.model.FileControl;
import com.example.go_gruppe1.model.Move;
import com.example.go_gruppe1.model.command.*;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static javafx.scene.paint.Color.*;

public class boardMaskController {
    /*
      ================================================================================================================

                                            GUI component declaration

      ================================================================================================================
     */

    @FXML
    private GridPane board, infoPane;

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
    private Label pl1, pl2, komiBoard, handicapsBoard, blackTrapped, whiteTrapped, timerBlack, timerWhite, blackTimeLabel, whiteTimeLabel;
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
    private Game game;
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
    private Timeline timerTimeline1;
    private Timeline timerTimeline2;
    private long START_TIME1;
    private long START_TIME2;
    private long elapsedTime1 = 0;
    private long elapsedTime2 = 0;
    protected int BYOYOMI_NUMBER = 0;
    private int blackByoyomi = 0, whiteByoyomi = 0;
    protected int BYOYOMI_TIME = 0;

    /*
      ----------------------------------------------------------------------------------------------------------------
                                        global variables
      ----------------------------------------------------------------------------------------------------------------
     */
    private Circle[][] circlesOfBoard;
    private Color lastColor = BLACK;
    private int blackTrappedStones = 0;
    private int whiteTrappedStones = 0;
    private final List<Circle[][]> changeLog = new ArrayList<>();
    private int changeLogCounter = -1;
    private long blackTotal = 0, whiteTotal = 0;

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
        inputMask.initiateDisplay();

        Node source = topRegion.getTop();
        Stage stage = (Stage) source.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setMinWidth(630);
        stage.setMinHeight(500);
        stage.centerOnScreen();
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
    public void switchToNewGame(String player1Name, String player2Name, String komi, String handicaps, int boardSize, List<Move> moves) throws IOException{
        System.out.println(player1Name + player2Name + komi + handicaps + boardSize);
        initiateDisplay(player1Name, player2Name, komi, handicaps, boardSize);
        Color currentColor = BLACK;
        for(Move m: moves){
            int col = new String(ALPHABET).indexOf(m.col());
            fileControl.writeMoves((m.row() - 1), ALPHABET[col], m.text());
            setSampleSolutionDisplay(m.text());
            terminalInfo("Stone (" + currentColor + ") placed at: " + m.row() + ALPHABET[col]);
            circlesOfBoard[col+1][m.row()+1].setFill(currentColor);
            boardLogicControl.setStoneToList(currentColor, m.row(), col);
            game.executeCommand(new PlaceStoneCommand(game.getBoard(), m.row(), col, currentColor));
            currentColor = (currentColor == BLACK ? WHITE : BLACK);
        }
        lastColor = currentColor;
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
        modeAndMoveDisplay.setFont(Font.font("System", FontWeight.BOLD, 15));
        modeAndMoveDisplay.prefHeightProperty().bind(bottomRegion.heightProperty().multiply(0.25));

        modeAndMoveDisplay.setText((lastColor == BLACK ? pl1.getText() : pl2.getText()) + "'s turn!");
    }

    @FXML
    public void onModeNavigateClick() {
        terminalInfo("navigation mode activated!");
        //printSomething();
        leftArrow.setVisible(true);
        rightArrow.setVisible(true);
        //TODO logic for arrow clicks
        rightArrow.setOnMouseClicked(e -> System.out.println("something at last"));
        leftArrow.setOnMouseClicked(e -> {
            printSomething();
            //copying somehow doesn't work yet
            /*
            int index = changeLogCounter - 1;
            if(!changeLog.isEmpty() && !(index < 0)) {
                Circle[][] boardToBe = changeLog.get(index);
                System.out.println("white: " + WHITE + "\nblack: " + BLACK);
                for(int i = 1; i < boardToBe.length; i++){
                    System.out.print("i(" + i + ")");
                    for(int j = 1; j < boardToBe[i].length; j++){
                        circlesOfBoard[i][j].setFill(TRANSPARENT);
                        System.out.print(", j(" + j + ")" + circlesOfBoard[i][j].getFill() + "\t");
                    }
                    System.out.println();
                }
                System.out.println("\nshould be:\n");
                printSomething();

                        /*
                        if(boardToBe[k][j] != null) {
                            /*if(circlesOfBoard[k][j] != null){
                                circlesOfBoard[k][j].setFill(TRANSPARENT);
                                System.out.println("GOT YOU");
                            }
                            circlesOfBoard[k][j].setFill(boardToBe[k][j].getFill());
                            System.out.println("1Colored circle " + boardToBe[k][j].getFill() + " at row " + k + ", col " + j);
                            System.out.println("should be colored: " + k + ", " + j + " with color " + circlesOfBoard[k][j].getFill());
                        }else if(circlesOfBoard[k][j] != null) {
                            circlesOfBoard[k][j].setFill(TRANSPARENT);
                            circlesOfBoard = null;
                            System.out.println("2Colored circle " + boardToBe[k][j].getFill() + " at row " + k + ", col " + j);
                        }
            */
                changeLogCounter--;
            //}
            //System.out.println("empty?" + changeLog.isEmpty() + "\n index < 0? " + index);
            System.out.println("well something is not working!");
        });

        passButton.setVisible(false);
        resignButton.setVisible(false);
        modeAndMoveDisplay.setText("Navigation mode activated");
        modeAndMoveDisplay.setFont(Font.font("System", FontWeight.BOLD, board.getHeight() * 0.10));
        modeAndMoveDisplay.prefHeightProperty().bind(bottomRegion.heightProperty().multiply(0.25));
    }
    private void printSomething(){
        System.out.println("full test");
        for(int i = changeLogCounter; i >= 0; i--) {
            Circle[][] test3 = changeLog.get(changeLogCounter);
            System.out.println("test" + i + "\nwhite: " + WHITE + "\nblack: " + BLACK);
            for (int k = 1; k < test3.length; k++) {
                System.out.print("k: " + k);
                for (int j = 1; j < test3[k].length; j++)
                    System.out.print(", j: " + j + "(" + (test3[k][j] != null ? test3[k][j].getFill() : TRANSPARENT + "Transparent") + ")");
                System.out.println();
            }
        }
    }
    /*
      ================================================================================================================

                                            getter and setter

      ================================================================================================================
     */

    //TODO sample solution
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
        game = new Game(this, boardSize);
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
        KOMI = Double.parseDouble(komiAdvantage);
        komiBoard.setText("Komi: "+ KOMI);
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
                    sampleSolutionDisplay.setText(sampleSolutionDisplay.getText() + "\nInvalid handicap input -> Handicaps set to 0");
                } else {
                    handicapsBoard.setText("Handicaps: " + HANDICAPS);
                }
            } else if (BOARD_SIZE == 19) {
                if (HANDICAPS < 0 || HANDICAPS > 9) {
                    handicapsBoard.setText("Handicaps: " + "0");
                    HANDICAPS = 0;
                    sampleSolutionDisplay.setText(sampleSolutionDisplay.getText() + "\nInvalid handicap input -> Handicaps set to 0");
                } else {
                    handicapsBoard.setText("Handicaps: " + HANDICAPS);
                }
            }
            terminalInfo("handicaps: " + HANDICAPS);
        } catch (NumberFormatException nfe) {
            HANDICAPS = 0;
            handicapsBoard.setText("Handicaps: " + HANDICAPS);
            if (!handicaps.isEmpty()) {
                sampleSolutionDisplay.setText(sampleSolutionDisplay.getText() + "\nInvalid handicap input -> Handicaps set to 0");
                terminalInfo("invalid handicap input");
            }
            terminalInfo("handicaps set to: " + HANDICAPS);
        }
    }

    private void displayTrappedStone(int numberTrapped, Label trappedLabel) {
            trappedLabel.setText("Trapped: " + numberTrapped);
            terminalInfo(trappedLabel.getId() + " set to " + numberTrapped);
    }

    protected void initiateTimeRules(String byoyomiNumber, String byoyomiTime) {
        try {
            BYOYOMI_NUMBER = Integer.parseInt(byoyomiNumber);
            BYOYOMI_TIME = Integer.parseInt(byoyomiTime);
            if(BYOYOMI_NUMBER <= 0 || BYOYOMI_TIME < 30) {
                BYOYOMI_NUMBER = 0;
                BYOYOMI_TIME = 0;
                blackTimeLabel.setVisible(false);
                GridPane.setValignment(blackTimeLabel, VPos.CENTER);
                timerBlack.setVisible(false);
                whiteTimeLabel.setVisible(false);
                timerWhite.setVisible(false);
                terminalInfo("Invalid input of Byoyomi");
            } else {
                blackByoyomi = BYOYOMI_NUMBER;
                whiteByoyomi = BYOYOMI_NUMBER;
                blackTimeLabel.setText(BYOYOMI_NUMBER + " time period(s) à " + BYOYOMI_TIME + " s");
                whiteTimeLabel.setText(BYOYOMI_NUMBER + " time period(s) à " + BYOYOMI_TIME + " s");
                terminalInfo("Number of Byoyomi time periods set to " + BYOYOMI_NUMBER);
                terminalInfo("Time period duration: " + BYOYOMI_TIME);
            }
        } catch (NumberFormatException nfe) {
            BYOYOMI_NUMBER = 0;
            BYOYOMI_TIME = 0;
            blackTimeLabel.setVisible(false);
            GridPane.setValignment(blackTimeLabel, VPos.CENTER);
            timerBlack.setVisible(false);
            whiteTimeLabel.setVisible(false);
            timerWhite.setVisible(false);

            terminalInfo("No or false byoyomi input");
        }
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

        //bind bottom and upper region to 20% of window height
        bottomRegion.prefHeightProperty().bind(boardPane.heightProperty().multiply(0.1));
        topRegion.prefHeightProperty().bind(boardPane.heightProperty().multiply(0.2));

        //set left, right and center (board) region to 60% of window height
        leftRegion.prefHeightProperty().bind(boardPane.heightProperty().multiply(0.7));
        rightRegion.prefHeightProperty().bind(boardPane.heightProperty().multiply(0.7));
        board.prefHeightProperty().bind(boardPane.heightProperty().multiply(0.7));

        //bind center width to 60% of window height, so it's a square
        board.prefWidthProperty().bind(boardPane.heightProperty().multiply(0.7));

        //bind left and right width to remaining width of the window of what's left from taking 60% of the height
        leftRegion.prefWidthProperty().bind(boardPane.widthProperty().subtract(board.prefWidthProperty()).divide(2));
        rightRegion.prefWidthProperty().bind(boardPane.widthProperty().subtract(board.prefWidthProperty()).divide(2));

        //create grid
        board.getChildren().clear();
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
        //immediately start timer for 1st player
        if(HANDICAPS == 0) {
            START_TIME1 = System.currentTimeMillis();
            timerTimeline1.play();
        } else {
            START_TIME2 = System.currentTimeMillis();
            timerTimeline2.play();
        }


        //creating output file
        //For now creating a file is deactivated, otherwise there would be too much files created while coding
        fileControl.createFile(this, "", PLAYER1NAME, PLAYER2NAME, BOARD_SIZE, KOMI, HANDICAPS, BYOYOMI_NUMBER, BYOYOMI_TIME);
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

            double sizeBinding = 0.035;
            int padding = 5;
            if(BOARD_SIZE == 19) {
                sizeBinding = 0.02;
                padding = 4;
            }

            //right labelling
            if (i != 0) {
                Label rightNumberCell = new Label();
                rightNumberCell.setText(String.valueOf(BOARD_SIZE - i + 1));
                rightNumberCell.setCenterShape(true);
                rightNumberCell.styleProperty().bind(Bindings.concat(
                        "-fx-text-fill: #483C32; ",
                        "-fx-font-weight: bold; ",
                        "-fx-font-size: ", board.heightProperty().multiply(sizeBinding).asString()
                ));
                rightNumberCell.setPadding(new Insets(0, padding, 0, 0));
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

            //bottom labelling
            if (i < BOARD_SIZE) {
                Label letter = new Label();
                letter.setText(String.valueOf(ALPHABET[i]));
                letter.setCenterShape(true);
                letter.styleProperty().bind(Bindings.concat(
                        "-fx-text-fill: #483C32; ",
                        "-fx-font-weight: bold; ",
                        "-fx-font-size: ", board.heightProperty().multiply(sizeBinding).asString()
                ));
                letter.setPadding(new Insets(0, 0, padding - 3, 0));
                GridPane.setHalignment(letter, HPos.RIGHT);
                GridPane.setValignment(letter, VPos.BOTTOM);
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
                circle.translateYProperty().bind(boardPane.heightProperty().multiply(0.7).divide(BOARD_SIZE * 2.4).multiply(-1));
                circle.translateXProperty().bind(boardPane.heightProperty().multiply(0.8).divide(BOARD_SIZE * 3.9).multiply(-1));

                //initial start
                if (HANDICAPS <= 0) {
                    modeAndMoveDisplay.setText(pl1.getText() + "'s turn!");
                } else {
                    modeAndMoveDisplay.setText(pl2.getText() + "'s turn!");
                    lastColor = WHITE;
                }
                modeAndMoveDisplay.setFont(Font.font("System", FontWeight.BOLD, 15));

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
            game.executeCommand(new PlaceStoneCommand(game.getBoard(), lowerValue - 1, higherValue - 1, BLACK));
        }
        if (HANDICAPS >= 2) {
            circlesOfBoard[higherValue][lowerValue].setFill(BLACK);
            boardLogicControl.setStoneToList(BLACK, higherValue - 1, lowerValue - 1);
            game.executeCommand(new PlaceStoneCommand(game.getBoard(), higherValue - 1, lowerValue - 1, BLACK));
        }

        if (HANDICAPS >= 3) {
            circlesOfBoard[higherValue][higherValue].setFill(BLACK);
            boardLogicControl.setStoneToList(BLACK, higherValue - 1, higherValue - 1);
            game.executeCommand(new PlaceStoneCommand(game.getBoard(), higherValue - 1, higherValue - 1, BLACK));
        }

        if (HANDICAPS >= 4) {
            circlesOfBoard[lowerValue][lowerValue].setFill(BLACK);
            boardLogicControl.setStoneToList(BLACK, lowerValue - 1, lowerValue - 1);
            game.executeCommand(new PlaceStoneCommand(game.getBoard(), lowerValue - 1, lowerValue - 1, BLACK));
        }

        if (HANDICAPS >= 5) {
            circlesOfBoard[midValue][midValue].setFill(BLACK);
            boardLogicControl.setStoneToList(BLACK, midValue - 1, midValue - 1);
            game.executeCommand(new PlaceStoneCommand(game.getBoard(), midValue - 1, midValue - 1, BLACK));
        }

        if (HANDICAPS >= 6) {
            circlesOfBoard[midValue][lowerValue].setFill(BLACK);
            boardLogicControl.setStoneToList(BLACK, midValue - 1, lowerValue - 1);
            game.executeCommand(new PlaceStoneCommand(game.getBoard(), midValue - 1, lowerValue - 1, BLACK));
        }

        if (HANDICAPS >= 7) {
            circlesOfBoard[midValue][higherValue].setFill(BLACK);
            boardLogicControl.setStoneToList(BLACK, midValue - 1, higherValue - 1);
            game.executeCommand(new PlaceStoneCommand(game.getBoard(), midValue - 1, higherValue - 1, BLACK));
        }

        if (HANDICAPS >= 8) {
            circlesOfBoard[lowerValue][midValue].setFill(BLACK);
            boardLogicControl.setStoneToList(BLACK, lowerValue - 1, midValue - 1);
            game.executeCommand(new PlaceStoneCommand(game.getBoard(), lowerValue - 1, midValue - 1, BLACK));
        }

        if (HANDICAPS == 9) {
            circlesOfBoard[higherValue][midValue].setFill(BLACK);
            boardLogicControl.setStoneToList(BLACK, higherValue - 1, midValue - 1);
            game.executeCommand(new PlaceStoneCommand(game.getBoard(), higherValue - 1, midValue - 1, BLACK));
        }
        modeAndMoveDisplay.setFont(Font.font("System", FontWeight.BOLD, 15));
    }

    private void drawNavigationArrows() {
        leftArrow.translateXProperty().bind(leftRegion.widthProperty().divide(2));
        leftArrow.translateYProperty().bind(leftRegion.heightProperty().divide(2));

        rightArrow.translateXProperty().bind(rightRegion.widthProperty().divide(2));
        rightRegion.translateYProperty().bind(rightRegion.heightProperty().divide(2));
    }

    private void drawPassButton() {
        passButton.prefWidthProperty().bind(boardPane.widthProperty().multiply(0.1));
        passButton.styleProperty().bind(Bindings.concat(
                "-fx-text-fill: #483C32; ",
                "-fx-font-weight: bold; ",
                "-fx-font-size: ", board.heightProperty().multiply(0.03).asString()
        ));
        passButton.setFocusTraversable(false);

        //pass logic
        passButton.setOnMouseClicked(e -> {
            if (lastColor == BLACK) {
                modeAndMoveDisplay.setText(pl1.getText() + " passed! - " + pl2.getText() + "'s turn");
                lastColor = WHITE;
                timerTimeline1.stop();
                START_TIME2 = System.currentTimeMillis();
                timerTimeline2.play();
                System.out.println(passedSlotSeconds1());
                initiateByoyomiRules(1);
                elapsedTime1 = 0;
            } else {
                modeAndMoveDisplay.setText(pl2.getText() + " passed! - " + pl1.getText() + "'s turn");
                lastColor = BLACK;
                timerTimeline2.stop();
                START_TIME1 = System.currentTimeMillis();
                timerTimeline1.play();
                System.out.println(passedSlotSeconds2());
                initiateByoyomiRules(2);
                elapsedTime2 = 0;
            }
            //file functionality is disabled for now
            fileControl.writeAction("passed");
        });
    }

    private void drawResignButton() {
        resignButton.prefWidthProperty().bind(boardPane.widthProperty().multiply(0.1));
        resignButton.styleProperty().bind(Bindings.concat(
                "-fx-text-fill: #483C32; ",
                "-fx-font-weight: bold; ",
                "-fx-font-size: ", board.heightProperty().multiply(0.03).asString()
        ));
        resignButton.setFocusTraversable(false);

        //resign logic
        resignButton.setOnMouseClicked(e -> {
            if (lastColor == BLACK) {
                try {
                    switchToWinnerMask(2, 2);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                modeAndMoveDisplay.setText(pl1.getText() + " resigned! - " + pl2.getText() + " won!");
            } else {
                try {
                    switchToWinnerMask(1, 2);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                modeAndMoveDisplay.setText(pl2.getText() + " resigned! - " + pl1.getText() + " won!");
            }
            //file functionality is disabled for now
            fileControl.writeAction("resigned");
        });
    }

    private void initTimer() {
        timerTimeline1 = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateTimer1()));
        timerTimeline1.setCycleCount(Animation.INDEFINITE);

        timerTimeline2 = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateTimer2()));
        timerTimeline2.setCycleCount(Animation.INDEFINITE);
    }


    private void updateTimer1() {
        long currentTime = System.currentTimeMillis();
        long elapsedTimeForTurn = currentTime - START_TIME1;
        elapsedTime1 += elapsedTimeForTurn;
        START_TIME1 = currentTime;  // update the start time

        long minutes = TimeUnit.MILLISECONDS.toMinutes(elapsedTime1);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedTime1) - TimeUnit.MINUTES.toSeconds(minutes);

        timerBlack.setText(String.format("%02d:%02d", minutes, seconds));
        timerBlack.setPadding(new Insets(0, 0, 4, 0));
    }

    private void updateTimer2() {
        long currentTime = System.currentTimeMillis();
        long elapsedTimeForTurn = currentTime - START_TIME2;
        elapsedTime2 += elapsedTimeForTurn;
        START_TIME2 = currentTime;  // update the start time

        long minutes = TimeUnit.MILLISECONDS.toMinutes(elapsedTime2);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedTime2) - TimeUnit.MINUTES.toSeconds(minutes);

        timerWhite.setText(String.format("%02d:%02d", minutes, seconds));
        timerWhite.setPadding(new Insets(0, 0, 4, 0));
    }

    private int passedSlotSeconds1() {
        long elapsedTimeForTurn = System.currentTimeMillis() - START_TIME1;

        return (int) TimeUnit.MILLISECONDS.toSeconds(elapsedTimeForTurn + elapsedTime1);
    }

    private int passedSlotSeconds2() {
        long elapsedTimeForTurn = System.currentTimeMillis() - START_TIME2;

        return (int) TimeUnit.MILLISECONDS.toSeconds(elapsedTimeForTurn + elapsedTime2);
    }
    private void initiateByoyomiRules(int playerNumber) {
        if(BYOYOMI_NUMBER != 0) {
            if(playerNumber == 1) {
                if(passedSlotSeconds1() > BYOYOMI_TIME) {
                    int slots = passedSlotSeconds1() / BYOYOMI_TIME;
                    blackByoyomi -= slots;
                    if(blackByoyomi < 0) {
                        try {
                            switchToWinnerMask(2, 3);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        modeAndMoveDisplay.setText(pl1.getText() + " used all time slots. " + pl2.getText() + " won!");
                        blackTimeLabel.setText("No time left");
                    } else {
                        blackTimeLabel.setText(blackByoyomi + " time period(s) à " + BYOYOMI_TIME + " s");
                    }
                }
            } else if(playerNumber == 2){
                if(passedSlotSeconds2() > BYOYOMI_TIME) {
                    int slots = passedSlotSeconds2() / BYOYOMI_TIME;
                    whiteByoyomi -= slots;
                    if(whiteByoyomi < 0) {
                        try {
                            switchToWinnerMask(1, 3);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        modeAndMoveDisplay.setText(pl2.getText() + " used all time slots. " + pl1.getText() + " won!");
                        whiteTimeLabel.setText("No time left");
                    } else {
                        whiteTimeLabel.setText(whiteByoyomi + " time period(s) à " + BYOYOMI_TIME + " s");
                    }
                }
            }
        }
    }

    /*
      ================================================================================================================

                                            other helper functions

      ================================================================================================================
     */

    public void setStone(Circle c) {
        modeAndMoveDisplay.setFont(Font.font("System", FontWeight.BOLD, 15));
        int row;
        int col;
        for (Node n : board.getChildren()) {
            if (n instanceof Circle && n.equals(c)) {
                row = GridPane.getRowIndex(n);
                col = GridPane.getColumnIndex(n);
                fileControl.writeMoves((row - 1), ALPHABET[col-1], "");
                terminalInfo("Stone (" + lastColor + ") placed at: " + row + ALPHABET[col-1]);
                System.out.println("attaching stone to row " + row + ", col " + col);
                c.setFill(lastColor);
                boardLogicControl.setStoneToList(lastColor, row - 1, col - 1);
                game.executeCommand(new PlaceStoneCommand(game.getBoard(), row - 1, col - 1, lastColor));

                /*Circle[][] logEntry = copyMatrix(circlesOfBoard);
                changeLog.add(logEntry);
                changeLogCounter++;*/
                printSomething();
                if (lastColor == WHITE) {
                    lastColor = BLACK;
                    modeAndMoveDisplay.setText(pl1.getText() + "'s turn!");
                    timerTimeline2.stop();
                    START_TIME1 = System.currentTimeMillis();
                    timerTimeline1.play(); // start the timer for player 1
                    System.out.println(passedSlotSeconds2());
                    initiateByoyomiRules(2);
                    elapsedTime2 = 0;
                } else {
                    lastColor = WHITE;
                    modeAndMoveDisplay.setText(pl2.getText() + "'s turn!");
                    timerTimeline1.stop();
                    START_TIME2 = System.currentTimeMillis();
                    timerTimeline2.play(); // start the timer for player 2
                    System.out.println(passedSlotSeconds1());
                    initiateByoyomiRules(1);
                    elapsedTime1 = 0;
                }
                break;
            }
             //   terminalInfo("Error: System was unable to located circle!");
        }
    }

    public void deleteStoneGroup(StoneGroup toDelete) {
        if (toDelete.getColour() == WHITE) {
            blackTrappedStones += toDelete.getPosition().size();
            displayTrappedStone(blackTrappedStones, blackTrapped);
        }else {
            whiteTrappedStones += toDelete.getPosition().size();
            displayTrappedStone(whiteTrappedStones, whiteTrapped);
        }

        //finds the circle for every position of stoneGroup toDelete and sets the visibility to TRANSPARENT
        for (Position p : toDelete.getPosition()) {
            Circle c = circlesOfBoard[p.col()+1][p.row()+1];
            if(c.getFill() == TRANSPARENT)
                terminalInfo("Error: no stone found at " + (p.row()+1) + ALPHABET[p.col()]);
            else {
                c.setFill(TRANSPARENT);
                terminalInfo("Stone deleted: " + (p.row() + 1) + ALPHABET[p.col()]);
            }
        }
        /*Circle[][] logEntry = copyMatrix(circlesOfBoard);
        changeLog.add(logEntry);
        changeLogCounter++;*/
    }

    public static int calculateScore(char playerColor, char[][] board) {
        int score = 0;
        for (char[] chars : board)
            for (char aChar : chars)
                if (aChar == playerColor)
                    score++;
        return score;
    }

    public static void endGame(char[][] board) {
        int blackScore = calculateScore('B', board);
        int whiteScore = calculateScore('W', board);

        System.out.println("Black score: " + blackScore);
        System.out.println("White score: " + whiteScore);

        if (blackScore > whiteScore) {
            System.out.println("Black won the game!");
        } else if (blackScore < whiteScore) {
            System.out.println("White won the game!");
        } else {
            System.out.println("The game is a draw!");
        }
    }

    private void switchToWinnerMask(int player, int reasonForWinning) throws IOException {
        //reason for winning 1 - points, 2 - resigned, 3 - byoyomi
        if((player == 1 || player == 2) && (reasonForWinning >= 1 && reasonForWinning <= 3)) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/winnerMaskGUI.fxml"));
            Parent root = loader.load();

            winnerMaskController winnerMask = loader.getController();
            winnerMask.setSize(getWidth(), getHeight());
            winnerMask.setReasonForWinning(reasonForWinning);

            if(player == 1) {
                winnerMask.setName(pl1.getText(), pl2.getText());
                winnerMask.setTotalPoints(blackTotal);
                winnerMask.setTrapped(blackTrappedStones);
                winnerMask.setExtraPoints("Handicaps:");
                winnerMask.setExtraPointsValue(HANDICAPS);
                winnerMask.setByoyomi(BYOYOMI_NUMBER, blackByoyomi, BYOYOMI_TIME);
                terminalInfo("Black won... \n[log end]");
            } else {
                winnerMask.setName(pl2.getText(), pl1.getText());
                winnerMask.setTotalPoints(whiteTotal);
                winnerMask.setTrapped(whiteTrappedStones);
                winnerMask.setExtraPoints("Komi:");
                winnerMask.setExtraPointsValue(KOMI);
                winnerMask.setByoyomi(BYOYOMI_NUMBER, whiteByoyomi, BYOYOMI_TIME);
                terminalInfo("White won... \n[log end]");
            }

            Node source = topRegion.getTop();
            Stage stage = (Stage) source.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setMinHeight(300);
            stage.setMinWidth(550);
            stage.centerOnScreen();
            stage.show();
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

    private Circle[][] copyMatrix(Circle[][] array) {
        return Arrays.stream(array)
                .map(this::copyArray)
                .toArray(Circle[][]::new);
    }

    private Circle[] copyArray(Circle[] array){
        return Arrays.stream(array)
                .map(this::copyCircle)
                .toArray(Circle[]::new);
    }

    private Circle copyCircle(Circle circle) {
        return (circle == null ? null : new Circle(circle.getCenterX(), circle.getCenterY(), circle.getRadius()));
    }
}