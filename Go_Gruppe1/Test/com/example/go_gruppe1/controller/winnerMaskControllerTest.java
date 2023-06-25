package com.example.go_gruppe1.controller;

import javafx.scene.layout.GridPane;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class winnerMaskControllerTest {

    @Test
    void testSetSize() {
        winnerMaskController controller = new winnerMaskController();
        controller.pane = new GridPane();

        double width = 100.0;
        double height = 200.0;

        controller.setSize(width, height);

        assertEquals(width, controller.pane.getPrefWidth());
        assertEquals(height, controller.pane.getPrefHeight());
    }

    @Test
    void testGetWidth() {
        winnerMaskController controller = new winnerMaskController();
        controller.pane = new GridPane();

        double width = 100.0;
        controller.pane.setPrefWidth(width);

        assertEquals(width, controller.getWidth());
    }

    @Test
    void testGetHeight() {
        winnerMaskController controller = new winnerMaskController();
        controller.pane = new GridPane();

        double height = 200.0;
        controller.pane.setPrefHeight(height);

        assertEquals(height, controller.getHeight());
    }
}