package org.example.common.command;

import java.io.Serializable;
import java.util.List;
import org.example.common.model.Product;

public class CommandResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    private boolean success;
    private String message;
    private List<Product> list;

    public CommandResponse(boolean success, String message, List<Product> list) {
        this.success = success;
        this.message = message;
        this.list = list;
    }

    public String getMessage() {
        return message;
    }

    // 关键：这个方法名必须是 getList()
    public List<Product> getList() {
        return list;
    }
}