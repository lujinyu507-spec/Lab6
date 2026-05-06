package org.example.server;

import org.example.server.handler.ClientHandler;
import org.example.server.storage.RouteStorage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private final int port;
    private final RouteStorage storage;
    private boolean isRunning = true;

    // 构造方法：端口+数据存储，和ServerMain里的调用完全匹配
    public Server(int port, RouteStorage storage) {
        this.port = port;
        this.storage = storage;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.printf("[INFO] %s - Server started on port: %d%n", this.getClass().getName(), port);
            System.out.printf("[INFO] %s - Waiting for clients...%n", this.getClass().getName());

            // 服务器关闭时自动保存数据
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                isRunning = false;
                System.out.printf("[INFO] %s - Server stopping... Saving collection%n", this.getClass().getName());
                storage.saveToFile();
            }));

            // 循环接收客户端连接
            while (isRunning) {
                Socket clientSocket = serverSocket.accept();
                System.out.printf("[INFO] %s - New client connected: %s%n", this.getClass().getName(), clientSocket.getInetAddress());
                // 每个客户端单独开线程处理
                new ClientHandler(clientSocket, storage).start();
            }

        } catch (IOException e) {
            System.out.printf("[ERROR] %s - Server start failed: %s%n", this.getClass().getName(), e.getMessage());
        }
    }
}