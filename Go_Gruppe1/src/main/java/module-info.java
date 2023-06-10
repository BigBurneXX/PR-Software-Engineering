module com.example.go_gruppe1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires json.simple;


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
}