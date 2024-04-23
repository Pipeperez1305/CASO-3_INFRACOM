package Clases;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SecureCommunicationConsole {
    private static final int PORT = 8000;
    private static SecureServer server;

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String input;
        boolean exit = false;

        startServer(); // Assume we start the server automatically

        while (!exit) {
            System.out.println("\nSelect an option:");
            System.out.println("1. Run 4 clients");
            System.out.println("2. Run 16 clients");
            System.out.println("3. Run 32 clients");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");
            input = reader.readLine();

            switch (input) {
                case "1":
                    runClients(4);
                    break;
                case "2":
                    runClients(16);
                    break;
                case "3":
                    runClients(32);
                    break;
                case "4":
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }

        System.out.println("Exiting...");
        System.exit(0);
    }

    private static void startServer() {
        System.out.println("Starting server...");
        // You should start the server thread here
    }

    private static void runClients(int numberOfClients) {
        ExecutorService executor = Executors.newFixedThreadPool(numberOfClients);
        System.out.println("Running " + numberOfClients + " clients...");

        for (int i = 0; i < numberOfClients; i++) {
            executor.submit(() -> simulateClient());
        }

        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static void simulateClient() {
        // Simulate client operations and measure time
        long startTime = System.nanoTime(); // Start time
        // Simulate operations e.g., connect to server, send data, receive data
        long endTime = System.nanoTime(); // End time

        System.out.println("Client operation completed in " + ((endTime - startTime) / 1_000_000) + " ms");
    }
}
