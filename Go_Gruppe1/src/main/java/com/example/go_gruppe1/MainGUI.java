package com.example.go_gruppe1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MainGUI extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/inputMaskGUI.fxml")));
        Scene inputMask = new Scene(root);
        stage.setTitle("Board game Go");
        stage.setResizable(true);

        //Image icon = new Image(new File("/src/main/java/com/example/go_gruppe1/go.png").toURI().toString());
        //stage.getIcons().add(icon);
        stage.setScene(inputMask);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}