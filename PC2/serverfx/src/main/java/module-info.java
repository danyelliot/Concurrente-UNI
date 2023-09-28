module concurrente.server.serverfx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens concurrente.server.serverfx to javafx.fxml;
    exports concurrente.server.serverfx;
}