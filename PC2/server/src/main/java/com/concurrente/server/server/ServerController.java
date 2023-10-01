package com.concurrente.server.server;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Random;
import java.util.Vector;

public class ServerController {
    @FXML
    private Label portLabel;

    private ServerSocket serverSocket;
    private Vector<ClientData> clients;
    private Vector<Vector2> initialPoints;
    public ServerController() {
        clients = new Vector<>();
        initialPoints = new Vector<>();
        int maxX = 1920;
        int maxY = 1000;

        for (int x = 0; x <= maxX; x++) {
            for (int y = 0; y <= maxY; y++) {
                Vector2 point = new Vector2(x, y);
                initialPoints.add(point);
            }
        }

    }

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
            int index = clients.size();
            System.out.println("Cliente "+ index+ " conectado: " + clientSocket.getInetAddress());
            clients.add(new ClientData(index));
            OutputStream outputStream = clientSocket.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            dataOutputStream.writeInt(index);
            Random random = new Random();
            int r = random.nextInt(initialPoints.size());
            Vector2 point = initialPoints.get(r);
            dataOutputStream.writeInt(point.getX());
            dataOutputStream.writeInt(point.getY());

            while (true){

            }

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
