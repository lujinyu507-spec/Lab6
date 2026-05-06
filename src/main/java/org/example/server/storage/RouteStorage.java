package org.example.server.storage;

import org.example.common.model.Route;

import java.io.*;
import java.time.ZonedDateTime;
import java.util.*;

public class RouteStorage {
    private TreeSet<Route> routes;
    private final ZonedDateTime initializationDate;
    private final String fileName;

    public RouteStorage() {
        this.fileName = System.getenv("LAB5_FILE");
        this.initializationDate = ZonedDateTime.now();
        this.routes = new TreeSet<>();
        loadFromFile();
    }

    public void loadFromFile() {
        if (fileName == null || fileName.isBlank()) return;
        File file = new File(fileName);
        if (!file.exists()) return;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            routes = (TreeSet<Route>) ois.readObject();
        } catch (Exception ignored) {}
    }

    public void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(routes);
        } catch (IOException ignored) {}
    }

    public void add(Route route) {
        routes.add(route);
    }

    public boolean removeById(long id) {
        Optional<Route> r = routes.stream().filter(route -> route.getId() == id).findFirst();
        if (r.isPresent()) {
            routes.remove(r.get());
            return true;
        }
        return false;
    }

    public void clear() {
        routes.clear();
    }

    public TreeSet<Route> getRoutes() {
        return routes;
    }

    public long generateNewId() {
        return routes.isEmpty() ? 1 : routes.last().getId() + 1;
    }

    public ZonedDateTime getInitializationDate() {
        return initializationDate;
    }

    public int getSize() {
        return routes.size();
    }
}