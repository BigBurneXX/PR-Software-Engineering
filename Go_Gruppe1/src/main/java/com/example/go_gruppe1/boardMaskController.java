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
import javafx.scene.text.Text;
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
        for (int col = 1; col < boardSize; col++) {
            for (int row = 1; row < boardSize; row++) {
                Pane cell = new Pane();
                cell.setStyle("-fx-background-color:  #C4A484; -fx-border-color: #483C32");
                board.add(cell, col, row);
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

        //initial start - still needs handicap logic
        if(Integer.parseInt(handicapsBoard.getText().substring(handicapsBoard.getText().length()-1)) <= 0) {
            //modeAndMoveDisplay.setText(pl1.getText() + "'s turn!");
            //lastColor = Color.BLACK;
        } else {
            modeAndMoveDisplay.setText(pl2.getText() + "'s turn!");
        }
        modeAndMoveDisplay.setFont(Font.font("Baskerville Old Face", FontWeight.BOLD, 15));

        drawNavigationArrows();
        drawPassButton();
        drawResignButton();

        //creating output file
        //For now creating a file is deactivated, otherwise there would be too much files created while coding
        //fileControl.createFile(this, "", player1Name, player2Name, boardSize, komiBoard.getText());
    }

    private void boardLabelling(){
        //add color and labelling but without borders
        for (int i = 0; i <= boardSize; i++) {
            //top color
            Pane topLetterCell = new Pane();
            topLetterCell.setStyle("-fx-background-color:  #C4A484");
            board.add(topLetterCell, i, 0);

            //right color
            if(i != 0 && i != boardSize) {
                Pane rightCell = new Pane();
                rightCell.setStyle("-fx-background-color:  #C4A484");
                board.add(rightCell, boardSize, i);
            }

            //bottom color
            Pane bottomLetterCell = new Pane();
            bottomLetterCell.setStyle("-fx-background-color:  #C4A484");
            board.add(bottomLetterCell, i, boardSize);

            //right labelling
            if(i != 0) {
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
            if(i != 0 && i != boardSize) {
                Pane leftNumberCell = new Pane();
                leftNumberCell.setStyle("-fx-background-color:  #C4A484");
                board.add(leftNumberCell, 0, i);
            }


            //add A in left column since the pane also is created in the left column
            /*if(i == boardSize) {
                Label A = new Label();
                A.setText(String.valueOf(alphabet[0]));
                A.setCenterShape(true);
                A.setFont(Font.font("Baskerville Old Face", FontWeight.BOLD, 13));
                A.setStyle("-fx-text-fill: #C4A484");
                board.setHalignment(A, HPos.RIGHT);
                board.setValignment(A, VPos.BOTTOM);
                A.setStyle("-fx-font-size: 15");
                A.translateXProperty().bind(leftNumberCell.widthProperty().divide(8));
                board.add(A, 0, i);
            }*/



            if(i < boardSize) {
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





            /*if(row != boardSize) {
                Label number = new Label(String.valueOf(row + 1));
                number.setCenterShape(true);
                number.setFont(Font.font("Baskerville Old Face", FontWeight.BOLD, 13));
                number.setStyle("-fx-text-fill: #C4A484");
                board.setHalignment(number, HPos.RIGHT);
                board.setValignment(number, VPos.BOTTOM);
                number.setStyle("-fx-font-size: 15");
                number.translateYProperty().bind(rightNumberCell.heightProperty().divide(4).multiply(-1));
                //board.add(number, boardSize, row + 1);
                rightNumberCell.getChildren().add(number);

            }*/


            //letters on the bottom
            /*if(row != boardSize) {
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
            letter.translateXProperty().bind(rightNumberCell.widthProperty().divide(8).multiply(-1));
            if(row == boardSize) {
                letter.setText(String.valueOf(alphabet[boardSize]));
                letter.setFont(Font.font("Baskerville Old Face", FontWeight.BOLD, 13));
                letter.setStyle("-fx-text-fill: #C4A484");
                board.setHalignment(letter, HPos.LEFT);
                board.setValignment(letter, VPos.BOTTOM);
                letter.setStyle("-fx-font-size: 15");
            }
            board.add(letter, row, boardSize);*/
        //}


    }


    //add circles for stones
    private void addStones() {
        for (int row = 1; row <= boardSize; row++) {
            for (int col = 1; col <= boardSize; col++) {
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
        });
        //file functionality is disabled for now
        //fileControl.writeToPosition("\nresigned");
    }

    public void setStone(Circle c) {
        modeAndMoveDisplay.setFont(Font.font("Baskerville Old Face", FontWeight.BOLD, 15));

        for(Node n : board.getChildren()) {
            if(n instanceof Circle && n.equals(c)) {
                int row = board.getRowIndex(n);
                int col = board.getColumnIndex(n);
                String stonePosition = "\n" + row + alphabet[col];
                //boardLogicControl.setStoneToList(lastColor, row, col);
                //boardLogicControl.setStone(lastColor, row, col);
                //fileControl.writeToPosition(stonePosition);
                System.out.println(" " + (row) + alphabet[col-1]);
                //int col = board.getColumnIndex(n);
                //int row = board.getRowIndex(n) + 1;
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

    protected void deleteStoneGroup(StoneGroup toDelete){
        if(toDelete.getColour() == Color.BLACK)
            displayTrappedStone(++blackTrappedStones, blackTrapped);
        else
            displayTrappedStone(++whiteTrappedStones, whiteTrapped);

        //finds the circle for every position of stoneGroup toDelete and sets the visibility to TRANSPARENT
        for (Position p : toDelete.getPosition())
            for(Node n : board.getChildren())
                if(n instanceof Circle c && board.getColumnIndex(n) == p.col() && board.getRowIndex(n) == p.row())
                    c.setFill(Color.TRANSPARENT);
    }
}


