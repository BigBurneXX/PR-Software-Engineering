package com.example.go_gruppe1.model;

import javafx.scene.paint.Color;

public class GameHandler {
    private final Player playerBlack;
    private final Player playerWhite;
    private Player currentPlayer;
    private Player nextPlayer;
    private boolean logging;

    GameHandler(String playerBlackName, String playerWhiteName, Color lastColor){
        playerBlack = new Player(playerBlackName, Color.BLACK);
        playerWhite = new Player(playerWhiteName, Color.WHITE);
    }

    public void moveMade(){
        //modeAndMoveDisplay.setText(pl1.getText() + "'s turn!");
        currentPlayer.getTimer().stopTimer();
        nextPlayer.getTimer().startTimer();
        terminalInfo(String.valueOf(currentPlayer.getTimer().passedSlotSeconds()));
        changePlayer();
        //initiateByoyomiRules(2);
    }

    public void startTimer(){
        currentPlayer.getTimer().startTimer();
    }

    private void changePlayer(){
        Player change = currentPlayer;
        currentPlayer = nextPlayer;
        nextPlayer = change;
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
