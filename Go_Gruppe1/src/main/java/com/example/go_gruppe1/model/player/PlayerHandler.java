package com.example.go_gruppe1.model.player;

import javafx.scene.paint.Color;

public class PlayerHandler {
    private final Player playerBlack;
    private final Player playerWhite;
    private final int byoyomiOverruns;
    private final int byoyomiTimeLimit;
    private final boolean timerActive;
    private Player currentPlayer;
    private Player nextPlayer;


    public PlayerHandler(String playerBlackName, String playerWhiteName){
        this(playerBlackName, playerWhiteName, 0, 0);
    }

    public PlayerHandler(String playerBlackName, String playerWhiteName, int byoyomiOverruns, int byoyomiTimeLimit){
        final Color hoverBlack = Color.valueOf("#00000070");
        final Color hoverWhite = Color.valueOf("#FFFFFF70");

        terminalInfo("Number of Byoyomi time overruns: " + byoyomiOverruns);
        terminalInfo("Byoyomi time limit: " + byoyomiTimeLimit);
        this.byoyomiOverruns = byoyomiOverruns;
        this.byoyomiTimeLimit = byoyomiTimeLimit;
        timerActive = (byoyomiOverruns != 0);

        playerBlack = new Player(playerBlackName, Color.BLACK, hoverBlack, byoyomiOverruns, byoyomiTimeLimit);
        playerWhite = new Player(playerWhiteName, Color.WHITE, hoverWhite, byoyomiOverruns, byoyomiTimeLimit);
        currentPlayer = playerBlack;
        nextPlayer = playerWhite;
    }

    public void moveMade(){
        if(timerActive) {
            currentPlayer.getTimer().stopTimer();
            nextPlayer.getTimer().startTimer();
            //terminalInfo("passed seconds: " + currentPlayer.getTimer().getPassedSeconds());
        }
        changePlayer();
    }

    public void startTimer(){
        if(timerActive)
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

    public int getByoyomiOverruns(){
        return byoyomiOverruns;
    }

    public int getByoyomiTimeLimit(){
        return byoyomiTimeLimit;
    }

    public boolean checkByoyomi() {
        if (timerActive) {
            int passedSeconds = getCurrentPlayer().getTimer().getPassedSeconds();

            if(passedSeconds > byoyomiTimeLimit) {
                int passedSlots = passedSeconds / byoyomiTimeLimit;
                int currentByoyomi = getCurrentPlayer().getByoyomi() - passedSlots;
                getCurrentPlayer().setByoyomi(currentByoyomi);

                if(currentByoyomi < 0) {
                    getCurrentPlayer().setTimeLabelText("No time left");
                    return true;
                }
                getCurrentPlayer().setTimeLabelText(currentByoyomi + " time period(s) à " + byoyomiTimeLimit + " s");
            }
        }
        return false;
    }

    private void terminalInfo(String data){
        System.out.println(data);
    }
}
