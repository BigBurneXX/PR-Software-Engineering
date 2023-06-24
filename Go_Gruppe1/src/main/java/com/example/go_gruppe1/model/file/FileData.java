package com.example.go_gruppe1.model.file;

import java.util.List;

/**
 * @param player1Name      black player name
 * @param player2Name      white player name
 * @param boardSize        size of board
 * @param komi             komi advantage for white
 * @param handicaps        advantage for black
 * @param byoyomiOverruns  # of byoyomi time periods
 * @param byoyomiTimeLimit time of byoyomi period
 * @param moves            moves of game
 *                         <p>
 *                         keeps track of data in file
 */
public record FileData(String player1Name, String player2Name, int boardSize, double komi, int handicaps,
                       int byoyomiOverruns, int byoyomiTimeLimit, List<Move> moves) {
}