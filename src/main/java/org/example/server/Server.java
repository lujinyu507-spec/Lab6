package org.example.server;

import org.example.server.handler.ClientHandler;
import org.example.server.storage.ProductStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final Logger logger = LoggerFactory.getLogger(Server.class);
    private final int port;
    private final ProductStorage storage;

    public Server(int port) {
        this.port = port;
        this.storage = new ProductStorage();
        this.storage.loadFromFile();
    }

    public void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("Server started on port: {}", port);
            logger.info("Waiting for clients...");

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                logger.info("Server stopping... Saving collection");
                storage.saveToFile();
            }));

            while (true) {
                Socket clientSocket = serverSocket.accept();
                logger.info("New client connected: {}", clientSocket.getInetAddress());
                new ClientHandler(clientSocket, storage).run();
            }
        } catch (Exception e) {
            logger.error("Server error", e);
        }
    }
}