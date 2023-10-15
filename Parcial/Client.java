import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.io.OutputStream;

public class Client {
    private static final int port = 222;
    private static final String host = "localhost";
    private static Socket client;

    public static void main(String[] args){
        try {
            client = new Socket(host, port);
            System.out.println("Client has connected to server on port " + client.getPort());
            Thread receiveThread = new Thread(() -> {
                try (Scanner scanner = new Scanner(client.getInputStream())) {
                    while (true) {
                        if (scanner.hasNextLine()) {
                            String message = scanner.nextLine();
                            if ("EXIT".equals(message)) {
                                System.out.println("Server has closed the connection. Exiting...");
                                break;
                            }
                            System.out.println("Server says: " + message);
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            receiveThread.start();

            OutputStream outputStream = client.getOutputStream();
                Scanner scanner = new Scanner(System.in);
                while (true) {
                    String message = scanner.nextLine();
                    outputStream.write((message + "\n").getBytes());
                    outputStream.flush();
                }



        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
