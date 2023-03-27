package com.example.go_gruppe2;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
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
        Parent root = FXMLLoader.load(getClass().getResource("inputMaskGUI.fxml"));
        Scene inputMask = new Scene(root, 600, 400, Color.BEIGE);
        stage.setTitle("Boardgame Go");
        stage.setResizable(true);

        Image icon = new Image(new FileInputStream("src/go.png"));
        stage.getIcons().add(icon);
        stage.setScene(inputMask);
        stage.show();


        /*Group root = new Group();

        //input mask
        Scene inputMask = new Scene(root, 600, 400, Color.BEIGE);
        stage.setTitle("Boardgame Go");
        stage.setResizable(true);

        Image icon = new Image(new FileInputStream("src/go.png"));
        stage.getIcons().add(icon);

        //header
        Text header = new Text();
        header.setText("Welcome to Go!");
        header.setFont(Font.font("Cambria", 40));
        header.setFill(Color.CHOCOLATE);
        header.setWrappingWidth(565);
        header.setTextAlignment(TextAlignment.CENTER);

        //center header initially
        header.layoutXProperty().bind(inputMask.widthProperty().divide(2).subtract(header.prefWidth(-1)/2));
        header.layoutYProperty().bind(inputMask.heightProperty().divide(6));

        //adjust font size dynamically
        ObjectBinding<Font> fontBinding = Bindings.createObjectBinding(() ->
                        Font.font("Cambria", inputMask.getHeight() / 10),
                inputMask.heightProperty());
        header.fontProperty().bind(fontBinding);

        root.getChildren().add(header);

        //resize listener for header
        inputMask.widthProperty().addListener((obs, oldVal, newVal) ->
                header.layoutXProperty().
                        bind(inputMask.widthProperty().
                                divide(2).subtract(header.prefWidth(-1)/2)));
        inputMask.heightProperty().addListener((obs, oldVal, newVal) ->
                header.layoutYProperty().
                        bind(inputMask.heightProperty().divide(6)));



        stage.setScene(inputMask);
        stage.show();*/
    }

    public static void main(String[] args) {
        launch(args);
    }
}