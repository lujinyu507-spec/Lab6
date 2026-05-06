package org.example.client;

import org.example.client.console.ConsoleInputReader;
import org.example.common.model.Route;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private final String host;
    private final int port;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() {
        try (Socket socket = new Socket(host, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            ConsoleInputReader reader = new ConsoleInputReader(scanner);
            System.out.println("Connected to server!");
            System.out.println("Type 'help' to see commands\n");

            while (true) {
                System.out.print("> ");
                String cmd = scanner.nextLine().trim();

                if (cmd.equalsIgnoreCase("exit")) {
                    System.out.println("Disconnecting...");
                    break;
                }

                out.println(cmd);

                if (cmd.equals("add") || cmd.startsWith("update") || cmd.equals("add_if_min")) {
                    String ok = in.readLine();
                    if (ok == null || !"OK".equals(ok)) {
                        System.out.println("Server error: " + ok);
                        continue;
                    }
                    Route route = reader.readRoute();
                    out.println(route);
                }

                String line;
                while ((line = in.readLine()) != null) {
                    if (line.isEmpty()) break;
                    System.out.println(line);
                }
            }

        } catch (Exception e) {
            System.out.println("Connection closed: " + e.getMessage());
        }
    }
}