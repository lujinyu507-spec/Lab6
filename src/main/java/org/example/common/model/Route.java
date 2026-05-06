package org.example.common.model;

import java.time.ZonedDateTime;

public class Route implements Comparable<Route> {
    private Long id;
    private String name;
    private Coordinates coordinates;
    private ZonedDateTime creationDate;
    private Location from;
    private Location to;
    private Float distance;

    public Route(Long id, String name, Coordinates coordinates, ZonedDateTime creationDate, Location from, Location to, Float distance) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.from = from;
        this.to = to;
        this.distance = distance;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public Coordinates getCoordinates() { return coordinates; }
    public ZonedDateTime getCreationDate() { return creationDate; }
    public Location getFrom() { return from; }
    public Location getTo() { return to; }
    public Float getDistance() { return distance; }

    // 固定格式，绝对不会和解析逻辑冲突
    @Override
    public String toString() {
        return "Route[id=" + id + "|name=" + name + "|coords=" + coordinates + "|date=" + creationDate + "|from=" + from + "|to=" + to + "|distance=" + distance + "]";
    }

    @Override
    public int compareTo(Route o) {
        return this.distance.compareTo(o.distance);
    }
}