module com.example.go_gruppe1 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.go_gruppe1 to javafx.fxml;
    exports com.example.go_gruppe1;
}