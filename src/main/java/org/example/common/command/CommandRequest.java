package org.example.common.command;

import java.io.Serializable;

public class CommandRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String commandName;
    private Object[] arguments; 

    public CommandRequest(String commandName, Object[] arguments) {
        this.commandName = commandName;
        this.arguments = arguments;
    }

    public String getCommandName() {
        return commandName;
    }

    public Object[] getArguments() {
        return arguments;
    }
}
