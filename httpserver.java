import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {
    private static final int PORT = 8080;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is listening on port " + PORT);
            while (true) {
                Socket socket = serverSocket.accept();
                new ClientHandler(socket).start();
            }
        } catch (IOException e) {
            System.err.println("Could not start server: " + e.getMessage());
        }
    }
}

class ClientHandler extends Thread {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
             
            // Read the request
            String requestLine = in.readLine();
            System.out.println("Received request: " + requestLine);

            // Prepare the response
            String httpResponse = "HTTP/1.1 200 OK\r\n" +
                                  "Content-Type: text/html\r\n" +
                                  "\r\n" +
                                  "<html>" +
                                  "<head><title>Simple HTTP Server</title></head>" +
                                  "<body><h1>Hello, World!</h1>" +
                                  "<p>This is a simple HTTP server.</p>" +
                                  "</body></html>";

            // Send the response
            out.println(httpResponse);
        } catch (IOException e) {
            System.err.println("Error handling client: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Could not close socket: " + e.getMessage());
            }
        }
    }
}
