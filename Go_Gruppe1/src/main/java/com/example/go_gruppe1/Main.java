package com.example.go_gruppe1;

import com.example.go_gruppe1.controller.inputMaskController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/inputMaskGUI.fxml"));
        Parent root = loader.load();

        inputMaskController inputMask = loader.getController();
        inputMask.initiateDisplay();

        Scene scene = new Scene(root);

        stage.setTitle("Board game Go");
        stage.setResizable(true);
        stage.setScene(scene);
        stage.setMinWidth(630);
        stage.setMinHeight(500);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}