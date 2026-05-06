package org.example.common.command;

import java.io.Serializable;

public class CommandRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String commandName;
    private Object[] arguments; // 这里的变量名是 arguments

    public CommandRequest(String commandName, Object[] arguments) {
        this.commandName = commandName;
        this.arguments = arguments;
    }

    public String getCommandName() {
        return commandName;
    }

    // 关键：这里必须叫 getArguments()，而不是 getArgs()
    public Object[] getArguments() {
        return arguments;
    }
}