package com.example.go_gruppe1;

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
import javafx.scene.text.TextAlignment;
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
    private MenuItem fileSave;

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

    private Stone[][] boardArray;

    private final FileControl fileControl = new FileControl();

    public void onRenameFileClick(){
        //file functionality is disabled for now
        //fileControl.renameFile(renameFileName.getText());
    }

    public void onLoadFileClick(){
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            //file functionality is disabled for now
            //fileControl.loadFile(selectedFile);
        }
    }

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
        boardArray = new Stone[boardSize][boardSize];
        drawBoard(boardSize);
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
    private void drawBoard(int size) {
        boardSize = size;
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

        boardLabelling();

        //add color to board
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
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



        //initial start ... needs additional logic if handicaps are used
        modeAndMoveDisplay.setText(pl1.getText() + "'s turn!");
        modeAndMoveDisplay.setFont(Font.font("Baskerville Old Face", FontWeight.BOLD, 15));

        //add circles for stones
        for (int row = 0; row <= size; row++) {
            for (int col = 0; col <= size; col++) {
                Circle circle = new Circle(10, Color.TRANSPARENT);
                board.add(circle, row, col);

                //make stones resizable and adjust x and y properties
                circle.radiusProperty().bind(boardPane.heightProperty().multiply(0.6).divide(size).divide(4));
                circle.translateYProperty().bind(boardPane.heightProperty().multiply(0.6).divide(size * 2.4).multiply(-1));
                circle.translateXProperty().bind(boardPane.heightProperty().multiply(0.6).divide(size * 3.9).multiply(-1));

                //initial start ... needs additional logic if handicaps are used
                modeAndMoveDisplay.setText(pl1.getText() + "'s turn!");
                modeAndMoveDisplay.setFont(Font.font("Baskerville Old Face", FontWeight.BOLD, 15));

                //color for hovering
                Color hoverBlack = Color.valueOf("#00000070");
                Color hoverWhite = Color.valueOf("#FFFFFF70");
                //when the mouse is clicked the circle will be filled with a white or black colour depending on whose turn it is
                circle.setOnMouseClicked(e -> {
                    if(modePlay.isSelected()) {
                        if (circle.getFill() == hoverWhite || circle.getFill() == hoverBlack)
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
                                circle.setFill(hoverBlack);
                            else
                                circle.setFill(hoverWhite);
                        }
                    }
                });

                //when the mouse is no longer hovering over the circle the colour is removed
                circle.setOnMouseExited(e -> {
                    if(modePlay.isSelected()) {
                        if (circle.getFill() == hoverWhite || circle.getFill() == hoverBlack)
                            circle.setFill(Color.TRANSPARENT);
                    }
                });
            }
        }

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

    //should be improved (code for left and right are very similiar
    private void drawNavigationArrows(){
        leftArrow.setFill(Color.web("#483C32"));
        leftArrow.setStrokeWidth(1.5);

        // add leftArrow to leftRegion
        //leftRegion.getChildren().add(leftArrow);
        leftArrow.translateXProperty().bind(leftRegion.widthProperty().divide(2));
        leftArrow.translateYProperty().bind(leftRegion.heightProperty().divide(2));

        // create rightArrow
        rightArrow.setFill(Color.web("#483C32"));
        rightArrow.setStrokeWidth(1.5);

        // add leftArrow to leftRegion
        rightArrow.translateXProperty().bind(rightRegion.widthProperty().divide(2));
        rightRegion.translateYProperty().bind(rightRegion.heightProperty().divide(2));

        leftArrow.setScaleX(2);
        leftArrow.setScaleY(2);

        rightArrow.setScaleX(2);
        rightArrow.setScaleY(2);
    }

    private void drawPassButton(){
        passButton.setText("PASS");
        passButton.setFont(Font.font("Baskerville Old Face", FontWeight.BOLD, 13));
        passButton.setStyle("-fx-background-color: transparent; -fx-border-color: #483C32; -fx-text-fill: #483C32");
        passButton.setMinWidth(70);
        passButton.setPrefWidth(70);
        passButton.prefWidthProperty().bind(boardPane.widthProperty().multiply(0.08));
        passButton.setOnMouseEntered(e -> passButton.setStyle("-fx-background-color: #C4A484; -fx-border-color: #483C32"));
        passButton.setOnMouseExited(e -> passButton.setStyle("-fx-background-color: transparent; -fx-border-color: #483C32"));
        //topGrid.add(passButton, 1, 3);
        GridPane.setHalignment(passButton, HPos.LEFT);
        passButton.setTextAlignment(TextAlignment.CENTER);

        //pass logic
        passButton.setOnMouseClicked(e -> {
            if(lastColor == Color.BLACK) {
                modeAndMoveDisplay.setText(pl1.getText() + " passed! - " + pl2.getText() + "'s turn");
                lastColor = Color.WHITE;
            } else {
                modeAndMoveDisplay.setText(pl2.getText() + " passed! - " + pl1.getText() + "'s turn");
                lastColor = Color.BLACK;
            }
            //file functionality is disabled for now
            //fileControl.writeToPosition("\npassed");
        });
    }

    private void drawResignButton(){
        resignButton.setText("RESIGN");
        resignButton.setFont(Font.font("Baskerville Old Face", FontWeight.BOLD, 13));
        resignButton.setStyle("-fx-background-color: transparent; -fx-border-color: #483C32; -fx-text-fill: #483C32");
        resignButton.setMinWidth(70);
        resignButton.setPrefWidth(70);
        resignButton.setAlignment(Pos.CENTER_LEFT);
        resignButton.setTextAlignment(TextAlignment.CENTER);
        resignButton.prefWidthProperty().bind(boardPane.widthProperty().multiply(0.08));
        resignButton.setOnMouseEntered(e -> resignButton.setStyle("-fx-background-color: #C4A484; -fx-border-color: #483C32"));
        resignButton.setOnMouseExited(e -> resignButton.setStyle("-fx-background-color: transparent; -fx-border-color: #483C32"));
        //topGrid.add(resignButton, 1, 3);
        GridPane.setHalignment(resignButton, HPos.RIGHT);
        resignButton.setTextAlignment(TextAlignment.CENTER);

        //resign logic
        resignButton.setOnMouseClicked(e -> {
            if(lastColor == Color.BLACK) {
                modeAndMoveDisplay.setText(pl1.getText() + " resigned! - " + pl2.getText() + " won!");
            } else {
                modeAndMoveDisplay.setText(pl2.getText() + " resigned! - " + pl1.getText() + " won!");
            }
            //file functionality is disabled for now
            //fileControl.writeToPosition("\nresigned");
            /*try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }

            try {
                switchToInputMask();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }*/

        });
    }

    public void setStone(Circle c) {
        modeAndMoveDisplay.setFont(Font.font("Baskerville Old Face", FontWeight.BOLD, 15));

        for(Node n : board.getChildren()) {
            if(n instanceof Circle && n.equals(c)) {
                String stonePosition = "\n" + board.getRowIndex(n) + alphabet[board.getColumnIndex(n)];
                //fileControl.writeToPosition(stonePosition);
                System.out.print(stonePosition);
                //int col = board.getColumnIndex(n);
                //int row = board.getRowIndex(n) + 1;
                //addStoneToBoardArray(board.getColumnIndex(n), board.getRowIndex(n));
                //System.out.print(row);
                //System.out.println(alphabet[col]);
            }
        }

        if(lastColor == Color.WHITE){
            c.setFill(lastColor);
            lastColor = Color.BLACK;
            modeAndMoveDisplay.setText(pl1.getText() + "'s turn!");
        } else {
            c.setFill(lastColor);
            lastColor = Color.WHITE;
            modeAndMoveDisplay.setText(pl2.getText() + "'s turn!");
        }
    }

    private void addStoneToBoardArray(int col, int row){
        Stone stone = new Stone(lastColor);//, row, col);
        boardArray[row][col] = stone;

        //sets liberties for the stone above
        if(!(row - 1 < 0)){
            Stone upperNeighbour = boardArray[row-1][col];
            if(upperNeighbour == null)
                stone.changeLiberty(1);
            else if(upperNeighbour.getColour() != lastColor) {
                upperNeighbour.changeLiberty(-1);
                if(upperNeighbour.isDead())
                    deleteStone((row - 1), col);
            } else {
                boardArray[row][col] = upperNeighbour;

                //stone.changeLiberty(-1);
                //.changeLiberty(-1);
                //stone.addToGroup(upperNeighbour);
            }
            System.out.println("Stone at " + (row+1) + ", " + (col+1) + " has " + stone.getLiberties() + " liberties.");
        }

        //sets liberties for the stone to the right
        if(col + 1 < boardSize){
            Stone rightNeighbour = boardArray[row][col+1];
            if(rightNeighbour == null)
                stone.changeLiberty(1);
            else if(rightNeighbour.getColour() != lastColor) {
                rightNeighbour.changeLiberty(-1);
                if(rightNeighbour.isDead())
                    deleteStone(row, (col + 1));
            }
            System.out.println("Stone at " + (row+1) + ", " + (col+1) + " has " + stone.getLiberties() + " liberties.");
        }

        //sets liberties for the stone underneath
        if (row + 1 < boardSize) {
            Stone lowerNeighbour = boardArray[row + 1][col];
            if (lowerNeighbour == null)
                stone.changeLiberty(1);
            else if (lowerNeighbour.getColour() != lastColor) {
                lowerNeighbour.changeLiberty(-1);
                if(lowerNeighbour.isDead())
                    deleteStone((row + 1), row);
            }
            System.out.println("Stone at " + (row+1) + ", " + (col+1) + " has " + stone.getLiberties() + " liberties.");
        }

        //sets liberties for the stone to the left
        if(!(col - 1 < 0)){
            Stone leftNeighbour = boardArray[row][col-1];
            if(leftNeighbour == null)
                stone.changeLiberty(1);
            else if(leftNeighbour.getColour() != lastColor){
                leftNeighbour.changeLiberty(-1);
                if(leftNeighbour.isDead())
                    deleteStone(row, (row - 1));
            }
            System.out.println("Stone at " + (row+1) + ", " + (col+1) + " has " + stone.getLiberties() + " liberties.");
        }
        if(stone.getLiberties() == 0)
            deleteStone(row, col);
    }

    protected void deleteStone(int row, int col){
        if(boardArray[row][col].getColour() == Color.BLACK)
            displayTrappedStone(++blackTrappedStones, blackTrapped);
        else
            displayTrappedStone(++whiteTrappedStones, whiteTrapped);
        boardArray[row][col] = null;
        if(boardArray[row-1][col] != null)
            boardArray[row-1][col].changeLiberty(1);
        if(boardArray[row][col+1] != null)
            boardArray[row][col+1].changeLiberty(1);
        if(boardArray[row+1][col] != null)
            boardArray[row+1][col].changeLiberty(1);
        if(boardArray[row][col-1] != null)
            boardArray[row][col-1].changeLiberty(1);

        for(Node n : board.getChildren()) {
            if(n instanceof Circle c && board.getColumnIndex(n) == col && board.getRowIndex(n) == row) {
                c.setFill(Color.TRANSPARENT);
            }
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


