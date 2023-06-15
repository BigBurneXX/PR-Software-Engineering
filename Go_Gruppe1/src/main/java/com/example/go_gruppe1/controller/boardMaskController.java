package com.example.go_gruppe1.controller;

import com.example.go_gruppe1.model.*;
import com.example.go_gruppe1.model.command.Position;
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
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    private Label pl1, pl2, blackTrapped, whiteTrapped, timerBlack, timerWhite, blackTimeLabel, whiteTimeLabel, title;
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

    private GameHandler gameHandler;
    private PlayerHandler playerHandler;
    private FileHandler fileHandler;
    //private final FileControl fileControl = new FileControl();

    /*
      ----------------------------------------------------------------------------------------------------------------
                                        constants
      ----------------------------------------------------------------------------------------------------------------
     */

    private final boolean LOGGING = true;
    private final char[] ALPHABET = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T'};
    private int BOARD_SIZE;
    private int HANDICAPS;
    private double KOMI;
    protected int BYOYOMI_NUMBER;
    protected int BYOYOMI_TIME;
    private Player PLAYER_BLACK;
    private Player PLAYER_WHITE;

    /*
      ----------------------------------------------------------------------------------------------------------------
                                        constants - GUI multipliers
      ----------------------------------------------------------------------------------------------------------------
     */

    private final double PLAYER_LABEL_MULTIPLIER = 0.04;
    private final double TRAPPED_LABEL_MULTIPLIER = 0.026;
    private final double TIMER_LABEL_MULTIPLIER = 0.026;
    private final double TIME_LABEL_MULTIPLIER = 0.023;
    private final double TOP_REGION_MULTIPLIER = 0.2;
    private final double BOTTOM_REGION_MULTIPLIER = 0.1;
    private final double REGION_MULTIPLIER = 0.7;

    /*
      ----------------------------------------------------------------------------------------------------------------
                                        global variables
      ----------------------------------------------------------------------------------------------------------------
     */
    private Circle[][] circlesOfBoard;
    private int blackTrappedStones = 0, whiteTrappedStones = 0;
    private long blackTotal = 0, whiteTotal = 0;
    private int blackByoyomi = 0, whiteByoyomi = 0;

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
        fileHandler.save();
    }

    @FXML
    public void onOpenFileClick() {
        terminalInfo("Loading file... \n[log end]");
        fileHandler.open();
    }

    public void switchToNewGame(String player1Name, String player2Name, String komi, String handicaps, int boardSize, List<Move> moves) throws IOException {
        terminalInfo(player1Name + player2Name + komi + handicaps + boardSize);
        initiateDisplay(player1Name, player2Name, komi, handicaps, boardSize);
        Color currentColor = BLACK;
        for (Move m : moves) {
            int col = new String(ALPHABET).indexOf(m.col());
            fileHandler.write((m.row() - 1), ALPHABET[col], m.text());
            setSampleSolutionDisplay(m.text());
            terminalInfo("Stone (" + currentColor + ") placed at: " + m.row() + ALPHABET[col]);
            circlesOfBoard[col + 1][m.row() + 1].setFill(currentColor);
            gameHandler.addMove(m.row(), col, currentColor);
            currentColor = (currentColor == BLACK ? WHITE : BLACK);
        }
        if(currentColor.equals(BLACK))
            playerHandler.changePlayer();
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

        modeAndMoveDisplay.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", boardPane.heightProperty().multiply(0.025).asString()
        ));

        modeAndMoveDisplay.setText(playerHandler.getCurrentPlayer().getName() + "'s turn!");
    }

    @FXML
    public void onModeNavigateClick() {
        terminalInfo("navigation mode activated!");
        leftArrow.setVisible(true);
        rightArrow.setVisible(true);

        rightArrow.setOnMouseClicked(e -> {
            gameHandler.redo();
            drawStones();
        });

        leftArrow.setOnMouseClicked(e -> {
            gameHandler.undo();
            drawStones();
        });

        passButton.setVisible(false);
        resignButton.setVisible(false);
        modeAndMoveDisplay.setText("Navigation mode activated");
        modeAndMoveDisplay.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", boardPane.heightProperty().multiply(0.025).asString()
        ));
    }

    /*
      ================================================================================================================

                                            getter and setter

      ================================================================================================================
     */

    private void setSampleSolutionDisplay(String text) {
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

                                            initialization methods

      ================================================================================================================
     */
    protected void initiateDisplay(String player1Name, String player2Name, String komi, String handicaps, int boardSize) {
        BOARD_SIZE = boardSize;
        terminalInfo("starting a new game\nboard size set to: " + BOARD_SIZE);
        //boardLogicControl = new BoardLogicControl(this, BOARD_SIZE);
        gameHandler = new GameHandler(BOARD_SIZE);
        initiatePlayers(player1Name, player2Name);
        playerHandler = new PlayerHandler(PLAYER_BLACK.getName(), PLAYER_WHITE.getName());
        playerHandler.setLogging(true);
        displayKomi(komi);
        displayHandicaps(handicaps);
        displayTrappedStone(0, blackTrapped);
        displayTrappedStone(0, whiteTrapped);
        onModePlayClick();
        modePlay.setSelected(true);
        circlesOfBoard = new Circle[BOARD_SIZE + 1][BOARD_SIZE + 1];
        drawBoard();

        title.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", boardPane.heightProperty().multiply(0.08).asString()
        ));
    }

    private void initiatePlayers(String p1, String p2) {
        String playerBlack = p1.isEmpty() ? "Player 1" : p1;
        String playerWhite = p2.isEmpty() ? "Player 2" : p2;
        PLAYER_BLACK = new Player(playerBlack, BLACK, GOLD);
        PLAYER_WHITE = new Player(playerWhite, WHITE, GOLD);

        displayPlayerNames();
    }

    private void displayPlayerNames(){
        pl1.setText(PLAYER_BLACK.getName() + " (Black)");
        pl2.setText(PLAYER_WHITE.getName() + " (White)");
        terminalInfo("player1 set to: " + PLAYER_BLACK.getName());
        terminalInfo("player2 set to: " + PLAYER_WHITE.getName());

        pl1.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", boardPane.heightProperty().multiply(PLAYER_LABEL_MULTIPLIER).asString()
        ));
        pl2.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", boardPane.heightProperty().multiply(PLAYER_LABEL_MULTIPLIER).asString()
        ));
    }

    private void displayKomi(String komiAdvantage) {
        KOMI = Double.parseDouble(komiAdvantage);
    }

    private void displayHandicaps(String handicaps) {
        //only numeric values can be entered
        try {
            HANDICAPS = Integer.parseInt(handicaps);
            //only values greater than 0 are valid
            if (BOARD_SIZE == 9 || BOARD_SIZE == 13) {
                if (HANDICAPS < 0 || HANDICAPS > 5) {
                    HANDICAPS = 0;
                    sampleSolutionDisplay.setText(sampleSolutionDisplay.getText() + "\nInvalid handicap input -> Handicaps set to 0");
                }
            } else if (BOARD_SIZE == 19) {
                if (HANDICAPS < 0 || HANDICAPS > 9) {
                    HANDICAPS = 0;
                    sampleSolutionDisplay.setText(sampleSolutionDisplay.getText() + "\nInvalid handicap input -> Handicaps set to 0");
                }
            }
            terminalInfo("handicaps: " + HANDICAPS);
        } catch (NumberFormatException nfe) {
            HANDICAPS = 0;
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

        whiteTrapped.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", boardPane.heightProperty().multiply(TRAPPED_LABEL_MULTIPLIER).asString()
        ));
        blackTrapped.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", boardPane.heightProperty().multiply(TRAPPED_LABEL_MULTIPLIER).asString()
        ));
    }

    protected void initiateTimeRules(String byoyomiNumber, String byoyomiTime) {
        try {
            BYOYOMI_NUMBER = Integer.parseInt(byoyomiNumber);
            BYOYOMI_TIME = Integer.parseInt(byoyomiTime);
            if (BYOYOMI_NUMBER <= 0 || BYOYOMI_TIME < 30) {
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

        blackTimeLabel.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", boardPane.heightProperty().multiply(TIME_LABEL_MULTIPLIER).asString()
        ));

        whiteTimeLabel.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", boardPane.heightProperty().multiply(TIME_LABEL_MULTIPLIER).asString()
        ));

        timerBlack.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", boardPane.heightProperty().multiply(TIMER_LABEL_MULTIPLIER).asString()
        ));

        timerWhite.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", boardPane.heightProperty().multiply(TIMER_LABEL_MULTIPLIER).asString()
        ));

        timerBlack.textProperty().bind(playerHandler.getPlayerBlack().getTimer().timeProperty());
        timerWhite.textProperty().bind(playerHandler.getPlayerWhite().getTimer().timeProperty());
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
        bottomRegion.prefHeightProperty().bind(boardPane.heightProperty().multiply(BOTTOM_REGION_MULTIPLIER));
        topRegion.prefHeightProperty().bind(boardPane.heightProperty().multiply(TOP_REGION_MULTIPLIER));

        //set left, right and center (board) region to 60% of window height
        leftRegion.prefHeightProperty().bind(boardPane.heightProperty().multiply(REGION_MULTIPLIER));
        rightRegion.prefHeightProperty().bind(boardPane.heightProperty().multiply(REGION_MULTIPLIER));
        board.prefHeightProperty().bind(boardPane.heightProperty().multiply(REGION_MULTIPLIER));

        //bind center width to 60% of window height, so it's a square
        board.prefWidthProperty().bind(boardPane.heightProperty().multiply(REGION_MULTIPLIER));

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
        playerHandler.startTimer();

        //creating output file
        fileHandler = new FileHandler(this, PLAYER_BLACK.getName(), PLAYER_WHITE.getName(), BOARD_SIZE, KOMI, HANDICAPS, BYOYOMI_NUMBER, BYOYOMI_TIME);
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
            if (BOARD_SIZE == 19) {
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
                board.add(circle, col, row);
                circlesOfBoard[row][col] = circle;

                //make stones resizable and adjust X and Y properties
                circle.radiusProperty().bind(boardPane.heightProperty().multiply(0.8).divide(BOARD_SIZE).divide(4));
                circle.translateYProperty().bind(boardPane.heightProperty().multiply(0.7).divide(BOARD_SIZE * 2.4).multiply(-1));
                circle.translateXProperty().bind(boardPane.heightProperty().multiply(0.8).divide(BOARD_SIZE * 3.9).multiply(-1));

                //color for hovering
                final Color HOVER_BLACK = playerHandler.getPlayerBlack().getHoverColor();
                final Color HOVER_WHITE = playerHandler.getPlayerWhite().getHoverColor();

                //when the mouse is clicked the circle will be filled with a white or black colour depending on whose turn it is
                circle.setOnMouseClicked(e -> {
                    if (modePlay.isSelected())
                        if (circle.getFill().equals(HOVER_WHITE) || circle.getFill().equals(HOVER_BLACK))
                            setStone(circle);

                });

                //when the mouse is hovering over a transparent circle this circle is coloured white or black
                //side note: these colours are a little different from the white and black that a circle is filled with
                //           when clicked so that .equals will return false
                circle.setOnMouseEntered(e -> {
                    if (modePlay.isSelected()) {
                        if (circle.getFill() == TRANSPARENT || circle.getFill() == null) {
                            circle.setFill(playerHandler.getCurrentPlayer().getHoverColor());
                        }
                    }
                });

                //when the mouse is no longer hovering over the circle the colour is removed
                circle.setOnMouseExited(e -> {
                    if (modePlay.isSelected()) {
                        if (circle.getFill().equals(HOVER_WHITE) || circle.getFill().equals(HOVER_BLACK))
                            circle.setFill(TRANSPARENT);
                    }
                });
            }
        }
    }

    private void drawHandicaps() {
        int dif = BOARD_SIZE == 9 ? 2 : 3;
        int lowerValue = dif;
        int higherValue = BOARD_SIZE - dif - 1;
        int midValue = BOARD_SIZE / 2;

        if (HANDICAPS >= 1)
            gameHandler.addMove(lowerValue, higherValue, BLACK);

        if (HANDICAPS >= 2)
            gameHandler.addMove(higherValue, lowerValue, BLACK);

        if (HANDICAPS >= 3)
            gameHandler.addMove(higherValue, higherValue, BLACK);

        if (HANDICAPS >= 4)
            gameHandler.addMove(lowerValue, lowerValue, BLACK);

        if (HANDICAPS >= 5)
            gameHandler.addMove(midValue, midValue, BLACK);

        if (HANDICAPS >= 6)
            gameHandler.addMove(midValue, lowerValue, BLACK);

        if (HANDICAPS >= 7)
            gameHandler.addMove(midValue, higherValue, BLACK);

        if (HANDICAPS >= 8)
            gameHandler.addMove(lowerValue, midValue, BLACK);

        if (HANDICAPS == 9)
            gameHandler.addMove(higherValue, midValue, BLACK);
        drawStones();
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
            modeAndMoveDisplay.setText(playerHandler.getCurrentPlayer().getName() + " passed! - "
                                + playerHandler.getNextPlayer().getName() + "'s turn");

            int num = playerHandler.getCurrentPlayer().getColor() == BLACK ? 1 : 2;
            initiateByoyomiRules(num);

            playerHandler.moveMade();
            //fileHandler for passing (addMove with -1, -1)
            //fileControl.writeAction("passed");
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
            int num = playerHandler.getCurrentPlayer().getColor() == BLACK ? 2 : 1;
            try {
                switchToWinnerMask(num, 2);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            modeAndMoveDisplay.setText(playerHandler.getCurrentPlayer().getName() + " resigned! - "
                                + playerHandler.getNextPlayer().getName() + " won!");

            //TODO: adding logic for passing (add Move -2, -2);
            //fileControl.writeAction("resigned");
        });
    }

