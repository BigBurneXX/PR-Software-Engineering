package com.example.go_gruppe1.controller;

import com.example.go_gruppe1.model.command.GameHandler;
import com.example.go_gruppe1.model.file.FileData;
import com.example.go_gruppe1.model.file.FileHandler;
import com.example.go_gruppe1.model.file.Move;
import com.example.go_gruppe1.model.player.Player;
import com.example.go_gruppe1.model.player.PlayerHandler;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
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
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;

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
    private Label plBlack, plWhite, blackTrapped, whiteTrapped, timerBlack, timerWhite, blackTimeLabel, whiteTimeLabel, title;
    @FXML
    private Button passButton, resignButton;
    @FXML
    private RadioMenuItem modePlay;

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

    private final char[] ALPHABET = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T'};

    /*
      ----------------------------------------------------------------------------------------------------------------
                                        constants - GUI
      ----------------------------------------------------------------------------------------------------------------
     */

    private final int MIN_WIDTH = 600;
    private final int MIN_HEIGHT = 580;
    private final double MOVE_AND_MODE_DISPLAY_MULTIPLIER = 0.025;
    private static final double STROKE_WIDTH = 2.0;

    /*
      ----------------------------------------------------------------------------------------------------------------
                                        global variables
      ----------------------------------------------------------------------------------------------------------------
     */
    private Circle[][] circlesOfBoard;
    private int boardSize;
    private int handicaps;
    private double komi;
    private boolean doublePassed;
    private int currentSelectionRow = 1;
    private int currentSelectionCol = 1;

    /*
      ================================================================================================================

                                       file onActionMethods and helper methods

      ================================================================================================================
     */

    @FXML
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
    public void onOpenFileClick(){
        terminalInfo("Loading file... \n[log end]");
        loadGame(fileHandler.open());
    }

    private void loadGame(FileData fileData){
        if(fileData == null){
            modeAndMoveDisplay.setText("no file selected");
            return;
        }
        initiateDisplay(fileData.player1Name(), fileData.player2Name(), fileData.komi(), fileData.handicaps(),
                fileData.boardSize(), fileData.byoyomiOverruns(), fileData.byoyomiTimeLimit());

        Color currentColor = BLACK;

        for (Move m : fileData.moves()) {
            char colLetter = m.col();

            if(colLetter == 'p')
                passButton.getOnMouseClicked().handle(null);
            else if (colLetter == 'r')
                resignButton.getOnMouseClicked().handle(null);
            else {
                System.out.print("m.row() " + m.row() + ", ");
                System.out.println("m.col() " + colLetter);
                int row = switchNumAndIndex(m.row());
                System.out.print("row: " + row + ", ");
                int colIndex = Arrays.binarySearch(ALPHABET, colLetter);
                System.out.println("col: " + colIndex);
                String text = m.text();

                terminalInfo("Stone (" + (currentColor.equals(BLACK) ? "BLACK" : "WHITE") +
                        ") placed at: " + m.row() + colLetter);

                fileHandler.write(m.row(), colLetter, text);
                gameHandler.addMove(row, colIndex, currentColor, text);

                circlesOfBoard[colIndex + 1][row + 1].setFill(currentColor);
                //setSampleSolutionDisplay(text);
                currentColor = (currentColor == BLACK ? WHITE : BLACK);
            }
        }
        if(currentColor.equals(BLACK))
            playerHandler.changePlayer();
        drawStones();
    }

    @FXML
    public void onExitGameClick() {
        terminalInfo("Exiting file... \n[log end]");
        Platform.exit();
    }

    /*
      ================================================================================================================

                                        mode onActionMethods and helper methods

      ================================================================================================================
     */

    @FXML
    public void onModePlayClick() {
        terminalInfo("Play mode activated!");
        changeModeVisibility(true);

        modeAndMoveDisplay.setText(playerHandler.getCurrentPlayer().getName() + "'s turn!");
        bindFont(modeAndMoveDisplay, MOVE_AND_MODE_DISPLAY_MULTIPLIER);
    }

    @FXML
    public void onModeNavigateClick() {
        terminalInfo("Navigation mode activated!");
        changeModeVisibility(false);

        modeAndMoveDisplay.setText("Navigation mode activated");
        bindFont(modeAndMoveDisplay, MOVE_AND_MODE_DISPLAY_MULTIPLIER);
    }

    private void changeModeVisibility(boolean isPlay){
        leftArrow.setVisible(!isPlay);
        rightArrow.setVisible(!isPlay);

        passButton.setVisible(isPlay);
        resignButton.setVisible(isPlay);
    }

    /*
      ================================================================================================================

                                            button onActionMethods

      ================================================================================================================
     */

    @FXML
    public void onPassButtonClick(){
        fileHandler.pass();

        if(doublePassed){
            //switching to winnerMask
            Player current = playerHandler.getCurrentPlayer();
            Player next = playerHandler.getNextPlayer();
            double currentPlayerPoints = gameHandler.getTerritoryScore(current.getColor()) +
                                            gameHandler.getBoard().getTrapped(current.getColor());

            double nextPlayerPoints = gameHandler.getTerritoryScore(next.getColor()) +
                                            gameHandler.getBoard().getTrapped(next.getColor());

            //result > 0 --> currentPlayer won
            //result = 0 --> draw
            //result < 0 --> nextPlayer won
            double result = currentPlayerPoints - nextPlayerPoints;
            result = (current.getColor().equals(WHITE) ? result + komi : result - komi);

            switch (Double.compare(result, 0)) {
                case 1 -> switchToWinnerMask(1);
                case -1 -> {
                    playerHandler.changePlayer();
                    switchToWinnerMask(1);
                }
                default -> switchToWinnerMask(4);
            }
        }
        doublePassed = true;
        modeAndMoveDisplay.setText(playerHandler.getCurrentPlayer().getName() + " passed! - "
                + playerHandler.getNextPlayer().getName() + "'s turn");

        if(playerHandler.checkByoyomi())
            switchToWinnerMask(3);

        playerHandler.moveMade();
    }

    @FXML
    public void onResignButtonClick(){
        //switch player for the other player wins
        playerHandler.changePlayer();

        fileHandler.resign();
        switchToWinnerMask(2);
    }

    /*
      ================================================================================================================

                                            getter and setter

      ================================================================================================================
     */

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
    protected void initiateDisplay(String player1Name, String player2Name, double komi, int handicaps, int boardSize,
                                   int byoyomiOverruns, int byoyomiTimeLimit) {

        terminalInfo("starting a new game..." +
                "\nBoard size: " + boardSize +
                "\nKomi: " + komi +
                "\nHandicaps: " + handicaps +
                "\nByoyomi overruns: " + byoyomiOverruns +
                "\nByoyomi time limit: " + byoyomiTimeLimit +
                "\nPlayer 1 (BLACK) Name: " + player1Name +
                "\nPlayer 2 (WHITE) Name: " + player2Name);

        this.komi = komi;
        this.handicaps = handicaps;
        this.boardSize = boardSize;

        initiateHandlers(player1Name, player2Name, byoyomiOverruns, byoyomiTimeLimit);
        displayPlayerNames(player1Name, player2Name);
        displayTrapped();
        sampleSolutionDisplay.textProperty().bind(gameHandler.getDescription());

        onModePlayClick();
        modePlay.setSelected(true);

        drawBoard();

        bindFont(title, 0.08);
    }

    private void initiateHandlers(String playerBlack, String playerWhite, int byoyomiOverruns, int byoyomiTimeLimit) {
        gameHandler = new GameHandler(boardSize);
        playerHandler = (byoyomiOverruns == 0) ? new PlayerHandler(playerBlack, playerWhite) :
                new PlayerHandler(playerBlack, playerWhite, byoyomiOverruns, byoyomiTimeLimit);
        initiateTimeRules(byoyomiOverruns == 0);
        fileHandler = new FileHandler(playerBlack, playerWhite, boardSize, komi, handicaps, byoyomiOverruns, byoyomiTimeLimit);
    }

    private void initiateTimeRules(boolean notActive) {
        if (notActive) {
            blackTimeLabel.setVisible(false);
            timerBlack.setVisible(false);
            whiteTimeLabel.setVisible(false);
            timerWhite.setVisible(false);
            terminalInfo("Byoyomi deactivated!");
            return;
        }

        double timeLabelMultiplier = 0.023;
        bindFont(blackTimeLabel, timeLabelMultiplier);
        bindFont(whiteTimeLabel, timeLabelMultiplier);

        double timerMultiplier = 0.026;
        bindFont(timerBlack, timerMultiplier);
        bindFont(timerWhite, timerMultiplier);

        blackTimeLabel.textProperty().bind(playerHandler.getPlayerBlack().getTimeLabelText());
        whiteTimeLabel.textProperty().bind(playerHandler.getPlayerWhite().getTimeLabelText());
        timerBlack.textProperty().bind(playerHandler.getPlayerBlack().getTimer().getTimeProperty());
        timerWhite.textProperty().bind(playerHandler.getPlayerWhite().getTimer().getTimeProperty());
    }

    private void displayPlayerNames(String playerBlack, String playerWhite){
        plBlack.setText(playerBlack + " (Black)");
        plWhite.setText(playerWhite + " (White)");

        double PLAYER_LABEL_MULTIPLIER = 0.04;
        bindFont(plBlack, PLAYER_LABEL_MULTIPLIER);
        bindFont(plWhite, PLAYER_LABEL_MULTIPLIER);
    }

    private void displayTrapped() {
        blackTrapped.setText("Trapped: 0");
        whiteTrapped.setText("Trapped: 0");

        double TRAPPED_LABEL_MULTIPLIER = 0.026;
        bindFont(whiteTrapped, TRAPPED_LABEL_MULTIPLIER);
        bindFont(blackTrapped, TRAPPED_LABEL_MULTIPLIER);
    }

    /*
      ----------------------------------------------------------------------------------------------------------------
                                        draw board and all elements in it
      ----------------------------------------------------------------------------------------------------------------
     */

    private void drawBoard() {
        //bind height and width property
        setBoardProperties();

        //create grid
        createGrid();

        //add color to board
        addColorToBoard();

        //adding circles to all potential locations on the board
        addCirclesToBoard();

        //add board labeling
        addBoardLabelling();

        //draw all (if any) handicaps
        drawHandicaps();

        //add all logic and binding concerning the navigation arrows
        addArrowLogic(leftArrow, true);
        addArrowLogic(rightArrow, false);

        //add binding for all buttons
        addButtonBinding(passButton);
        addButtonBinding(resignButton);

        //immediately start the timer for the first player (black if no handicaps were placed, white if handicaps were placed)
        playerHandler.startTimer();

        //keyboard logic?
        // Set the board to be focusable
        board.setFocusTraversable(true);

        // Request focus on the board
        board.requestFocus();

        // Set up keyboard controls
        setupKeyboardControls();
    }

    private void setBoardProperties(){
        //set padding, so stones are not covered by top region
        board.setPadding(new Insets(20, 0, 0, 0));

        //bind bottom region to 10% of window height
        bottomRegion.prefHeightProperty().bind(boardPane.heightProperty().multiply(0.1));

        //bind upper region to 20% of window height
        topRegion.prefHeightProperty().bind(boardPane.heightProperty().multiply(0.2));


        //set left, right and center (board) region to 60% of window height
        double regionMultiplier = 0.7;
        leftRegion.prefHeightProperty().bind(boardPane.heightProperty().multiply(regionMultiplier));
        rightRegion.prefHeightProperty().bind(boardPane.heightProperty().multiply(regionMultiplier));
        board.prefHeightProperty().bind(boardPane.heightProperty().multiply(regionMultiplier));

        //bind center width to 60% of window height, so it's a square
        board.prefWidthProperty().bind(boardPane.heightProperty().multiply(regionMultiplier));

        //bind left and right width to remaining width of the window of what's left from taking 60% of the height
        leftRegion.prefWidthProperty().bind(boardPane.widthProperty().subtract(board.prefWidthProperty()).divide(2));
        rightRegion.prefWidthProperty().bind(boardPane.widthProperty().subtract(board.prefWidthProperty()).divide(2));
    }

    private void createGrid(){
        board.getChildren().clear();
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
    }

    private void addColorToBoard(){
        for (int col = 1; col < boardSize; col++) {
            for (int row = 1; row < boardSize; row++) {
                Pane cell = new Pane();
                cell.setStyle("-fx-background-color:  #C4A484; -fx-border-color: #483C32");
                cell.toBack();
                board.add(cell, col, row);
            }
        }
    }

    private void addCirclesToBoard() {
        circlesOfBoard = new Circle[boardSize + 1][boardSize + 1];

        for (int row = 1; row <= boardSize; row++) {
            for (int col = 1; col <= boardSize; col++) {
                Circle circle = new Circle(10, TRANSPARENT);
                Rectangle rectangle = new Rectangle(10, 10, TRANSPARENT);

                board.add(circle, col, row);
                board.add(rectangle, col, row);

                circlesOfBoard[row][col] = circle;
                rectangle.toFront();

                //make stones resizable and adjust X and Y properties
                circle.radiusProperty().bind(boardPane.heightProperty().multiply(0.8).divide(boardSize).divide(4));
                circle.translateYProperty().bind(boardPane.heightProperty().multiply(0.7).divide(boardSize * 2.4).multiply(-1));
                circle.translateXProperty().bind(boardPane.heightProperty().multiply(0.8).divide(boardSize * 3.9).multiply(-1));

                //make highlighting resizable
                DoubleBinding rectangleHeightProperty = boardPane.heightProperty().multiply(0.8).divide(boardSize).divide(4);
                DoubleBinding rectangleWidthProperty =  boardPane.heightProperty().multiply(0.8).divide(boardSize).divide(4);
                DoubleBinding rectangleTranslateXProperty = circle.centerXProperty().subtract(rectangle.widthProperty().divide(2));
                DoubleBinding rectangleTranslateYProperty = circle.centerYProperty().subtract(rectangle.heightProperty().
                        multiply(6).divide(4));

                rectangle.heightProperty().bind(rectangleHeightProperty);
                rectangle.widthProperty().bind(rectangleWidthProperty);
                rectangle.translateXProperty().bind(rectangleTranslateXProperty);
                rectangle.translateYProperty().bind(rectangleTranslateYProperty);


                //color for hovering
                final Color HOVER_BLACK = playerHandler.getPlayerBlack().getHoverColor();
                final Color HOVER_WHITE = playerHandler.getPlayerWhite().getHoverColor();

                //when the mouse is clicked the circle will be filled with a white or black colour depending on whose turn it is
                circle.setOnMouseClicked(e -> onMouseClicked(e, rectangle, circle));
                rectangle.setOnMouseClicked(e -> onMouseClicked(e, rectangle, circle));

                //when the mouse is hovering over a transparent circle this circle is coloured white or black
                //Note: the hover color is 30% transparent
                circle.setOnMouseEntered(e -> {
                    if (modePlay.isSelected()) {
                        if (circle.getFill() == TRANSPARENT || circle.getFill() == null) {
                            circle.setFill(playerHandler.getCurrentPlayer().getHoverColor());
                            circle.toFront();
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

    private void onMouseClicked(MouseEvent event, Rectangle rectangle, Circle circle){
        final Color HOVER_BLACK = playerHandler.getPlayerBlack().getHoverColor();
        final Color HOVER_WHITE = playerHandler.getPlayerWhite().getHoverColor();

        if(event.getButton() == MouseButton.SECONDARY){
            if(!rectangle.getFill().equals(TRANSPARENT))
                rectangle.setFill(TRANSPARENT);
            else if(circle.getFill().equals(WHITE))
                rectangle.setFill(BLUE);
            else
                rectangle.setFill((circle.getFill().equals(BLACK) ||
                        playerHandler.getCurrentPlayer().getHoverColor().equals(HOVER_BLACK)) ? YELLOW : BLUE);
            rectangle.toFront();
        } else if (modePlay.isSelected()) {
            if (circle.getFill().equals(HOVER_WHITE) || circle.getFill().equals(HOVER_BLACK)) {
                setStone(circle);
                circle.toFront();
            }
        }
    }

    private void setStone(Circle c) {
        int row = GridPane.getRowIndex(c);
        int col = GridPane.getColumnIndex(c);
        fileHandler.write(switchNumAndIndex(row - 1), ALPHABET[col - 1], "");

        Color current = playerHandler.getCurrentPlayer().getColor();
        terminalInfo("Stone (" + current + ") placed at: " + row + ALPHABET[col - 1] + " by "
                + playerHandler.getCurrentPlayer().getName());
        c.setFill(current);

        int isValidMove = gameHandler.addMove(row-1, col-1, current);
        if (isValidMove != 0) {
            drawStones();
            modeAndMoveDisplay.setText(isValidMove == 1 ? "This is suicide. Please select another position" :
                    "This violates the ko logic. Please select another position");
            return;
        }

        modeAndMoveDisplay.setText(playerHandler.getNextPlayer().getName() + "'s turn!");

        if (playerHandler.checkByoyomi()) {
            switchToWinnerMask(3);
        }

        playerHandler.moveMade();

        doublePassed = false;
        drawStones();

        // text property binding maybe??
        blackTrapped.setText("Trapped: " + gameHandler.getBoard().getTrapped(BLACK));
        whiteTrapped.setText("Trapped: " + gameHandler.getBoard().getTrapped(WHITE));
    }

    private void addBoardLabelling() {
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

            double sizeBinding = 0.035;
            int padding = 5;
            if (boardSize == 19) {
                sizeBinding = 0.02;
                padding = 4;
            }

            //right labelling
            if (i != 0) {
                Label rightNumberCell = new Label();
                rightNumberCell.setText(String.valueOf(boardSize - i + 1));
                rightNumberCell.setCenterShape(true);
                bindFontIntensive(rightNumberCell, sizeBinding);
                rightNumberCell.setPadding(new Insets(0, padding, 0, 0));
                GridPane.setHalignment(rightNumberCell, HPos.RIGHT);
                GridPane.setValignment(rightNumberCell, VPos.TOP);
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

            //bottom labelling
            if (i < boardSize) {
                Label letter = new Label();
                letter.setText(String.valueOf(ALPHABET[i]));
                letter.setCenterShape(true);
                bindFontIntensive(letter, sizeBinding);
                letter.setPadding(new Insets(0, 0, padding - 3, 0));
                GridPane.setHalignment(letter, HPos.RIGHT);
                GridPane.setValignment(letter, VPos.BOTTOM);
                letter.translateXProperty().bind(bottomLetterCell.widthProperty().multiply(-0.8));
                board.add(letter, i + 1, boardSize);
            }
        }
    }

    private void drawHandicaps() {
        if(handicaps == 0)
            return;

        int lowerValue = (boardSize == 9) ? 2 : 3;
        int higherValue = boardSize - lowerValue - 1;
        int midValue = boardSize / 2;

        if (handicaps >= 1)
            gameHandler.addMove(lowerValue, higherValue, BLACK);

        if (handicaps >= 2)
            gameHandler.addMove(higherValue, lowerValue, BLACK);

        if (handicaps >= 3)
            gameHandler.addMove(higherValue, higherValue, BLACK);

        if (handicaps >= 4)
            gameHandler.addMove(lowerValue, lowerValue, BLACK);

        if (handicaps >= 5)
            gameHandler.addMove(midValue, midValue, BLACK);

        if (handicaps >= 6)
            gameHandler.addMove(midValue, lowerValue, BLACK);

        if (handicaps >= 7)
            gameHandler.addMove(midValue, higherValue, BLACK);

        if (handicaps >= 8)
            gameHandler.addMove(lowerValue, midValue, BLACK);

        if (handicaps == 9)
            gameHandler.addMove(higherValue, midValue, BLACK);

        playerHandler.changePlayer();
        modeAndMoveDisplay.setText(playerHandler.getCurrentPlayer().getName() + "'s turn");
        drawStones();
    }

    private void addArrowLogic(Polygon arrow, boolean isLeftArrow){
        //add on mouse clicked logic of an arrow
        //if left is clicked    -->   undo last move
        //if right is clicked   -->   redo last move
        arrow.setOnMouseClicked(e -> {
            if(isLeftArrow)
                gameHandler.undo();
            else
                gameHandler.redo();
            drawStones();
        });

        //bind properties to ensure qualitative resizing
        Region region = (Region) arrow.getParent();
        arrow.translateXProperty().bind(region.widthProperty().divide(2));
        arrow.translateYProperty().bind(region.heightProperty().divide(2));
    }

    private void addButtonBinding(Button button){
        button.prefWidthProperty().bind(boardPane.widthProperty().multiply(0.1));
        bindFontIntensive(button, 0.03);
        button.setFocusTraversable(false);
    }

    /*
      ================================================================================================================

                                            GUI helper functions

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

    /*
      ================================================================================================================

                                            other functions

      ================================================================================================================
     */

    private void drawStones() {
        Color[][] boardToDraw = gameHandler.getBoard().getBoard();
        StringBuilder printBoard = new StringBuilder();
        for(int r = 0; r < boardSize; r++) {
            for (int c = 0; c < boardSize; c++) {
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

    private void switchToWinnerMask(int reasonForWinning)  {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/winnerMaskGUI.fxml"));
            Parent root = loader.load();

            winnerMaskController winnerMask = loader.getController();
            winnerMask.setSize(boardPane.getWidth(), boardPane.getHeight());

            Player player = playerHandler.getCurrentPlayer();
            String playerWon = playerHandler.getCurrentPlayer().getName() + " " +
                        (playerHandler.getCurrentPlayer().getColor().equals(BLACK) ? "(Black)" : "(White)");
            String playerLost = playerHandler.getNextPlayer().getName() + " " +
                        (playerHandler.getNextPlayer().getColor().equals(BLACK) ? "(Black)" : "(White)");

            terminalInfo(playerWon + " won... \n[log end]");
            switch (reasonForWinning) {
                case 1 ->
                        //2x passed
                        winnerMask.initiateDisplay(playerWon, gameHandler.getTerritoryScore(player.getColor()),
                                                        gameHandler.getBoard().getTrapped(player.getColor()), komi);
                case 2 ->
                        //resigned
                        winnerMask.initiateDisplay(playerWon, playerLost, true);
                case 3 ->
                        //byoyomi time run out
                        winnerMask.initiateDisplay(playerWon, playerLost, false);
                case 4 ->
                        //draw
                        winnerMask.initiateDisplay();
            }
            terminalInfo("switching to winner Mask...\n[log end]");

            Node source = topRegion.getTop();
            Stage stage = (Stage) source.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setMinWidth(MIN_WIDTH);
            stage.setMinHeight(MIN_HEIGHT);
            stage.centerOnScreen();
        } catch (IOException e){
            terminalInfo(e.getMessage());
        }
    }

    private void setupKeyboardControls() {
        board.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case W -> moveSelection(0, -1);
                case S -> moveSelection(0, 1);
                case A -> moveSelection(-1, 0);
                case D -> moveSelection(1, 0);
                case SPACE -> {
                    placeStoneAtSelection();
                    // Request focus again after placing a stone
                    board.requestFocus();
                }
            }
        });
    }

    public void moveSelection(int dx, int dy) {
        // Unhighlight the previous position
        if (currentSelectionRow > 0 && currentSelectionRow <= boardSize &&
                currentSelectionCol > 0 && currentSelectionCol <= boardSize) {
            Circle previousCircle = circlesOfBoard[currentSelectionRow][currentSelectionCol];
            previousCircle.setStroke(null);
        }

        // Update row
        currentSelectionRow += dy;
        if (currentSelectionRow < 1) {
            currentSelectionRow = 1;
        } else if (currentSelectionRow > boardSize) {
            currentSelectionRow = boardSize;
        }

        // Update column
        currentSelectionCol += dx;
        if (currentSelectionCol < 1) {
            currentSelectionCol = 1;
        } else if (currentSelectionCol > boardSize) {
            currentSelectionCol = boardSize;
        }

        // Highlight the new position
        circlesOfBoard[currentSelectionRow][currentSelectionCol].setStroke(playerHandler.getCurrentPlayer().getColor());
        circlesOfBoard[currentSelectionRow][currentSelectionCol].setStrokeWidth(STROKE_WIDTH); // use a constant stroke width
    }

    public void placeStoneAtSelection() {
        Circle selectedCircle = circlesOfBoard[currentSelectionRow][currentSelectionCol];
        setStone(selectedCircle);
    }

    /*
      ================================================================================================================

                                            other system functions

      ================================================================================================================
     */

    private void terminalInfo(String data) {
        System.out.println(data);
    }

    private int switchNumAndIndex (int row) {
        return (boardSize - row);
    }
}