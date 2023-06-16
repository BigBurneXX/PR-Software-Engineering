package com.example.go_gruppe1.controller;

import com.example.go_gruppe1.model.command.GameHandler;
import com.example.go_gruppe1.model.file.FileHandler;
import com.example.go_gruppe1.model.Move;
import com.example.go_gruppe1.model.player.PlayerHandler;
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
    private Label plBlack, plWhite, blackTrapped, whiteTrapped, timerBlack, timerWhite, blackTimeLabel, whiteTimeLabel, title;
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

    /*
      ----------------------------------------------------------------------------------------------------------------
                                        constants - GUI
      ----------------------------------------------------------------------------------------------------------------
     */

    private final int MIN_WIDTH = 600;
    private final int MIN_HEIGHT = 580;
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
    private int blackByoyomi = 0, whiteByoyomi = 0;
    private boolean doublePassed;

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

    //should potentially be in FileHandler?
    public void switchToNewGame(String player1Name, String player2Name, String komi, String handicaps, int boardSize,
                                List<Move> moves, String byoyomiNumberOfTimes, String byoyomiTimeLimit) throws IOException {
        terminalInfo(player1Name + player2Name + komi + handicaps + boardSize);
        initiateDisplay(player1Name, player2Name, komi, handicaps, boardSize, byoyomiNumberOfTimes, byoyomiTimeLimit);
        Color currentColor = BLACK;
        for (Move m : moves) {
            if(m.col() == 'p') {
                passButton.getOnMouseClicked();
            } else if (m.col() == 'r') {
                resignButton.getOnMouseClicked();
            } else {
                int col = new String(ALPHABET).indexOf(m.col());
                fileHandler.write((m.row() - 1), ALPHABET[col], m.text());
                setSampleSolutionDisplay(m.text());
                terminalInfo("Stone (" + (currentColor == BLACK ? "BLACK" : "WHITE") + ") placed at: " + m.row() + ALPHABET[col]);
                circlesOfBoard[col + 1][m.row() + 1].setFill(currentColor);
                gameHandler.addMove(m.row(), col, currentColor);
                currentColor = (currentColor == BLACK ? WHITE : BLACK);
            }
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

        bindFont(modeAndMoveDisplay, 0.025);
        modeAndMoveDisplay.setText(playerHandler.getCurrentPlayer().name() + "'s turn!");
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
        bindFont(modeAndMoveDisplay, 0.025);
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
        boardPane.setMinSize(MIN_WIDTH, MIN_HEIGHT);
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
    protected void initiateDisplay(String player1Name, String player2Name, String komi, String handicaps, int boardSize,
                                   String byoyomiNumber, String byoyomiTime) {
        BOARD_SIZE = boardSize;
        terminalInfo("starting a new game\nboard size set to: " + BOARD_SIZE);
        gameHandler = new GameHandler(BOARD_SIZE);
        displayKomi(komi);
        displayHandicaps(handicaps);
        displayTrappedStone(0, blackTrapped);
        displayTrappedStone(0, whiteTrapped);
        BYOYOMI_NUMBER = Integer.parseInt(byoyomiNumber);
        BYOYOMI_TIME = Integer.parseInt(byoyomiTime);
        initiatePlayers(player1Name, player2Name);
        initiateTimeRules(byoyomiNumber, byoyomiTime);
        onModePlayClick();
        modePlay.setSelected(true);
        circlesOfBoard = new Circle[BOARD_SIZE + 1][BOARD_SIZE + 1];
        drawBoard();

        bindFont(title, 0.08);
    }

    private void initiatePlayers(String p1, String p2) {
        String playerBlack = p1.isEmpty() ? "Player 1" : p1;
        String playerWhite = p2.isEmpty() ? "Player 2" : p2;
        playerHandler = new PlayerHandler(playerBlack, playerWhite);
        playerHandler.setLogging(true);
        //creating output file
        fileHandler = new FileHandler(this, playerBlack, playerWhite, BOARD_SIZE, KOMI, HANDICAPS, BYOYOMI_NUMBER, BYOYOMI_TIME);

        displayPlayerNames(playerBlack, playerWhite);
    }

    private void displayPlayerNames(String playerBlack, String playerWhite){
        plBlack.setText(playerBlack + " (Black)");
        plWhite.setText(playerWhite + " (White)");
        terminalInfo("player1 set to: " + playerBlack);
        terminalInfo("player2 set to: " + playerWhite);

        bindFont(plBlack, PLAYER_LABEL_MULTIPLIER);
        bindFont(plWhite, PLAYER_LABEL_MULTIPLIER);
    }

    private void displayKomi(String komiAdvantage) {
        KOMI = Double.parseDouble(komiAdvantage);
    }

    private void displayHandicaps(String handicaps) {
        //the spinner of the inputMask only allows valid inputs
        //therefore the values don't need to be checked
        HANDICAPS = Integer.parseInt(handicaps);
        terminalInfo("handicaps: " + HANDICAPS);
    }

    private void displayTrappedStone(int numberTrapped, Label trappedLabel) {
        trappedLabel.setText("Trapped: " + numberTrapped);
        terminalInfo(trappedLabel.getId() + " set to " + numberTrapped);

        bindFont(whiteTrapped, TRAPPED_LABEL_MULTIPLIER);
        bindFont(blackTrapped, TRAPPED_LABEL_MULTIPLIER);
    }

    protected void initiateTimeRules(String byoyomiNumber, String byoyomiTime) {
        try {
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

        bindFont(blackTimeLabel, TIME_LABEL_MULTIPLIER);
        bindFont(whiteTimeLabel, TIME_LABEL_MULTIPLIER);
        bindFont(timerBlack, TIMER_LABEL_MULTIPLIER);
        bindFont(timerWhite, TIMER_LABEL_MULTIPLIER);

        timerBlack.textProperty().bind(playerHandler.getPlayerBlack().timer().timeProperty());
        timerWhite.textProperty().bind(playerHandler.getPlayerWhite().timer().timeProperty());
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
                bindFontIntensive(rightNumberCell, sizeBinding);
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
                bindFontIntensive(letter, sizeBinding);
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
                final Color HOVER_BLACK = playerHandler.getPlayerBlack().hoverColor();
                final Color HOVER_WHITE = playerHandler.getPlayerWhite().hoverColor();

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
                            circle.setFill(playerHandler.getCurrentPlayer().hoverColor());
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
        playerHandler.changePlayer();
        modeAndMoveDisplay.setText(playerHandler.getCurrentPlayer().name() + "'s turn");
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
        bindFontIntensive(passButton, 0.03);
        passButton.setFocusTraversable(false);

        //pass logic
        passButton.setOnMouseClicked(e -> {
            System.out.println("nice");
            if(doublePassed){
                System.out.println("game over!!");
            //if(modeAndMoveDisplay.getText().equals(playerHandler.getNextPlayer().getName() + " passed! - "
            //        + playerHandler.getCurrentPlayer().getName() + "'s turn")) {
                long blackPoints = gameHandler.getTerritoryScore(BLACK) + gameHandler.getBoard().getBlackTrapped();
                long whitePoints = (long) (gameHandler.getTerritoryScore(WHITE) + gameHandler.getBoard().getWhiteTrapped() + KOMI);
                if(blackPoints > whitePoints) {
                    try {
                        switchToWinnerMask(1, 1);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                } else if(whitePoints > blackPoints) {
                    try {
                        switchToWinnerMask(2, 1);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                } /*else {
                    try {
                        switchToWinnerMask(3, 1);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }*/
            }
            doublePassed = true;
            modeAndMoveDisplay.setText(playerHandler.getCurrentPlayer().name() + " passed! - "
                                + playerHandler.getNextPlayer().name() + "'s turn");

            initiateByoyomiRules();

            playerHandler.moveMade();
            fileHandler.pass();
        });
    }

    private void drawResignButton() {
        resignButton.prefWidthProperty().bind(boardPane.widthProperty().multiply(0.1));
        bindFontIntensive(resignButton, 0.03);
        resignButton.setFocusTraversable(false);

        //resign logic
        resignButton.setOnMouseClicked(e -> {
            int num = playerHandler.getCurrentPlayer().color() == BLACK ? 2 : 1;
            try {
                switchToWinnerMask(num, 2);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            modeAndMoveDisplay.setText(playerHandler.getCurrentPlayer().name() + " resigned! - "
                                + playerHandler.getNextPlayer().name() + " won!");

            fileHandler.resign();
        });
    }

    private void initiateByoyomiRules() {
        if (BYOYOMI_NUMBER != 0) {
            playerHandler.getCurrentPlayer().timer().passedSlotSeconds();
            System.out.println(playerHandler.getCurrentPlayer().timer().getPassedSeconds());
            if(playerHandler.getCurrentPlayer().timer().getPassedSeconds() > BYOYOMI_TIME) {
                int slots = playerHandler.getCurrentPlayer().timer().getPassedSeconds() / BYOYOMI_TIME;
                System.out.println("slots: " + slots);
                int currentByoyomi;
                if (playerHandler.getCurrentPlayer().color().equals(BLACK)) {
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
                    int num = playerHandler.getNextPlayer().color().equals(BLACK) ? 1 : 2;
                    try {
                        switchToWinnerMask(num, 3);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    modeAndMoveDisplay.setText(playerHandler.getCurrentPlayer().name() + " used all time slots. "
                            + playerHandler.getNextPlayer().name() + " won!");
                }
            }
        }
    }

    /*
      ================================================================================================================

                                            other helper functions

      ================================================================================================================
     */

    private void setStone(Circle c) {
        int row;
        int col;
        for (Node n : board.getChildren()) {
            if (n instanceof Circle && n.equals(c)) {
                row = GridPane.getRowIndex(n);
                col = GridPane.getColumnIndex(n);
                fileHandler.write((row-1), ALPHABET[col-1], "");

                Color current = playerHandler.getCurrentPlayer().color();
                terminalInfo("Stone (" + current + ") placed at: " + row + ALPHABET[col - 1] + " by "
                        + playerHandler.getCurrentPlayer().name());
                c.setFill(current);
                if(gameHandler.addMove(row-1, col-1, current)){
                    drawStones();
                    modeAndMoveDisplay.setText("This is suicide. Please select another position");
                    return;
                }

                modeAndMoveDisplay.setText(playerHandler.getNextPlayer().name() + "'s turn!");

                playerHandler.moveMade();
                initiateByoyomiRules();
                break;
            }
            //   terminalInfo("Error: System was unable to located circle!");
        }
        doublePassed = false;
        drawStones();
        blackTrapped.setText("Trapped: " + gameHandler.getBoard().getBlackTrapped());
        whiteTrapped.setText("Trapped: " + gameHandler.getBoard().getWhiteTrapped());
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

    private void switchToWinnerMask(int player, int reasonForWinning) throws IOException {
        //reason for winning 1 - points(2x consecutive passing), 2 - resigned, 3 - byoyomi
        //player is 3 when there's a draw
        if ((player == 1 || player == 2 || player == 3 ) && (reasonForWinning >= 1 && reasonForWinning <= 3)) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/winnerMaskGUI.fxml"));
            Parent root = loader.load();

            winnerMaskController winnerMask = loader.getController();
            winnerMask.setSize(boardPane.getWidth(), boardPane.getHeight());
            winnerMask.setReasonForWinning(reasonForWinning);

            if(player == 1) {
                terminalInfo("Black won... \n[log end]");
                winnerMask.initiateDisplay(plBlack.getText(), plWhite.getText(), gameHandler.getTerritoryScore(BLACK) + gameHandler.getBoard().getBlackTrapped(), gameHandler.getBoard().getBlackTrapped(), "Handicaps: ", HANDICAPS,
                        BYOYOMI_NUMBER, blackByoyomi, BYOYOMI_TIME);
            } else if(player == 2){
                terminalInfo("White won... \n[log end]");
                winnerMask.initiateDisplay(plWhite.getText(), plBlack.getText(), (long) (gameHandler.getTerritoryScore(WHITE) + gameHandler.getBoard().getWhiteTrapped() + KOMI), gameHandler.getBoard().getWhiteTrapped(), "Komi: ", KOMI,
                        BYOYOMI_NUMBER, whiteByoyomi, BYOYOMI_TIME);
            } else {
                terminalInfo("Draw... \n[log end]");
                winnerMask.setName("Draw", "Draw");
            }

            Node source = topRegion.getTop();
            Stage stage = (Stage) source.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setMinWidth(MIN_WIDTH);
            stage.setMinHeight(MIN_HEIGHT);
            stage.centerOnScreen();
        }
    }

    /*
      ================================================================================================================

                                            other system functions

      ================================================================================================================
     */

    private void bindFont(Node n, double multiplier){
        n.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", boardPane.heightProperty().multiply(multiplier).asString()
        ));
    }

    private void bindFontIntensive(Node n, double multiplier){
        n.styleProperty().bind(Bindings.concat(
                "-fx-text-fill: #483C32; ",
                "-fx-font-weight: bold; ",
                "-fx-font-size: ", board.heightProperty().multiply(multiplier).asString()
        ));
    }

    private void terminalInfo(String data) {
        if(LOGGING)
            System.out.println(data);
    }

    private int indexToNum(int row) {
        return (BOARD_SIZE + 1) - row;
    }
}