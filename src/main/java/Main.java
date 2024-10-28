import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        // You can use print statements as follows for debugging, they'll be visible when running tests.
        System.out.println("Logs from your program will appear here!");

        ServerSocket serverSocket = null;
        Socket clientSocket = null;

        try {
            serverSocket = new ServerSocket(4221);
            serverSocket.setReuseAddress(true);
            clientSocket = serverSocket.accept(); // Wait for connection from client.
            System.out.println("accepted new connection");

            // Create an InputStream from the client socket.
            BufferedReader inputStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            // Read the request line from the HTTP request.
            String requestLine = inputStream.readLine();
            System.out.println("Received request: " + requestLine);

            // Extract the URL path from the request line.
            String urlPath = requestLine.split(" ")[1];

            // Create an OutputStream from the client socket.
            OutputStream outputStream = clientSocket.getOutputStream();

            // Write the HTTP response to the output stream.
            String httpResponse;
            if ("/".equals(urlPath)) {
                httpResponse = "HTTP/1.1 200 OK\r\n\r\n";
            } else {
                httpResponse = "HTTP/1.1 404 Not Found\r\n\r\n";
            }
            outputStream.write(httpResponse.getBytes("UTF-8"));

            // Close the input and output streams.
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }  finally {
            // Close the client socket and server socket.
            try {
                if (clientSocket != null) {
                    clientSocket.close();
                }
                if (serverSocket != null) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                System.out.println("IOException: " + e.getMessage());
            }
        }
    }
}