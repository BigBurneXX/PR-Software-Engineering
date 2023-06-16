module com.example.go_gruppe1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires json.simple;
    requires com.google.gson;


    opens com.example.go_gruppe1 to javafx.fxml;
    exports com.example.go_gruppe1;
    exports com.example.go_gruppe1.model.command;
    opens com.example.go_gruppe1.model.command to javafx.fxml;
    exports com.example.go_gruppe1.controller;
    opens com.example.go_gruppe1.controller to javafx.fxml;
    exports com.example.go_gruppe1.model;
    opens com.example.go_gruppe1.model to javafx.fxml;
    exports com.example.go_gruppe1.oldClasses;
    opens com.example.go_gruppe1.oldClasses to javafx.fxml;
    exports com.example.go_gruppe1.model.file;
    opens com.example.go_gruppe1.model.file to javafx.fxml;
    exports com.example.go_gruppe1.model.player;
    opens com.example.go_gruppe1.model.player to javafx.fxml;
}