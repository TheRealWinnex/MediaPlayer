module com.example.mediaplayer {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.example.mediaplayer to javafx.fxml;
    exports com.example.mediaplayer;
}