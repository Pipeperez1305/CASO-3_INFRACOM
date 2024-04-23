package Clases;

import javax.crypto.*;
import javax.crypto.spec.*;
import java.net.*;
import java.security.*;
import java.io.*;

public class SecureClient {

    private static final String HOST = "localhost";
    private static final int PORT = 8000;
    private static long startTime, endTime;

    public static void main(String[] args) {
        try (Socket socket = new Socket(HOST, PORT);
             DataOutputStream toServer = new DataOutputStream(socket.getOutputStream());
             DataInputStream fromServer = new DataInputStream(socket.getInputStream())) {

            // Cifrar consulta
            startTime = System.nanoTime();
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            // Suposición: keySpec e ivSpec ya están inicializados correctamente
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
            byte[] encryptedMessage = cipher.doFinal("42".getBytes());
            endTime = System.nanoTime();
            System.out.println("Cifrar consulta tomó: " + (endTime - startTime) + " ns");

            // Enviar mensaje cifrado
            toServer.writeInt(encryptedMessage.length);
            toServer.write(encryptedMessage);

            // Recibir y descifrar la respuesta
            int responseLength = fromServer.readInt();
            byte[] encryptedResponse = new byte[responseLength];
            fromServer.readFully(encryptedResponse);

            startTime = System.nanoTime();
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            byte[] decryptedResponse = cipher.doFinal(encryptedResponse);
            endTime = System.nanoTime();
            System.out.println("Descifrar consulta tomó: " + (endTime - startTime) + " ns");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
