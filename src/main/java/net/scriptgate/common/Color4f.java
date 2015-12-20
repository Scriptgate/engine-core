package net.scriptgate.common;


public class Color4f {

    public float a, r, g, b;

    public Color4f(float a, float r, float g, float b) {
        this.a = a;
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public int intValue() {
        int aInt = (int) (a * 255) & 0xFF;
        int rInt = (int) (r * 255) & 0xFF;
        int gInt = (int) (g * 255) & 0xFF;
        int bInt = (int) (b * 255) & 0xFF;
        return aInt << 24 | rInt << 16 | gInt << 8 | bInt;
    }

    public static Color4f fromInt(float a, int r, int g, int b) {
        return new Color4f(a, r / 255f, g / 255f, b / 255f);
    }

    public static Color4f fromInt(int r, int g, int b) {
        return fromInt(1, r, g, b);
    }
}
