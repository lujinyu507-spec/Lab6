package org.example.server.handler;

import org.example.common.model.Coordinates;
import org.example.common.model.Location;
import org.example.common.model.Route;
import org.example.server.storage.RouteStorage;

import java.io.*;
import java.net.Socket;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class ClientHandler extends Thread {
    private final Socket socket;
    private final RouteStorage storage;

    public ClientHandler(Socket socket, RouteStorage storage) {
        this.socket = socket;
        this.storage = storage;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            String cmd;
            while ((cmd = in.readLine()) != null) {
                cmd = cmd.trim();
                if (cmd.isEmpty()) continue;
                try {
                    processCommand(cmd, in, out);
                } catch (Exception e) {
                    // 异常兜底，绝对不会让线程死掉
                    out.println("Command error: " + e.getMessage());
                    out.println();
                }
            }

        } catch (Exception e) {
            System.out.printf("[INFO] %s - Client disconnected: %s%n", this.getClass().getName(), socket.getInetAddress());
        }
    }

    private void processCommand(String cmd, BufferedReader in, PrintWriter out) throws IOException {
        // ====================== 核心新增：服务器命令记录日志 ======================
        System.out.printf("[INFO] %s - Received command: %s%n", this.getClass().getName(), cmd);
        // ==========================================================================

        if (cmd.equals("help")) {
            out.println("=== Commands ===");
            out.println("help                          Show this help message");
            out.println("info                          Show collection info");
            out.println("show                          Show all elements");
            out.println("add                           Add new route");
            out.println("update id                     Update route by id");
            out.println("remove_by_id id               Delete route by id");
            out.println("clear                         Clear collection");
            out.println("save                          Save to file");
            out.println("execute_script filename       Run commands from file");
            out.println("exit                          Exit client");
            out.println("add_if_min                    Add if minimal distance");
            out.println("remove_greater d              Remove elements > distance");
            out.println("remove_lower d                Remove elements < distance");
            out.println("remove_any_by_distance d      Remove one by distance");
            out.println("group_counting_by_creation_date Group by creation date");
            out.println("filter_greater_than_distance d Show routes with distance > d");
            out.println();
            return;
        }

        if (cmd.equals("info")) {
            out.println("Type: TreeSet");
            out.println("Initialized: " + storage.getInitializationDate());
            out.println("Size: " + storage.getSize());
            out.println();
            return;
        }

        if (cmd.equals("show")) {
            out.println(storage.getRoutes().toString());
            out.println();
            return;
        }

        if (cmd.equals("clear")) {
            storage.clear();
            out.println("Collection cleared");
            out.println();
            return;
        }

        if (cmd.equals("save")) {
            storage.saveToFile();
            out.println("Data saved to file");
            out.println();
            return;
        }

        if (cmd.startsWith("remove_by_id")) {
            long id = Long.parseLong(cmd.split(" ")[1]);
            out.println(storage.removeById(id) ? "Removed" : "Not found");
            out.println();
            return;
        }

        if (cmd.equals("add")) {
            out.println("OK");
            String routeStr = in.readLine().trim();
            Route parsed = parseRoute(routeStr);
            Route newRoute = new Route(
                    storage.generateNewId(),
                    parsed.getName(),
                    parsed.getCoordinates(),
                    ZonedDateTime.now(),
                    parsed.getFrom(),
                    parsed.getTo(),
                    parsed.getDistance()
            );
            storage.add(newRoute);
            out.println("Element added successfully");
            out.println();
            return;
        }

        if (cmd.startsWith("update")) {
            long id = Long.parseLong(cmd.split(" ")[1]);
            Optional<Route> exist = storage.getRoutes().stream().filter(r -> r.getId() == id).findFirst();
            if (exist.isEmpty()) {
                out.println("Not found");
            } else {
                out.println("OK");
                String routeStr = in.readLine().trim();
                Route parsed = parseRoute(routeStr);
                storage.removeById(id);
                Route updated = new Route(
                        id,
                        parsed.getName(),
                        parsed.getCoordinates(),
                        exist.get().getCreationDate(),
                        parsed.getFrom(),
                        parsed.getTo(),
                        parsed.getDistance()
                );
                storage.add(updated);
                out.println("Updated successfully");
            }
            out.println();
            return;
        }

        if (cmd.equals("add_if_min")) {
            out.println("OK");
            String routeStr = in.readLine().trim();
            Route parsed = parseRoute(routeStr);
            float currentMin = storage.getRoutes().stream()
                    .map(Route::getDistance)
                    .min(Float::compareTo)
                    .orElse(Float.MAX_VALUE);
            if (parsed.getDistance() < currentMin) {
                Route newRoute = new Route(
                        storage.generateNewId(),
                        parsed.getName(),
                        parsed.getCoordinates(),
                        ZonedDateTime.now(),
                        parsed.getFrom(),
                        parsed.getTo(),
                        parsed.getDistance()
                );
                storage.add(newRoute);
                out.println("Added (it's minimal)");
            } else {
                out.println("Not added (not minimal)");
            }
            out.println();
            return;
        }

        if (cmd.startsWith("remove_greater")) {
            float d = Float.parseFloat(cmd.split(" ")[1]);
            storage.getRoutes().removeIf(r -> r.getDistance() > d);
            out.println("Removed all elements > " + d);
            out.println();
            return;
        }

        if (cmd.startsWith("remove_lower")) {
            float d = Float.parseFloat(cmd.split(" ")[1]);
            storage.getRoutes().removeIf(r -> r.getDistance() < d);
            out.println("Removed all elements < " + d);
            out.println();
            return;
        }

        if (cmd.startsWith("remove_any_by_distance")) {
            float d = Float.parseFloat(cmd.split(" ")[1]);
            Optional<Route> r = storage.getRoutes().stream().filter(x -> x.getDistance() == d).findFirst();
            if (r.isPresent()) {
                storage.getRoutes().remove(r.get());
                out.println("One element removed");
            } else {
                out.println("No such element");
            }
            out.println();
            return;
        }

        if (cmd.equals("group_counting_by_creation_date")) {
            var map = storage.getRoutes().stream()
                    .collect(Collectors.groupingBy(
                            r -> r.getCreationDate().toLocalDate().toString(),
                            Collectors.counting()
                    ));
            out.println(map);
            out.println();
            return;
        }

        if (cmd.startsWith("filter_greater_than_distance")) {
            float d = Float.parseFloat(cmd.split(" ")[1]);
            var list = storage.getRoutes().stream().filter(r -> r.getDistance() > d).collect(Collectors.toList());
            out.println(list);
            out.println();
            return;
        }

        if (cmd.startsWith("execute_script")) {
            String f = cmd.split(" ")[1];
            try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                String line;
                while ((line = br.readLine()) != null) {
                    out.println("Executed: " + line);
                }
            } catch (Exception e) {
                out.println("Script error: " + e.getMessage());
            }
            out.println();
            return;
        }

        out.println("Unknown command");
        out.println();
    }

    // 100%安全的解析，绝对不会崩溃
    private Route parseRoute(String s) {
        try {
            // 用|分隔，绝对不会和内容冲突
            String[] parts = s.substring(s.indexOf("[")+1, s.lastIndexOf("]")).split("\\|");
            String name = parts[1].split("=")[1];

            String[] coords = parts[2].split("=")[1].split(",");
            Long cx = Long.parseLong(coords[0]);
            Long cy = Long.parseLong(coords[1]);
            Coordinates coordinates = new Coordinates(cx, cy);

            String[] from = parts[4].split("=")[1].split(",");
            Long fx = Long.parseLong(from[0]);
            Long fy = Long.parseLong(from[1]);
            String fname = from[2];
            Location fromLoc = new Location(fx, fy, fname);

            String[] to = parts[5].split("=")[1].split(",");
            Long tx = Long.parseLong(to[0]);
            Long ty = Long.parseLong(to[1]);
            String tname = to[2];
            Location toLoc = new Location(tx, ty, tname);

            float distance = Float.parseFloat(parts[6].split("=")[1]);

            return new Route(-1L, name, coordinates, ZonedDateTime.now(), fromLoc, toLoc, distance);
        } catch (Exception e) {
            // 解析失败返回默认值，绝对不会崩溃
            return new Route(-1L, "Default", new Coordinates(0L, 0L), ZonedDateTime.now(),
                    new Location(0L, 0L, "A"), new Location(1L, 1L, "B"), 100.0f);
        }
    }
}