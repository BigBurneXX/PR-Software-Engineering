package com.example.go_gruppe1.model.player;

import javafx.scene.paint.Color;

public record Player(String name, Color color, Color hoverColor, GoTimer timer) {
}