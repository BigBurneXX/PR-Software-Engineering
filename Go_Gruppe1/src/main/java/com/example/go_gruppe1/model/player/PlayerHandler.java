package com.example.go_gruppe1.model.player;

import javafx.scene.paint.Color;

public class PlayerHandler {
    private final Player playerBlack;
    private final Player playerWhite;
    private final int byoyomiTimeLimit;
    private final boolean timerActive;
    private Player currentPlayer;
    private Player nextPlayer;


    /**
     * @param playerBlackName black player name
     * @param playerWhiteName white player name
     *
     * initiates both Go players (black and white) with no byoyomi rules
     */
    public PlayerHandler(String playerBlackName, String playerWhiteName){
        this(playerBlackName, playerWhiteName, 0, 0);
    }

    /**
     * @param playerBlackName black player name
     * @param playerWhiteName white player name
     * @param byoyomiOverruns # of byoyomi time periods
     * @param byoyomiTimeLimit time of byoyomi period
     *
     * initiates both Go players (black and white) with byoyomi rules
     */
    public PlayerHandler(String playerBlackName, String playerWhiteName, int byoyomiOverruns, int byoyomiTimeLimit){
        final Color hoverBlack = Color.valueOf("#00000070");
        final Color hoverWhite = Color.valueOf("#FFFFFF70");

        terminalInfo("Number of Byoyomi time overruns: " + byoyomiOverruns);
        terminalInfo("Byoyomi time limit: " + byoyomiTimeLimit);
        this.byoyomiTimeLimit = byoyomiTimeLimit;
        timerActive = (byoyomiOverruns != 0);

        playerBlack = new Player(playerBlackName, Color.BLACK, hoverBlack, byoyomiOverruns, byoyomiTimeLimit);
        playerWhite = new Player(playerWhiteName, Color.WHITE, hoverWhite, byoyomiOverruns, byoyomiTimeLimit);
        currentPlayer = playerBlack;
        nextPlayer = playerWhite;
    }

    /**
     * when a move is made, the current player's timer is stopped
     * and the other's is started and the current player is changed
     */
    public void moveMade(){
        if(timerActive) {
            currentPlayer.getTimer().stopTimer();
            nextPlayer.getTimer().startTimer();
        }
        changePlayer();
    }

    /**
     * if there are byoyomi rules, the current player's timer is started
     */
    public void startTimer(){
        if(timerActive)
            currentPlayer.getTimer().startTimer();
    }

    /**
     * changes current player to next and vice versa
     */
    public void changePlayer(){
        Player change = currentPlayer;
        currentPlayer = nextPlayer;
        nextPlayer = change;
    }

    /**
     * @return current player
     */
    public Player getCurrentPlayer(){
        return currentPlayer;
    }

    /**
     * @return next player
     */
    public Player getNextPlayer(){
        return nextPlayer;
    }

    /**
     * @return black player
     */
    public Player getPlayerBlack(){
        return playerBlack;
    }

    /**
     * @return white player
     */
    public Player getPlayerWhite(){
        return playerWhite;
    }

    /**
     * @return true if player has used all time periods
     * <p>
     * if byoyomi time rules are set, checks if last move surpassed time period limit
     * and if there are time periods left
     */
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

    /**
     * @param data
     *
     * prints info to console for debugging
     */
    private void terminalInfo(String data){
        System.out.println(data);
    }
}
