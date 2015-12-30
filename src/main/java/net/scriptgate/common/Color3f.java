package net.scriptgate.common;

public class Color3f {

    public static final Color3f BLACK = new Color3f(0, 0, 0);
    public static final Color3f WHITE = new Color3f(1, 1, 1);
    public static final Color3f RED = new Color3f(1, 0, 0);
    public static final Color3f GREEN = new Color3f(0, 1, 0);
    public static final Color3f BLUE = new Color3f(0, 0, 1);
    public static final Color3f YELLOW = new Color3f(1, 1, 0);

    public float r, g, b;

    public Color3f(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public static Color3f fromInt(int r, int g, int b) {
        return new Color3f(r / 255f, g / 255f, b / 255f);
    }

}
