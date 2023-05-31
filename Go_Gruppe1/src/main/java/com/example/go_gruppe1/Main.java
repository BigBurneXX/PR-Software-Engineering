package com.example.go_gruppe1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        /*Parent root = FXMLLoader.load(getClass().getResource("/inputMaskGUI.fxml"));
        Scene inputMask = new Scene(root);
        stage.setTitle("Board game Go");
        stage.setResizable(true);
        inputMaskController boardMask = loader.getController();

        //Image icon = new Image(new File("/src/main/java/com/example/go_gruppe1/go.png").toURI().toString());
        //stage.getIcons().add(icon);
        stage.setScene(inputMask);
        //stage.sizeToScene();
        stage.setMinWidth(630);
        stage.setMinHeight(500);
        stage.centerOnScreen();
        stage.show();*/

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