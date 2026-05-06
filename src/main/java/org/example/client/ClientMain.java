package org.example.client;
public class ClientMain {
    public static void main(String[] args) {
        new Client("localhost", 12345).startClient();
    }
}