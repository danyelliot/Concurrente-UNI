package com.concurrente.server.server;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerController {
    @FXML
    private Label portLabel;

    private ServerSocket serverSocket;

    public void setPort(String port) {
        portLabel.setText("Puerto: " + port);
        startServer(port);

    }
    public void startServer(String portString) {
        Thread serverThread = new Thread(() -> {
            try {
                int port = Integer.parseInt(portString);
                serverSocket = new ServerSocket(port);
                System.out.println("Servidor iniciado en el puerto " + port + ". Esperando conexiones...");

                while (true) {
                    Socket clientSocket = serverSocket.accept();

                    // Inicia un nuevo hilo para manejar la conexión del cliente
                    Thread clientThread = new Thread(() -> handleClient(clientSocket));
                    clientThread.start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        serverThread.start();
    }

    private void handleClient(Socket clientSocket) {
        try {
            // Obtén el InputStream para recibir datos del cliente
            InputStream inputStream = clientSocket.getInputStream();

            // Lee y procesa los datos recibidos (puedes personalizar esta lógica)
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                String data = new String(buffer, 0, bytesRead);
                System.out.println("Datos recibidos de " + clientSocket.getInetAddress() + ": " + data);
            }

            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para detener el servidor *en revisión
    public void stopServer() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                System.out.println("Servidor detenido.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
