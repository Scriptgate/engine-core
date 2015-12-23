package net.scriptgate.common;

public class Color3f {

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
