module com.maruful {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.maruful to javafx.fxml;
    exports com.maruful;
}
