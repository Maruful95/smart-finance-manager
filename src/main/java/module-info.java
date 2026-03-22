module com.maruful {
  requires javafx.controls;
  requires javafx.fxml;
  requires transitive javafx.graphics;

  opens com.maruful to javafx.fxml;
  opens com.maruful.gui to javafx.fxml;
  exports com.maruful;
}
