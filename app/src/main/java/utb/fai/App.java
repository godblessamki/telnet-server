package utb.fai;
import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import java.net.ServerSocket;

public class App {

    public static void main(String[] args) {    
        if (args.length != 2) {
            System.err.println("Spatny pocet argumentu");
            return;
        }

        try {
            int port = Integer.parseInt(args[0]);
            int maxThreads = Integer.parseInt(args[1]);

            if (port < 1 || port > 65535 || maxThreads < 1) {
                System.err.println("Navlidni port nebo pocet vlaken.");
                return;
            }

            ThreadPoolExecutor executor = new ThreadPoolExecutor(
                maxThreads,
                maxThreads,
                0L,
                TimeUnit.MILLISECONDS,
                new SynchronousQueue<>()
            );

            try (ServerSocket serverSocket = new ServerSocket(port)) {
                System.out.println("Server bezi na portu: " + port);

                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("KLient pripojen");

                    try {
                        executor.execute(new ClientThread(clientSocket));
                    }
                    catch (RejectedExecutionException e){
                        System.out.println("Server je plny.");
                        clientSocket.close();
                    }
                }
            }
            finally {
                executor.shutdownNow();
            }
        }
        catch (NumberFormatException e) {
            System.err.println("Port a pocet vlaken musi byt cele cislo.");
        }
        catch (IOException e) {
            System.err.println("Chyba serveru: " + e.getMessage());
        }
    }
}
