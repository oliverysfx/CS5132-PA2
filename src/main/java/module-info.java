module com.example.treechooser {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.treechooser to javafx.fxml;
    opens com.example.treechooser.controllers to javafx.fxml;
    exports com.example.treechooser;
}