/*
    private void updateTimer1() {
        timerBlack.setPadding(new Insets(0, 0, 4, 0));
    }
*/

    private void initiateByoyomiRules(int playerNumber) {
        if (BYOYOMI_NUMBER != 0) {
            /*
            if (playerNumber == 1) {
                System.out.println("timer1: " + timer1.passedSlotSeconds() + "\nByoyomiTime: " + BYOYOMI_TIME);
                if (timer1.passedSlotSeconds() > BYOYOMI_TIME) {
                    int slots = timer1.passedSlotSeconds() / BYOYOMI_TIME;
                    blackByoyomi -= slots;
                    if (blackByoyomi < 0) {
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
            } else if (playerNumber == 2) {
                if (timer2.passedSlotSeconds() > BYOYOMI_TIME) {
                    int slots = timer2.passedSlotSeconds() / BYOYOMI_TIME;
                    whiteByoyomi -= slots;
                    if (whiteByoyomi < 0) {
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
        }*/
            if(playerHandler.getCurrentPlayer().getTimer().passedSlotSeconds() > BYOYOMI_TIME) {
                int slots = playerHandler.getCurrentPlayer().getTimer().passedSlotSeconds() / BYOYOMI_TIME;
                int currentByoyomi;
                if (playerHandler.getCurrentPlayer().getColor().equals(BLACK)) {
                    blackByoyomi -= slots;
                    if (blackByoyomi < 0)
                        blackTimeLabel.setText("No time left");
                    else
                        blackTimeLabel.setText(blackByoyomi + " time period(s) à " + BYOYOMI_TIME + " s");
                    currentByoyomi = blackByoyomi;
                } else {
                    whiteByoyomi -= slots;
                    if (whiteByoyomi < 0)
                        whiteTimeLabel.setText("No time left");
                    else
                        whiteTimeLabel.setText(whiteByoyomi + " time period(s) à " + BYOYOMI_TIME + " s");
                    currentByoyomi = whiteByoyomi;
                }
                System.out.println("curr: " + currentByoyomi);
                if (currentByoyomi < 0) {
                    int num = playerHandler.getNextPlayer().getColor().equals(BLACK) ? 1 : 2;
                    try {
                        switchToWinnerMask(num, 3);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    modeAndMoveDisplay.setText(playerHandler.getCurrentPlayer().getName() + " used all time slots. " + playerHandler.getNextPlayer().getName() + " won!");
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
        int row;
        int col;
        for (Node n : board.getChildren()) {
            if (n instanceof Circle && n.equals(c)) {
                row = GridPane.getRowIndex(n);
                col = GridPane.getColumnIndex(n);
                fileHandler.write((row-1), ALPHABET[col-1], "");
                /*
                terminalInfo("Stone (" + lastColor + ") placed at: " + row + ALPHABET[col - 1]);
                c.setFill(lastColor);
                if(game.executeCommand(new PlaceStoneCommand(game.getBoard(),row-1, col-1, lastColor))){
                    drawStones();
                    modeAndMoveDisplay.setText("This is suicide. Please select another position");
                    return;
                }*/

                Color current = playerHandler.getCurrentPlayer().getColor();
                terminalInfo("Stone (" + current + ") placed at: " + row + ALPHABET[col - 1] + " by " + playerHandler.getCurrentPlayer().getName());
                c.setFill(current);
                if(gameHandler.addMove(row-1, col-1, current)){
                    drawStones();
                    modeAndMoveDisplay.setText("This is suicide. Please select another position");
                    return;
                }

                modeAndMoveDisplay.setText(playerHandler.getNextPlayer().getName() + "'s turn!");

                playerHandler.moveMade();
                int num = playerHandler.getCurrentPlayer().getColor() == BLACK ? 1 : 2;
                initiateByoyomiRules(num);
                break;
            }
            //   terminalInfo("Error: System was unable to located circle!");
        }
        drawStones();
    }

    private void drawStones() {
        Color[][] boardToDraw = gameHandler.getBoard().getBoard();
        StringBuilder printBoard = new StringBuilder();
        for(int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                circlesOfBoard[r + 1][c + 1].setFill(boardToDraw[r][c] == null ? TRANSPARENT : boardToDraw[r][c] == BLACK ? BLACK : WHITE);
                Color toCompare = (Color) circlesOfBoard[r + 1][c + 1].getFill();
                printBoard.append(TRANSPARENT == toCompare || null == toCompare ? "empty\t" :
                        toCompare == BLACK ? "BLACK\t" :
                                toCompare == WHITE ? "WHITE\t" :
                                        toCompare + "\t");
            }
            printBoard.append("\n");
        }
        terminalInfo(printBoard.toString());
    }
/*
    public void deleteStoneGroup(StoneGroup toDelete) {
        if (toDelete.getColour() == WHITE) {
            blackTrappedStones += toDelete.getPosition().size();
            displayTrappedStone(blackTrappedStones, blackTrapped);
        } else {
            whiteTrappedStones += toDelete.getPosition().size();
            displayTrappedStone(whiteTrappedStones, whiteTrapped);
        }

        //finds the circle for every position of stoneGroup toDelete and sets the visibility to TRANSPARENT
        for (Position p : toDelete.getPosition()) {
            Circle c = circlesOfBoard[p.col() + 1][p.row() + 1];
            if (c.getFill() == TRANSPARENT)
                terminalInfo("Error: no stone found at " + (indexToNum(p.row() + 1)) + ALPHABET[p.col()]);
            else {
                c.setFill(TRANSPARENT);
                terminalInfo("Stone deleted: " + (indexToNum(p.row() + 1)) + ALPHABET[p.col()]);
            }
        }
    }
*/

    //TODO: Implementing calculate score accurately!
    private int calculateScore(char playerColor, char enemyColor, char[][] board) {
        int score = 0;
        int territory = 0;
        int captured = 0;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                // Check if the point is a player's stone
                if (board[i][j] == playerColor) {
                    score++;
                }
                // Check if the point is part of player's territory
                else if (board[i][j] == ' ' && isTerritory(i, j, playerColor, board)) {
                    territory++;
                }
                // Check if the point is a captured enemy's stone
                else if (board[i][j] == enemyColor && isCaptured(i, j, board)) {
                    captured++;
                }
            }
        }

        return score + territory + captured;
    }

    private boolean isTerritory(int i, int j, char playerColor, char[][] board) {
        return checkSurrounding(i, j, playerColor, board, new boolean[board.length][board[0].length]);
    }

    private boolean isCaptured(int i, int j, char[][] board) {
        return checkSurrounding(i, j, board[i][j], board, new boolean[board.length][board[0].length]);
    }

    private boolean checkSurrounding(int i, int j, char color, char[][] board, boolean[][] visited) {
        if (i < 0 || j < 0 || i >= board.length || j >= board[0].length) {
            return false;
        }

        if (visited[i][j]) {
            return true;
        }

        visited[i][j] = true;

        if (board[i][j] == ' ') {
            return true;
        }

        if (board[i][j] != color) {
            return false;
        }

        boolean result = true;
        result &= checkSurrounding(i - 1, j, color, board, visited);
        result &= checkSurrounding(i + 1, j, color, board, visited);
        result &= checkSurrounding(i, j - 1, color, board, visited);
        result &= checkSurrounding(i, j + 1, color, board, visited);

        return result;
    }

    private void switchToWinnerMask(int player, int reasonForWinning) throws IOException {
        //reason for winning 1 - points(2xconsecutive passing), 2 - resigned, 3 - byoyomi
        if ((player == 1 || player == 2) && (reasonForWinning >= 1 && reasonForWinning <= 3)) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/winnerMaskGUI.fxml"));
            Parent root = loader.load();

            winnerMaskController winnerMask = loader.getController();
            winnerMask.setSize(boardPane.getWidth(), boardPane.getHeight());
            winnerMask.setReasonForWinning(reasonForWinning);

            if (player == 1) {
                winnerMask.initiateDisplay(pl1.getText(), pl2.getText(), gameHandler.getTerritoryScore(BLACK) + blackTrappedStones, blackTrappedStones, "Handicaps: ", HANDICAPS,
                        BYOYOMI_NUMBER, blackByoyomi, BYOYOMI_TIME);
                terminalInfo("Black won... \n[log end]");
            } else {
                winnerMask.initiateDisplay(pl2.getText(), pl1.getText(), gameHandler.getTerritoryScore(WHITE) + whiteTrappedStones, whiteTrappedStones, "Komi: ", KOMI,
                        BYOYOMI_NUMBER, whiteByoyomi, BYOYOMI_TIME);
                terminalInfo("White won... \n[log end]");
            }

            Node source = topRegion.getTop();
            Stage stage = (Stage) source.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setMinWidth(600);
            stage.setMinHeight(580);
            stage.centerOnScreen();
            stage.show();
        }
    }

    /*
      ================================================================================================================

                                            other system functions

      ================================================================================================================
     */

    private void terminalInfo(String data) {
        if(LOGGING)
            System.out.println(data);
    }

    private int indexToNum(int row) {
        return (BOARD_SIZE + 1) - row;
    }
}