import java.io.IOException;
import java.net.Socket;

public class SlaveNode {
    private static final String MASTER_IP = "127.0.0.1"; // IP del nodo maestro
    private static final int MASTER_PORT = 9090; // Puerto del nodo maestro

    public static void main(String[] args) {
        SlaveNode slaveNode = new SlaveNode();
        slaveNode.connectToMaster();
    }

    public void connectToMaster() {
        try (Socket socket = new Socket(MASTER_IP, MASTER_PORT)) {
            System.out.println("Conectado al nodo maestro en " + MASTER_IP + ":" + MASTER_PORT);

            // Lógica para enviar y recibir mensajes con el nodo maestro
            // ...

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
