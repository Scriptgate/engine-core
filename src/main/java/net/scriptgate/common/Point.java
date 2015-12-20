package net.scriptgate.common;

import static java.lang.Integer.compare;
import static java.lang.Math.sqrt;

public class Point {

    public int x;
    public int y;

    public Point() {
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point(Point point) {
        this.x = point.x;
        this.y = point.y;
    }

    public static Point min(Point a, Point b) {
        return new Point(
                Math.min(a.x, b.x),
                Math.min(a.y, b.y)
        );
    }

    public static Point max(Point a, Point b) {
        return new Point(
                Math.max(a.x, b.x),
                Math.max(a.y, b.y)
        );
    }

    public static Point sum(Point a, Point b) {
        return new Point(a.x + b.x, a.y + b.y);
    }

    public static Point difference(Point a, Point b) {
        return new Point(a.x - b.x, a.y - b.y);
    }

    public static Point normalize(Point point) {
        return new Point(
                compare(point.x, 0),
                compare(point.y, 0)
        );
    }

    public static int crossProduct(Point a, Point b) {
        return a.x * b.y - a.y * b.x;
    }

    public static double distance(Point a, Point b) {
        return sqrt((b.x - a.x) * (b.x - a.x) + (b.y - a.y) * (b.y - a.y));
    }

    public static double distance(int x1, int y1, int x2, int y2) {
        return sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }

    public Point add(Point point) {
        return this.add(point.x, point.y);
    }

    public Point add(int x, int y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public Point multiply(double d) {
        this.x *= d;
        this.y *= d;
        return this;
    }

    public Point divide(double d) {
        this.x = (int) Math.floor(this.x / d);
        this.y = (int) Math.floor(this.y / d);
        return this;
    }

    public Point multiply(Point point) {
        this.x *= point.x;
        this.y *= point.y;
        return this;
    }

    public void setZero() {
        this.x = 0;
        this.y = 0;
    }

    public boolean isZero() {
        return this.x == 0 && this.y == 0;
    }

    @Override
    public String toString() {
        return "[" + x + "," + y + "]";
    }

    @Override
    public boolean equals(Object other) {
        if(other instanceof Point) {
            Point that = (Point) other;
            return this.x == that.x && this.y == that.y;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return (41 * (41 + x) + y);
    }

    public Point roundToBase(int base) {
        return this.divide(base).multiply(base);
    }
}
