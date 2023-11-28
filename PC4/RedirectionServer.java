import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class RedirectionServer {
    private static final int PORT = 8080;
    private static final String MASTER_NODE_IP = "127.0.0.1";
    private static final int MASTER_NODE_PORT = 9191;
    public static Socket masterNodeSocket;
    public static PrintWriter outToMaster;
    public static void main(String[] args) {
        RedirectionServer redirectionServer = new RedirectionServer();
        redirectionServer.startServer();
    }

    public void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Servidor de Redirección iniciado en el puerto: " + PORT);
            masterNodeSocket = new Socket(MASTER_NODE_IP, MASTER_NODE_PORT);
            outToMaster = new PrintWriter(masterNodeSocket.getOutputStream(), true);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Nueva conexión entrante: " + clientSocket);

                ClientRequestHandler requestHandler = new ClientRequestHandler(clientSocket);
                new Thread(requestHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClientRequestHandler implements Runnable {
        private Socket clientSocket;

        public ClientRequestHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String clientMessage;
                while ((clientMessage = input.readLine()) != null) {
                    System.out.println("Esperando mensaje del cliente...");
                    System.out.println("Mensaje recibido del cliente: " + clientMessage);
                    String[] command = parseCommand(clientMessage);
                    if (command[0].equals("error")) {
                        clientSocket.getOutputStream().write("Error en el comando\n".getBytes());
                    } else {
                        sendDataToMaster(command[0],command[1]);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String[] parseCommand(String command) {
            try {
                String[] parts = command.split("-");
                String type = parts[0];
                if (!type.equals("L") && !type.equals("A")) {
                    throw new Exception();
                }
                return new String[]{type, parts[1]};
            } catch (Exception e) {
                return new String[]{"error"};
            }
        }

        void sendDataToMaster(String type, String data) {
            try {
                RedirectionServer.outToMaster.println(type+data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}