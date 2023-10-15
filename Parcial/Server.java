import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    private static final int port = 222;
    private static final List<Socket> clients = new ArrayList<>();
    private static ServerSocket server;

    private static int numberSets = 3;
    private static int numbersPerSet = 3;

    public static void main(String[] args){
        PrepareInitialData();
        /*try {
            server = new ServerSocket(port);
            System.out.println("Server is running on port " + server.getLocalPort());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        new Thread(() -> {
            while (true) {
                try {
                    Socket client = server.accept();
                    clients.add(client);
                    System.out.println("New client connected from " + client.getInetAddress().getHostAddress());
                    new ClientHandler(client).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String message = scanner.nextLine();
                System.out.println("Sending message to " + clients.size() + " clients");
                System.out.println("Message: " + message);
                sendData(message);
            }
        }).start();*/
    }

    static void sendData(String message) {
        for (Socket client : clients) {
            try {
                client.getOutputStream().write((message + "\n").getBytes());
                client.getOutputStream().flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    static void PrepareInitialData(){
        String data = Integer.toString(numberSets) + " | " + Integer.toString(numbersPerSet) + " | ";
        Point[] numbers = new Point[numberSets];
        for(int i = 0; i < numberSets;i++){
            int x = Math.abs(new Random().nextInt() % 100);
            int y = Math.abs(new Random().nextInt() % 100);
            numbers[i] = new Point(x,y);
            data += numbers[i] + " ; ";
            for(int j = 1; j < numbersPerSet; j++){
                x = Math.abs(new Random().nextInt() % 100);
                y = Math.abs(new Random().nextInt() % 100);
                data += new Point(x,y);
                if(j != numbersPerSet - 1){
                    data += " ; ";
                }
            }
            if(i != numberSets - 1){
                data += " % ";
            }
        }
        data += " | ";
        for (int i = 0; i < numberSets; i++) {
            data += numbers[i];
            if(i != numberSets - 1){
                data += " ; ";
            }
        }
        System.out.println(data);
    }
}


class ClientHandler extends Thread{
    private final Socket client;
    private InputStream entry;
    public ClientHandler(Socket client){
        this.client = client;
    }
    public void run(){
        try{
            entry = client.getInputStream();
            Scanner scanner= new Scanner(entry);
            while(true){
                if(scanner.hasNextLine()) {
                    String message = scanner.nextLine();
                    System.out.println("Received from client: " + message);
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}