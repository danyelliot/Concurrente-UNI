package com.concurrente.server.server;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ServerController {
    @FXML
    private Label portLabel;

    public void setPort(String port) {
        portLabel.setText("Puerto: " + port);
    }
}
