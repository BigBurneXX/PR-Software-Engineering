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
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
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
    private MenuItem fileSave, fileLoadGame;

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

    private File outputFile;

    private Button pass, resign;

    private int boardSize;
    private String player1Name;
    private String player2Name;

    private int blackTrappedStones = 0;

    private int whiteTrappedStones = 0;

    private final char[] alphabet = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T'};

    private Stone[][] boardArray;

    private void createFile(String oldFileName){
        try{
            String newFileName = oldFileName.endsWith(".txt") ?
                    oldFileName.substring(0, oldFileName.length() -4) + "_1.txt" : player1Name + "_" + player2Name + ".txt";
            File outputFile = new File(newFileName);
            if (outputFile.createNewFile()) {
                FileWriter fileWriter = new FileWriter(outputFile);
                fileWriter.write(player1Name + " vs. " + player2Name);
                fileWriter.write("\nBoard size: " + boardSize);
                fileWriter.write("\n" + komiBoard.getText());
                fileWriter.close();
                System.out.println("File " + outputFile.getName() + " created.");
            }else {
                System.out.println("File " + outputFile.getName() + " already exists!");
                createFile(outputFile.getName());
            }
        } catch (IOException e ){
            System.out.println("File " + outputFile.getName() + " creation failed!");
            e.printStackTrace();
        }
    }

    public void onRenameFileClick(){
        File f = new File(renameFileName.getText() + ".txt");
        if(outputFile.renameTo(f)){
            sampleSolutionDisplay.setText("File successfully renamed!");
            System.out.println("The name of the output file has successfully been changed to " + outputFile.getName());
        } else {
            System.out.println("Renaming unsuccessfully, check if a file with the same name already exists in the selected directory and try again");
            sampleSolutionDisplay.setText("Renaming unsuccessfully, check if a file with the same name already exists in the selected directory and try again");
        }
    }

    protected void setSize(double width, double height) {
        boardPane.setPrefHeight(height);
        boardPane.setPrefWidth(width);
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
        leftRegion.getChildren().clear();
        rightRegion.getChildren().clear();
        modeAndMoveDisplay.setFont(Font.font("Baskerville Old Face", FontWeight.BOLD, 15));
        modeAndMoveDisplay.prefHeightProperty().bind(bottomRegion.heightProperty().multiply(0.25));

        if(lastColor == Color.BLACK) {
            modeAndMoveDisplay.setText(pl1.getText() + "'s turn!");
        } else {
            modeAndMoveDisplay.setText(pl2.getText() + "'s turn!");
        }
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
        modePlay.setSelected(true);
        this.boardSize = boardSize;
        boardArray = new Stone[boardSize][boardSize];
        drawBoard(boardSize);
    }

    private void displayPlayerNames(String p1, String p2) {
        p1 = p1.isEmpty() ? "Player 1" : p1;
        p2 = p2.isEmpty() ? "Player 2" : p2;

        pl1.setText(p1 + " (Black)");
        pl2.setText(p2 + " (White)");
        player1Name = p1;
        player2Name = p2;
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

        //add board labelling
        for (int row = 0; row <= size; row++) {
            //numbers on the right
            Pane numberCell = new Pane();
            numberCell.setStyle("-fx-background-color:  #F2F3F5; -fx-border-color: transparent");
            board.add(numberCell, size, row);

            Label number = new Label(String.valueOf(row + 1));
            number.setCenterShape(true);
            number.setFont(Font.font("Baskerville Old Face", FontWeight.BOLD, 13));
            number.setStyle("-fx-text-fill: #C4A484");
            board.setHalignment(number, HPos.RIGHT);
            board.setValignment(number, VPos.TOP);
            number.setStyle("-fx-font-size: 15");
            number.translateYProperty().bind(numberCell.heightProperty().divide(4).multiply(-1));

            if(row == size) {
                number.setFont(Font.font("Baskerville Old Face", FontWeight.BOLD, 13));
                number.setStyle("-fx-text-fill: #C4A484");
                board.setHalignment(number, HPos.RIGHT);
                board.setValignment(number, VPos.TOP);
                number.setStyle("-fx-font-size: 15");
            }
            board.add(number, size, row);

            //letters on the bottom
            if(row != size) {
                Pane letterCell = new Pane();
                letterCell.setStyle("-fx-background-color:  #F2F3F5; -fx-border-color: transparent");
                board.add(letterCell, row, size);
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
            if(row == size) {
                letter.setText(String.valueOf(alphabet[size]));
                letter.setFont(Font.font("Baskerville Old Face", FontWeight.BOLD, 13));
                letter.setStyle("-fx-text-fill: #C4A484");
                board.setHalignment(letter, HPos.LEFT);
                board.setValignment(letter, VPos.BOTTOM);
                letter.setStyle("-fx-font-size: 15");
            }
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

        pass = new Button("PASS");
        pass.setFont(Font.font("Baskerville Old Face", FontWeight.BOLD, 13));
        pass.setStyle("-fx-background-color: transparent; -fx-border-color: #483C32; -fx-text-fill: #483C32");
        pass.setMinWidth(70);
        pass.setPrefWidth(70);
        pass.prefWidthProperty().bind(boardPane.widthProperty().multiply(0.08));
        pass.setOnMouseEntered(e -> pass.setStyle("-fx-background-color: #C4A484; -fx-border-color: #483C32"));
        pass.setOnMouseExited(e -> pass.setStyle("-fx-background-color: transparent; -fx-border-color: #483C32"));
        topGrid.add(pass, 1, 3);
        GridPane.setHalignment(pass, HPos.LEFT);
        pass.setTextAlignment(TextAlignment.CENTER);

        //pass logic
        pass.setOnMouseClicked(e -> {
            if(lastColor == Color.BLACK) {
                modeAndMoveDisplay.setText(pl1.getText() + " passed! - " + pl2.getText() + "'s turn");
                lastColor = Color.WHITE;
            } else {
                modeAndMoveDisplay.setText(pl2.getText() + " passed! - " + pl1.getText() + "'s turn");
                lastColor = Color.BLACK;
            }
        });


        resign = new Button("RESIGN");
        resign.setFont(Font.font("Baskerville Old Face", FontWeight.BOLD, 13));
        resign.setStyle("-fx-background-color: transparent; -fx-border-color: #483C32; -fx-text-fill: #483C32");
        resign.setMinWidth(70);
        resign.setPrefWidth(70);
        resign.setAlignment(Pos.CENTER_LEFT);
        resign.setTextAlignment(TextAlignment.CENTER);
        resign.prefWidthProperty().bind(boardPane.widthProperty().multiply(0.08));
        resign.setOnMouseEntered(e -> resign.setStyle("-fx-background-color: #C4A484; -fx-border-color: #483C32"));
        resign.setOnMouseExited(e -> resign.setStyle("-fx-background-color: transparent; -fx-border-color: #483C32"));
        topGrid.add(resign, 1, 3);
        GridPane.setHalignment(resign, HPos.RIGHT);
        resign.setTextAlignment(TextAlignment.CENTER);

        //creating output file
        //For now creating a file is deactivated, otherwise there would be too much files created while coding
        //createFile("");
    }

    public void setStone(Circle c) {
        modeAndMoveDisplay.setFont(Font.font("Baskerville Old Face", FontWeight.BOLD, 15));

        for(Node n : board.getChildren()) {
            if(n instanceof Circle && n.equals(c)) {
                System.out.println(board.getColumnIndex(n));
                System.out.println(board.getRowIndex(n));
                int col = board.getColumnIndex(n);
                int row = board.getRowIndex(n) + 1;
                //addStoneToBoardArray(board.getColumnIndex(n), board.getRowIndex(n));
                System.out.print(row);
                System.out.println(alphabet[col]);
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


