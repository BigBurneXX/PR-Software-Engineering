package com.example.go_gruppe2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;

public class inputMaskController {

    @FXML
    private Label boardSizeLabel;

    @FXML
    private RadioButton size9, size11, size13, size19;

    public void getBoardSize(ActionEvent event) {
        if(size9.isSelected()) {
            System.out.println("9x9");
        } else if(size11.isSelected()) {
            System.out.println("11x11");
        } else if(size13.isSelected()) {
            System.out.println("13x13");
        } else {
            System.out.println("19x19");
        }
    }


}