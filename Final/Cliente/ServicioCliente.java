import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;

import com.sun.net.httpserver.*;

public class ServicioCliente{
    public static void main(String[] args) throws IOException {
        int puerto = 8000;
        HttpServer servidor = HttpServer.create(new InetSocketAddress(puerto), 0);
        servidor.createContext("/", new Handler("index.html"));        
        servidor.createContext("/public", new Handler(""));
        servidor.createContext("/cart", new Handler("cart.html"));
        servidor.start();
        System.out.println("Servidor HTTP iniciado en el puerto " + puerto);
        System.out.println("* http://localhost:" + puerto);
    }
    static class Handler implements HttpHandler {
        String rutaPublic = "public/";
        public Handler(String ruta) {
            this.rutaPublic += ruta;
        }
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String fileString = exchange.getRequestURI().getPath();
            File archivoSolicitado;
            if (fileString.startsWith("/public")) {
                archivoSolicitado = new File(fileString.substring(1));
            }else{
                archivoSolicitado = new File(rutaPublic);
            }
            if (archivoSolicitado.exists() && archivoSolicitado.isFile()) {
                byte[] archivoBytes = Files.readAllBytes(archivoSolicitado.toPath());

                exchange.sendResponseHeaders(200, archivoBytes.length);

                OutputStream os = exchange.getResponseBody();
                os.write(archivoBytes);
                os.close();
            } else {
                String respuesta = "Archivo no encontrado";
                exchange.sendResponseHeaders(404, respuesta.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(respuesta.getBytes());
                os.close();
            }
        }
    }
}