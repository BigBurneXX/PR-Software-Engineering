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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import java.util.concurrent.TimeUnit;

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

    private final char[] alphabet = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T'};

    private BoardLogicControl boardLogicControl;

    private final FileControl fileControl = new FileControl();

    private boolean isGameOver = false;
    private int passTurnCounter = 0;
    
    private Label timerLabel;
    private long startTime;
    private Timeline timerTimeline;

    public void initialize() {
        initTimer();
        topGrid.add(timerLabel, 2, 0);
        GridPane.setHalignment(timerLabel, HPos.CENTER);
        GridPane.setValignment(timerLabel, VPos.CENTER);
    }


    @FXML
    public void onSaveFileClick(){
        //file functionality is disabled for now
        //fileControl.saveFile();
    }

    @FXML
    public void onRenameFileClick(){
        //file functionality is disabled for now
        //fileControl.renameFile(renameFileName.getText());
    }

    @FXML
    public void onLoadFileClick(){
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            //file functionality is disabled for now
            //fileControl.loadFile(selectedFile);
        }
    }

    @FXML
    public void onModePlayClick() {
        leftArrow.setVisible(false);
        rightArrow.setVisible(false);
        passButton.setVisible(false);
        resignButton.setVisible(false);
        modeAndMoveDisplay.setFont(Font.font("Baskerville Old Face", FontWeight.BOLD, 15));
        modeAndMoveDisplay.prefHeightProperty().bind(bottomRegion.heightProperty().multiply(0.25));

        if(lastColor == Color.BLACK) {
            modeAndMoveDisplay.setText(pl1.getText() + "'s turn!");
        } else {
            modeAndMoveDisplay.setText(pl2.getText() + "'s turn!");
        }
    }

    @FXML
    public void onModeNavigateClick() {
        leftArrow.setVisible(true);
        rightArrow.setVisible(true);
        passButton.setVisible(true);
        resignButton.setVisible(true);
        modeAndMoveDisplay.setText("Navigate mode activated");
        modeAndMoveDisplay.setFont(Font.font("Baskerville Old Face", FontWeight.BOLD, board.getHeight() * 0.10));
        modeAndMoveDisplay.prefHeightProperty().bind(bottomRegion.heightProperty().multiply(0.25));
    }

    //first all ActionEventControllers then other controllers
    protected void setSampleSolutionDisplay(String text){
        sampleSolutionDisplay.setText(text);
    }

    protected void setSize(double width, double height) {
        boardPane.setPrefHeight(height);
        boardPane.setPrefWidth(width);
    }

    private double getWidth() {
        return boardPane.getWidth();
    }

    private double getHeight() {
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

    protected void initiateDisplay(String player1Name, String player2Name, String komi, String handicaps, int boardSize){
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

        boardPane.requestFocus();

        boardPane.setOnMouseClicked(e -> {
            boardPane.requestFocus();
        });

        handleKeyboardEvents();

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
            //only values greater than 0 are valid
            komiBoard.setText("Komi: " +
                    (d < 0 ? "0" : d)
            );
        } catch (NumberFormatException nfe) {
            komiBoard.setText("Komi: 0");
            if(!komiAdvantage.isEmpty()) {
                sampleSolutionDisplay.setText(sampleSolutionDisplay.getText() + "\nInvalid komi input -> handicaps set to 0");
                System.out.println("Invalid komi input -> handicaps set to 0");
            }
        }
    }

    private void displayHandicaps(String handicaps) {
        //only numeric values can be entered
        try {
            int d = Integer.parseInt(handicaps);
            System.out.println(d);
            //only values greater than 0 are valid
            handicapsBoard.setText("Handicaps: " +
                    (d < 0 ? "0" : d)
            );
        } catch (NumberFormatException nfe) {
            handicapsBoard.setText("Handicaps: 0");
            if(!handicaps.isEmpty()) {
                sampleSolutionDisplay.setText(sampleSolutionDisplay.getText() + "\nInvalid handicap input -> handicaps set to 0");
                System.out.println("Invalid handicap input -> handicaps set to 0");
            }
        }
    }

    private void displayTrappedStone(int numberTrapped, Label trappedLabel){
        if(numberTrapped >= 0){
            trappedLabel.setText("Trapped: " + numberTrapped);
        }
        else
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
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                Pane cell = new Pane();
                cell.setStyle("-fx-background-color:  #C4A484; -fx-border-color: #483C32");
                board.add(cell, row, col);
            }
        }

        /*Pane numberAndLetterCell = new Pane();
        numberAndLetterCell.setStyle("-fx-background-color:  #F2F3F5; -fx-border-color: transparent");
        board.add(numberAndLetterCell, size, size);
        Label letter = new Label();
        letter.setText(String.valueOf(alphabet[size]));
        letter.setFont(Font.font("Baskerville Old Face", FontWeight.BOLD, 13));
        letter.setStyle("-fx-text-fill: #C4A484");
        board.setHalignment(letter, HPos.LEFT);
        board.setValignment(letter, VPos.BOTTOM);
        letter.setStyle("-fx-font-size: 15");
        board.add(letter, size, size);*/

        /*Label number = new Label(String.valueOf(size + 1));
        number.setFont(Font.font("Baskerville Old Face", FontWeight.BOLD, 13));
        number.setStyle("-fx-text-fill: #C4A484");
        board.setHalignment(number, HPos.RIGHT);
        board.setValignment(number, VPos.TOP);
        number.setStyle("-fx-font-size: 15");
        board.add(number, size, size);*/

        addStones();

        updateCurrentCell(board, currentRow, currentCol);

        // Add the keyboard event handler
        board.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            int newRow = currentRow;
            int newCol = currentCol;

            if (e.getCode() == KeyCode.UP) {
                newRow = newRow > 0 ? newRow - 1 : newRow;
            } else if (e.getCode() == KeyCode.DOWN) {
                newRow = newRow < boardSize - 1 ? newRow + 1 : newRow;
            } else if (e.getCode() == KeyCode.LEFT) {
                newCol = newCol > 0 ? newCol - 1 : newCol;
            } else if (e.getCode() == KeyCode.RIGHT) {
                newCol = newCol < boardSize - 1 ? newCol + 1 : newCol;
            } else if (e.getCode() == KeyCode.SPACE || e.getCode() == KeyCode.ENTER) {
                Node node = getNodeFromGridPane(board, currentCol, currentRow);
                if (node instanceof Circle) {
                    setStone((Circle) node);
                }
            }

            if (newRow != currentRow || newCol != currentCol) {
                updateCurrentCell(board, newRow, newCol);
            }

            e.consume();
        });


        //initial start ... needs additional logic if handicaps are used
        modeAndMoveDisplay.setText(pl1.getText() + "'s turn!");
        modeAndMoveDisplay.setFont(Font.font("Baskerville Old Face", FontWeight.BOLD, 15));

        drawNavigationArrows();
        drawPassButton();
        drawResignButton();

        //creating output file
        //For now creating a file is deactivated, otherwise there would be too much files created while coding
        //fileControl.createFile(this, "", player1Name, player2Name, boardSize, komiBoard.getText());


    }

    private void boardLabelling(){
        for (int row = 0; row <= boardSize; row++) {
            //numbers on the right
            Pane numberCell = new Pane();
            numberCell.setStyle("-fx-background-color:  #F2F3F5; -fx-border-color: transparent");
            board.add(numberCell, boardSize, row);

            Label number = new Label(String.valueOf(row + 1));
            number.setCenterShape(true);
            number.setFont(Font.font("Baskerville Old Face", FontWeight.BOLD, 13));
            number.setStyle("-fx-text-fill: #C4A484");
            board.setHalignment(number, HPos.RIGHT);
            board.setValignment(number, VPos.TOP);
            number.setStyle("-fx-font-size: 15");
            number.translateYProperty().bind(numberCell.heightProperty().divide(4).multiply(-1));

            if(row == boardSize) {
                number.setFont(Font.font("Baskerville Old Face", FontWeight.BOLD, 13));
                number.setStyle("-fx-text-fill: #C4A484");
                board.setHalignment(number, HPos.RIGHT);
                board.setValignment(number, VPos.TOP);
                number.setStyle("-fx-font-size: 15");
            }
            board.add(number, boardSize, row);

            //letters on the bottom
            if(row != boardSize) {
                Pane letterCell = new Pane();
                letterCell.setStyle("-fx-background-color:  #F2F3F5; -fx-border-color: transparent");
                board.add(letterCell, row, boardSize);
            }

            Label letter = new Label();
            letter.setText(String.valueOf(alphabet[row]));
            letter.setCenterShape(true);
            letter.setFont(Font.font("Baskerville Old Face", FontWeight.BOLD, 13));
            letter.setStyle("-fx-text-fill: #C4A484");
            board.setHalignment(letter, HPos.LEFT);
            board.setValignment(letter, VPos.BOTTOM);
            letter.setStyle("-fx-font-size: 15");
            letter.translateXProperty().bind(numberCell.widthProperty().divide(8).multiply(-1));
            if(row == boardSize) {
                letter.setText(String.valueOf(alphabet[boardSize]));
                letter.setFont(Font.font("Baskerville Old Face", FontWeight.BOLD, 13));
                letter.setStyle("-fx-text-fill: #C4A484");
                board.setHalignment(letter, HPos.LEFT);
                board.setValignment(letter, VPos.BOTTOM);
                letter.setStyle("-fx-font-size: 15");
            }
            board.add(letter, row, boardSize);
        }
    }


    //add circles for stones
    private void addStones() {
        for (int row = 0; row <= boardSize; row++) {
            for (int col = 0; col <= boardSize; col++) {
                Circle circle = new Circle(10, Color.TRANSPARENT);
                board.add(circle, row, col);

                //make stones resizable and adjust x and y properties
                circle.radiusProperty().bind(boardPane.heightProperty().multiply(0.6).divide(boardSize).divide(4));
                circle.translateYProperty().bind(boardPane.heightProperty().multiply(0.6).divide(boardSize * 2.4).multiply(-1));
                circle.translateXProperty().bind(boardPane.heightProperty().multiply(0.6).divide(boardSize * 3.9).multiply(-1));

                //initial start ... needs additional logic if handicaps are used
                modeAndMoveDisplay.setText(pl1.getText() + "'s turn!");
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

    //should be improved (code for left and right are very similar)
    private void drawNavigationArrows(){
        leftArrow.translateXProperty().bind(leftRegion.widthProperty().divide(2));
        leftArrow.translateYProperty().bind(leftRegion.heightProperty().divide(2));

        rightArrow.translateXProperty().bind(rightRegion.widthProperty().divide(2));
        rightRegion.translateYProperty().bind(rightRegion.heightProperty().divide(2));
    }

    private void drawPassButton(){
        passButton.setFont(Font.font("Baskerville Old Face", FontWeight.BOLD, 13));
        passButton.prefWidthProperty().bind(boardPane.widthProperty().multiply(0.08));

        passButton.setOnMouseEntered(e -> passButton.setStyle("-fx-background-color: #C4A484; -fx-border-color: #483C32"));
        passButton.setOnMouseExited(e -> passButton.setStyle("-fx-background-color: transparent; -fx-border-color: #483C32"));

        //pass logic
        passButton.setOnMouseClicked(e -> {
            passTurnCounter++;
            if (passTurnCounter >= 2) {
                isGameOver = true;
            }

            if (lastColor == Color.WHITE) {
                lastColor = Color.BLACK;
                modeAndMoveDisplay.setText(pl1.getText() + "'s turn!");
            } else {
                lastColor = Color.WHITE;
                modeAndMoveDisplay.setText(pl2.getText() + "'s turn!");
            }
        });
    }

    private void drawResignButton(){
        resignButton.setFont(Font.font("Baskerville Old Face", FontWeight.BOLD, 13));
        resignButton.prefWidthProperty().bind(boardPane.widthProperty().multiply(0.08));
        resignButton.setOnMouseEntered(e -> resignButton.setStyle("-fx-background-color: #C4A484; -fx-border-color: #483C32"));
        resignButton.setOnMouseExited(e -> resignButton.setStyle("-fx-background-color: transparent; -fx-border-color: #483C32"));

        //resign logic
        resignButton.setOnMouseClicked(e -> {
            if(lastColor == Color.BLACK) {
                modeAndMoveDisplay.setText(pl1.getText() + " resigned! - " + pl2.getText() + " won!");
            } else {
                modeAndMoveDisplay.setText(pl2.getText() + " resigned! - " + pl1.getText() + " won!");
            }
            //file functionality is disabled for now
            //fileControl.writeToPosition("\nresigned");
            isGameOver = true; // Move this line inside the setOnMouseClicked event handler
        });
    }


    public void setStone(Circle c) {
        modeAndMoveDisplay.setFont(Font.font("Baskerville Old Face", FontWeight.BOLD, 15));

        for(Node n : board.getChildren()) {
            if(n instanceof Circle && n.equals(c)) {
                int row = board.getRowIndex(n);
                int col = board.getColumnIndex(n);
                String stonePosition = "\n" + row + alphabet[col];
                //boardLogicControl.setStone(lastColor, row, col);
                //fileControl.writeToPosition(stonePosition);
                System.out.print(stonePosition);
                //int col = board.getColumnIndex(n);
                //int row = board.getRowIndex(n) + 1;
                //System.out.print(row);
                //System.out.println(alphabet[col]);
            }
        }
        passTurnCounter = 0;

        if(lastColor == Color.WHITE){
            c.setFill(lastColor);
            lastColor = Color.BLACK;
            modeAndMoveDisplay.setText(pl1.getText() + "'s turn!");
        } else {
            c.setFill(lastColor);
            lastColor = Color.WHITE;
            modeAndMoveDisplay.setText(pl2.getText() + "'s turn!");
        }
        if (isGameOver) {
            checkWinCondition();
        }
    }


    protected void deleteStoneGroup(StoneGroup toDelete){
        int stonesCaptured = toDelete.getPosition().size();
        if(toDelete.getColour() == Color.BLACK) {
            blackTrappedStones += stonesCaptured;
            displayTrappedStone(blackTrappedStones, blackTrapped);
        } else {
            whiteTrappedStones += stonesCaptured;
            displayTrappedStone(whiteTrappedStones, whiteTrapped);
        }

        //finds the circle for every position of stoneGroup toDelete and sets the visibility to TRANSPARENT
        for (Position p : toDelete.getPosition())
            for(Node n : board.getChildren())
                if(n instanceof Circle c && board.getColumnIndex(n) == p.col() && board.getRowIndex(n) == p.row())
                    c.setFill(Color.TRANSPARENT);
    }


    private void checkWinCondition() {
        int player1Points = blackTrappedStones;
        int player2Points = whiteTrappedStones;

        // Check for a win condition
        if (player1Points > player2Points) {
            showWinDialog(pl1.getText(), player1Points, player2Points);
        } else if (player1Points < player2Points) {
            showWinDialog(pl2.getText(), player1Points, player2Points);
        } else {
            showDrawDialog(player1Points, player2Points);
        }
    }


    private void showWinDialog(String winner, int player1Points, int player2Points) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(winner + " wins!");
        alert.setContentText("Player 1 Points: " + player1Points + "\nPlayer 2 Points: " + player2Points);
        alert.showAndWait();
    }

    private void showDrawDialog(int player1Points, int player2Points) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText("It's a draw!");
        alert.setContentText("Player 1 Points: " + player1Points + "\nPlayer 2 Points: " + player2Points);
        alert.showAndWait();
    }
    private int currentRow = 0;
    private int currentCol = 0;

    private void updateCurrentCell(GridPane board, int newRow, int newCol) {
        Node oldNode = getNodeFromGridPane(board, currentCol, currentRow);
        if (oldNode instanceof Circle) {
            Circle oldCircle = (Circle) oldNode;
            if (oldCircle.getFill() == Color.TRANSPARENT) {
                oldCircle.setStroke(null);
            }
        }

        currentRow = newRow;
        currentCol = newCol;

        Node newNode = getNodeFromGridPane(board, currentCol, currentRow);
        if (newNode instanceof Circle) {
            Circle newCircle = (Circle) newNode;
            if (newCircle.getFill() == Color.TRANSPARENT) {
                newCircle.setStroke(Color.RED);
                newCircle.setStrokeWidth(2);
            }
        }
    }

    private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }

    public void handleKeyboardEvents() {
        boardPane.setOnKeyPressed(e -> {
            int newRow = currentRow;
            int newCol = currentCol;
            switch (e.getCode()) {
                case UP:
                    newRow = Math.max(0, currentRow - 1);
                    break;
                case DOWN:
                    newRow = Math.min(boardSize - 1, currentRow + 1);
                    break;
                case LEFT:
                    newCol = Math.max(0, currentCol - 1);
                    break;
                case RIGHT:
                    newCol = Math.min(boardSize - 1, currentCol + 1);
                    break;
                case ENTER:
                    Node node = getNodeFromGridPane(board, currentCol, currentRow);
                    if (node instanceof Circle) {
                        Circle circle = (Circle) node;
                        if (circle.getFill() == Color.TRANSPARENT) {
                            setStone(circle);
                        }
                    }
                    break;
                default:
                    break;
            }
            updateCurrentCell(board, newRow, newCol);
        });
        boardPane.requestFocus();
    }
    
     private void initTimer() {
        startTime = System.currentTimeMillis();
        timerLabel = new Label("00:00");
        timerLabel.setFont(Font.font("Baskerville Old Face", FontWeight.BOLD, 15));

        timerTimeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateTimer()));
        timerTimeline.setCycleCount(Animation.INDEFINITE);
        timerTimeline.play();
    }


    private void updateTimer() {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - startTime;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(elapsedTime);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedTime) - TimeUnit.MINUTES.toSeconds(minutes);

        timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
    }

}

