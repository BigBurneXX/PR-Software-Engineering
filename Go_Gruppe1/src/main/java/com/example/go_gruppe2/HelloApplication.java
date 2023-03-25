package com.example.go_gruppe2;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Group root = new Group();

        //input mask
        Scene inputMask = new Scene(root, Color.BEIGE);
        stage.setTitle("Boardgame Go");
        stage.setResizable(true);

        Text header = new Text();
        header.setText("Welcome to Go!");
        header.setFont(Font.font("Cambria", 40));
        header.setFill(Color.CHOCOLATE);
        header.setX(50);
        header.setY(50);
        header.setWrappingWidth(565);
        header.setTextAlignment(TextAlignment.CENTER);
        root.getChildren().add(header);

        stage.setScene(inputMask);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}