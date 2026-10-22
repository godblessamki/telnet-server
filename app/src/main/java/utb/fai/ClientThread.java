package utb.fai;

import java.io.*;
import java.net.*;

public class ClientThread extends Thread {

    private Socket clientSocket;

    public ClientThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (
            InputStream input = clientSocket.getInputStream();
            OutputStream output = clientSocket.getOutputStream()
        )
        {
            byte[] buffer = new byte[1024];
            int count;

            while ((count = input.read(buffer)) != -1) {
                output.write(buffer, 0, count);
                output.flush();
            }
        }
        catch (IOException e) {
            System.err.println("Klient odpojen nebo nastala chyba: " + e.getMessage());
        }
        finally {
            try {
                clientSocket.close();
            }
            catch (IOException e) {
                System.err.println("Chyba pri uzavirani socketu.");
            }
        }
    }
}
