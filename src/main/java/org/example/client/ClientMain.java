package org.example.client;

public class ClientMain {
    public static void main(String[] args) {
        // 改成 Java 标准的构造器调用方式
        new Client("localhost", 52717).start();
    }
}