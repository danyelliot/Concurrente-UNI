package com.concurrente.server.server;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.Vector;

public class ServerController {
    @FXML
    private Label portLabel;
    @FXML
    private ListView<String> eventListView;

    @FXML
    private Button closeButton;
    private ServerSocket serverSocket;
    private Vector<ClientData> clients;
    private Vector<Socket> sockets;
    private Vector<Vector2> initialPoints;
    private String event;
    @FXML
    private Label ipLabel;
    private boolean isServerRunning = true;

    public void initialize() {
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            String ipAddress = localHost.getHostAddress();
            ipLabel.setText("IP: " + ipAddress);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

                while (isServerRunning) {
                    if (!isServerRunning) {
                        break; //
                    }
                    Socket clientSocket = serverSocket.accept();
                    // Inicia un nuevo hilo para manejar la conexión del cliente
                    Thread clientThread = new Thread(() -> handleClient(clientSocket));
                    clientThread.start();
                }
            } catch (IOException e) {
              //  e.printStackTrace();
            }
        });
        serverThread.start();
        Thread sendThread = new Thread(this::sendDataToClients);
        sendThread.start();
    }

    private void addEventToList(String message) {
        Platform.runLater(() -> {
            ObservableList<String> items = eventListView.getItems();
            items.add(message);
        });
    }


    private void handleClient(Socket clientSocket) {
        try {
            int index = clients.size();
            String clientMessage = " Cliente " + index + " se ha conectado\t\t   IP: " + clientSocket.getInetAddress().getHostAddress();
            addEventToList(clientMessage);

            OutputStream outputStream = clientSocket.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            dataOutputStream.writeInt(index);
            Vector2 point = initialPoints.get(new Random().nextInt(initialPoints.size()));
            dataOutputStream.writeFloat(point.getX());
            dataOutputStream.writeFloat(point.getY());
            for (ClientData client : clients) {
                dataOutputStream.writeInt(client.getIndex());
                dataOutputStream.writeFloat(client.getX());
                dataOutputStream.writeFloat(client.getY());
            }
            dataOutputStream.flush();
            ClientData clientData = new ClientData(index, clientSocket);
            clients.add(clientData);
            InputStream inputStream = clientSocket.getInputStream();
            while (true){
                byte[] buffer = new byte[1024];
                String data = new String(buffer,0,inputStream.read(buffer));
                if(data.equals("update")){
                    byte[] buffer2 = new byte[1024];
                    String data2 = new String(buffer2,0,inputStream.read(buffer2));
                    String[] dataSplit = data2.split(",");
                    clients.get(Integer.parseInt(dataSplit[0])).update(Float.parseFloat(dataSplit[1]),Float.parseFloat(dataSplit[2]),Float.parseFloat(dataSplit[3]));
                }else if (data.equals("disconnect")){
                    byte[] buffer2 = new byte[1024];
                    String data2 = new String(buffer2,0,inputStream.read(buffer2));
                    int indexTemp = Integer.parseInt(data2);
                    clients.get(indexTemp).setActive(false);
                    String disconnectMessage = " Cliente " + indexTemp + " se ha desconectado";
                    addEventToList(disconnectMessage);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void stopServer() {
        isServerRunning = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                // Cierra todos los sockets de los clientes conectados
                for (ClientData client : clients) {
                    try {
                        client.getSocket().close();
                    } catch (IOException e) {

                        throw new RuntimeException(e);

                    }
                }
                // Cierra el socket del servidor
                serverSocket.close();
                System.out.println("Servidor detenido.");
                System.exit(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    private void sendDataToClients() {
        OutputStream outputStream;
        DataOutputStream dataOutputStream;
        while (true) {
            try {
                Thread.sleep(100);
                for (ClientData client : clients) {

                    if (!client.isActive()) {
                        continue;
                    }
                    outputStream = client.getSocket().getOutputStream();
                    for (ClientData clientData : clients) {
                        String data = clients.size() + "," + clientData.getIndex() + "," + clientData.getX() + "," + clientData.getY() + "," + clientData.getRotation() + "," + clientData.isActive();
                        outputStream.write(data.getBytes());
                    }
                    outputStream.flush();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
