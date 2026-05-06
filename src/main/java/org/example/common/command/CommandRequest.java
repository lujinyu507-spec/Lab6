package org.example.common.command;

import org.example.common.model.Route;
import java.io.Serializable;

public class CommandRequest implements Serializable {
    private final String command;
    private final String arg;
    private final Route route;

    public CommandRequest(String command, String arg, Route route) {
        this.command = command;
        this.arg = arg;
        this.route = route;
    }

    public String getCommand() {
        return command;
    }

    public String getArg() {
        return arg;
    }

    public Route getRoute() {
        return route;
    }
}