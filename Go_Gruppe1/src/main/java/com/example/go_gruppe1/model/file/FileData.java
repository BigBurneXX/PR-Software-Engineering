package com.example.go_gruppe1.model.file;

import java.util.List;

public record FileData(String player1Name, String player2Name, int boardSize, double komi, int handicaps,
                int byoyomiOverruns, int byoyomiTimeLimit, List<Move> moves) {
}
