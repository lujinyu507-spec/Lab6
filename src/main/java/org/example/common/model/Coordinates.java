package org.example.common.model;

public class Coordinates {
    private Long x;
    private Long y;

    public Coordinates(Long x, Long y) {
        this.x = x;
        this.y = y;
    }

    public Long getX() { return x; }
    public Long getY() { return y; }

    // ? 固定格式，用|分隔，避免冲突
    @Override
    public String toString() {
        return x + "," + y;
    }
}