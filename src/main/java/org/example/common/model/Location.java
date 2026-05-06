package org.example.common.model;

public class Location {
    private Long x;
    private Long y;
    private String name;

    public Location(Long x, Long y, String name) {
        this.x = x;
        this.y = y;
        this.name = name;
    }

    public Long getX() { return x; }
    public Long getY() { return y; }
    public String getName() { return name; }

    @Override
    public String toString() {
        return x + "," + y + "," + name;
    }
}
