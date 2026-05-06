package org.example.common.command;

import java.io.Serializable;

public class CommandResponse implements Serializable {
    private final String message;

    // 只接收一个 String 参数的构造器，和你调用时的写法完全匹配
    public CommandResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}