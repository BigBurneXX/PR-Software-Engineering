package com.example.go_gruppe1;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;

public class MainGUI extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/inputMaskGUI.fxml"));
        Scene inputMask = new Scene(root);
        stage.setTitle("Boardgame Go");
        stage.setResizable(true);

        Image icon = new Image(new FileInputStream("src/go.png"));
        stage.getIcons().add(icon);
        stage.setScene(inputMask);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}