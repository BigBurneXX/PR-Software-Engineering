module com.example.go_gruppe2 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.go_gruppe2 to javafx.fxml;
    exports com.example.go_gruppe2;
}