package Clases;

import javax.crypto.*;
import javax.crypto.spec.*;
import java.net.*;
import java.security.*;
import java.io.*;

public class SecureServer {
    private static final int PORT = 8000;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server listening on port " + PORT);

            while (true) {
                Socket socket = serverSocket.accept();
                new ClientHandler(socket).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler extends Thread {
        private final Socket socket;

        ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (DataInputStream fromClient = new DataInputStream(socket.getInputStream());
                 DataOutputStream toClient = new DataOutputStream(socket.getOutputStream())) {

                // Recibir mensaje cifrado y medir tiempo de descifrado
                long startTime = System.nanoTime();
                int messageLength = fromClient.readInt();
                byte[] encryptedMessage = new byte[messageLength];
                fromClient.readFully(encryptedMessage);
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
                byte[] decryptedMessage = cipher.doFinal(encryptedMessage);
                long endTime = System.nanoTime();
                System.out.println("Descifrar mensaje tomó: " + (endTime - startTime) + " ns");

                // Procesar y responder
                int number = Integer.parseInt(new String(decryptedMessage));
                int responseNumber = number - 1;
                byte[] responseBytes = Integer.toString(responseNumber).getBytes();
                cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
                byte[] encryptedResponse = cipher.doFinal(responseBytes);

                toClient.writeInt(encryptedResponse.length);
                toClient.write(encryptedResponse);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}