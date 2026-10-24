package utb.fai;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App {

    public static void main(String[] args) {
        int port = 12345;
        int max_threads = 10;    

        // Implement input parameter processing
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Neplatné číslo portu: " + args[0]);
                return;
            }
        }

        if (args.length > 1) {
            try {
                max_threads = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.err.println("Neplatný počet vláken: " + args[1]);
                return;
            }
        }

        if (port < 1 || port > 65535 || max_threads <= 0) {
            System.err.println("Parametry jsou mimo povolený rozsah.");
            return;
        }

        ExecutorService executor = Executors.newFixedThreadPool(max_threads);

        // Implementation of the main server loop
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientThread clientThread = new ClientThread(clientSocket);
                executor.execute(clientThread);
            }
        } catch (IOException e) {
            System.err.println("Chyba serveru: " + e.getMessage());
        } finally {
            executor.shutdown();
        }
    }
}
