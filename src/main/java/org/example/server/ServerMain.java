package org.example.server;

import org.example.server.storage.RouteStorage;

public class ServerMain {
    public static void main(String[] args) {
        int port = 52717;
        // 初始化数据存储核心类
        RouteStorage storage = new RouteStorage();
        // 构造器传入端口+存储实例，匹配Server类的定义
        new Server(port, storage).start();
    }
}