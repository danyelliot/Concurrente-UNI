module com.concurrente.server.server {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires com.google.gson;

    opens com.concurrente.server.server to javafx.fxml;
    exports com.concurrente.server.server;
}