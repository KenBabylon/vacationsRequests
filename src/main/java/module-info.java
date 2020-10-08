module ug.grupo2.project {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires com.jfoenix;

    opens ug.grupo2.project to javafx.fxml;

    exports ug.grupo2.project;
}