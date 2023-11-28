import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 8080;

    public static void main(String[] args) {
        Client client = new Client();
        client.startClient();
    }

    public void startClient() {
        try{
            Socket socket = new Socket(SERVER_IP, SERVER_PORT);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Conectado al servidor de redirección.");
            while(true){
                String command = "";
                System.out.print("Ingrese el comando: ");
                command = userInput.readLine();
                if (command.equals("exit")) {
                    break;
                }
                out.println(command);
                while (true) {
                    String res = in.readLine();
                    if (res == null || res.isEmpty()) {
                        continue;
                    }
                    System.out.println(res);
                    break;
                }
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}