package net.scriptgate.common;

public class Rectangle {
    public int x;
    public int y;
    public int width;
    public int height;

    public Rectangle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Rectangle(int width, int height) {
        this(0, 0, width, height);
    }

    public boolean contains(int x, int y) {
        return x >= this.x && x < this.x + this.width &&
                y >= this.y && y < this.y + this.height;
    }
}
