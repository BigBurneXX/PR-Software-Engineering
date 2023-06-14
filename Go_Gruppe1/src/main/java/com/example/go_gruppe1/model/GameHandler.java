package com.example.go_gruppe1.model;

import javafx.scene.paint.Color;

public class GameHandler {
    private final Player playerBlack;
    private final Player playerWhite;
    private Player currentPlayer;
    private Player nextPlayer;
    private boolean logging;

    public GameHandler(String playerBlackName, String playerWhiteName){
        //final Color HOVER_BLACK = Color.valueOf("#00000070");
        //final Color HOVER_WHITE = Color.valueOf("#FFFFFF70");


        playerBlack = new Player(playerBlackName, Color.BLACK, getHoverColorBlack());
        playerWhite = new Player(playerWhiteName, Color.WHITE, getHoverColorWhite());
        currentPlayer = playerBlack;
        nextPlayer = playerWhite;
    }

    public void moveMade(){
        currentPlayer.getTimer().stopTimer();
        nextPlayer.getTimer().startTimer();
        terminalInfo(String.valueOf(currentPlayer.getTimer().passedSlotSeconds()));
        changePlayer();
    }

    public void startTimer(){
        currentPlayer.getTimer().startTimer();
    }

    public Color getHoverColorBlack(){
        return Color.valueOf("#00000070");
    }

    public Color getHoverColorWhite(){
        return Color.valueOf("#FFFFFF70");
    }
    private void changePlayer(){
        Player change = currentPlayer;
        currentPlayer = nextPlayer;
        nextPlayer = change;
    }

    public Player getCurrentPlayer(){
        return currentPlayer;
    }

    public Player getNextPlayer(){
        return nextPlayer;
    }

    public Player getPlayerBlack(){
        return playerBlack;
    }

    public Player getPlayerWhite(){
        return playerWhite;
    }

    private void enableLogging(){
        logging = true;
    }

    private void disableLogging(){
        logging = false;
    }
    private void terminalInfo(String data){
        if(logging)
            System.out.println(data);
    }
}
