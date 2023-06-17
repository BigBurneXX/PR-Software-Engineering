package com.example.go_gruppe1.model.player;

import javafx.scene.paint.Color;

public class PlayerHandler {
    private final Player playerBlack;
    private final Player playerWhite;
    private Player currentPlayer;
    private Player nextPlayer;

    public PlayerHandler(String playerBlackName, String playerWhiteName){
        final Color hoverBlack = Color.valueOf("#00000070");
        final Color hoverWhite = Color.valueOf("#FFFFFF70");

        playerBlack = new Player(playerBlackName, Color.BLACK, hoverBlack, new GoTimer());
        playerWhite = new Player(playerWhiteName, Color.WHITE, hoverWhite, new GoTimer());
        currentPlayer = playerBlack;
        nextPlayer = playerWhite;
    }

    public void moveMade(){
        currentPlayer.timer().stopTimer();
        nextPlayer.timer().startTimer();
        terminalInfo("passed seconds: " + currentPlayer.timer().getPassedSeconds());
        changePlayer();
    }

    public void startTimer(){
        currentPlayer.timer().startTimer();
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

    private void terminalInfo(String data){
        System.out.println(data);
    }
}
