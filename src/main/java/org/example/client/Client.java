package org.example.client;

import org.example.client.console.ConsoleInputReader;
import org.example.client.network.NonBlockingClientChannel;
import org.example.common.command.CommandRequest;
import org.example.common.command.CommandResponse;
import org.example.common.model.Product;

public class Client {
    private final NonBlockingClientChannel channel;
    private final ConsoleInputReader console = new ConsoleInputReader();

    public Client(String host, int port) {
        this.channel = new NonBlockingClientChannel(host, port);
    }

    public void startClient() {
        System.out.println("Client started. Commands: add, show, clear, exit");

        while (true) {
            String cmd = console.readInput("Enter command");
            if ("exit".equalsIgnoreCase(cmd)) {
                System.out.println("Exit");
                break;
            }

            CommandRequest request;
            if ("add".equalsIgnoreCase(cmd)) {
                String name = console.readInput("Enter name");
                request = new CommandRequest("add", new Object[]{name});
            } else {
                request = new CommandRequest(cmd, new Object[0]);
            }

            CommandResponse res = channel.sendCommand(request);
            if (res != null) {
                System.out.println("Result: " + res.getMessage());
                // 这里的调用和上面的 getList() 对应
                if (res.getList() != null) {
                    for (Product p : res.getList()) {
                        System.out.println(p);
                    }
                }
            }
            System.out.println("----------------------------------------");
        }
    }
}