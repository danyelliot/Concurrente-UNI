import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MasterNode {
    private static final int PORT = 9191;
    public static void main(String[] args) {
        MasterNode masterNode = new MasterNode();
        masterNode.startServer();
    }
    public void startServer() {
        ExecutorService executorService = Executors.newCachedThreadPool();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Nodo maestro iniciado en el puerto: " + PORT);
            Socket redirectionServerSocket = serverSocket.accept();
            System.out.println("Nueva conexión entrante Servidor Redireccion: " + redirectionServerSocket);
            Thread redirectionServer = new Thread(){
                public void run(){
                    try (BufferedReader input = new BufferedReader(new InputStreamReader(redirectionServerSocket.getInputStream()))) {
                        String serverMessage;
                        System.out.println("Esperando mensaje del servidor de redirección...");
                        while ((serverMessage = input.readLine()) != null) {
                            System.out.println("Mensaje recibido del servidor de redirección: " + serverMessage);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }    
            };
            redirectionServer.start();
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Nueva conexión entrante: " + clientSocket);

                SlaveNodeRequestHandler requestHandler = new SlaveNodeRequestHandler(clientSocket);
                executorService.execute(requestHandler);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
    }

    class SlaveNodeRequestHandler implements Runnable {
        private Socket clientSocket;

        public SlaveNodeRequestHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            // Lógica para manejar las solicitudes de los nodos esclavos
            // ...
        }
    }
}
  

