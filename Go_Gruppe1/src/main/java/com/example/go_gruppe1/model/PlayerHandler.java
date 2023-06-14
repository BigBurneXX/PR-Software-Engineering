package com.example.go_gruppe1.model;

import javafx.scene.paint.Color;

public class PlayerHandler {
    private final Player playerBlack;
    private final Player playerWhite;
    private Player currentPlayer;
    private Player nextPlayer;
    private boolean logging;

    public PlayerHandler(String playerBlackName, String playerWhiteName){
        final Color hoverBlack = Color.valueOf("#00000070");
        final Color hoverWhite = Color.valueOf("#FFFFFF70");

        playerBlack = new Player(playerBlackName, Color.BLACK, hoverBlack);
        playerWhite = new Player(playerWhiteName, Color.WHITE, hoverWhite);
        currentPlayer = playerBlack;
        nextPlayer = playerWhite;
    }

    public void moveMade(){
        currentPlayer.getTimer().stopTimer();
        nextPlayer.getTimer().startTimer();
        terminalInfo("passed seconds: " + currentPlayer.getTimer().getPassedSeconds());
        changePlayer();
    }

    public void startTimer(){
        currentPlayer.getTimer().startTimer();
    }

    public void changePlayer(){
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

    public void setLogging(boolean logging){
        this.logging = logging;
    }

    private void terminalInfo(String data){
        if(logging)
            System.out.println(data);
    }
}
