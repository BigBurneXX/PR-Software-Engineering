package com.example.go_gruppe1.model;

import javafx.scene.paint.Color;

public class PlayerHandler {
    private final Player playerBlack;
    private final Player playerWhite;
    private Player currentPlayer;

    PlayerHandler(String playerBlackName, String playerWhiteName, Color lastColor){
        playerBlack = new Player(playerBlackName, Color.BLACK);
        playerWhite = new Player(playerWhiteName, Color.WHITE);
    }
}
