package org.example.server.handler;

import org.example.common.command.CommandRequest;
import org.example.common.command.CommandResponse;
import org.example.common.model.Product;
import org.example.server.storage.ProductStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.net.Socket;

public class ClientHandler {
    private static final Logger logger = LoggerFactory.getLogger(ClientHandler.class);
    private final Socket socket;
    private final ProductStorage storage;

    public ClientHandler(Socket socket, ProductStorage storage) {
        this.socket = socket;
        this.storage = storage;
    }

    public void run() {
        try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {

            CommandRequest request = (CommandRequest) in.readObject();
            logger.info("Received command: {}", request.getCommandName());

            CommandResponse response = processCommand(request);
            out.writeObject(response);
            out.flush();

        } catch (Exception e) {
            logger.error("Client handling error", e);
        } finally {
            try {
                socket.close();
            } catch (Exception ignored) {}
        }
    }

    private CommandResponse processCommand(CommandRequest req) {
        switch (req.getCommandName().toLowerCase()) {
            case "add":
                String name = (String) req.getArguments()[0];
                storage.addProduct(new Product(0, name));
                return new CommandResponse(true, "Product added", null);
            case "show":
                return new CommandResponse(true, "List:", storage.getSortedProducts());
            case "clear":
                storage.clearCollection();
                return new CommandResponse(true, "Cleared", null);
            default:
                return new CommandResponse(false, "Unknown command", null);
        }
    }
}