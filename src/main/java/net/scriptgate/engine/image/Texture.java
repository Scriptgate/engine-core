package net.scriptgate.engine.image;

import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;

public class Texture {

    private final int textureID;
    private int binaryWidth;
    private int binaryHeight;
    private int height;
    private int width;
    private float percentageOfTextureCoveredByImageX;
    private float percentageOfTextureCoveredByImageY;

    public Texture(int textureID) {
        this.textureID = textureID;
    }

    public void bind() {
        GL11.glBindTexture(GL_TEXTURE_2D, textureID);
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
        this.binaryHeight = getBase2(this.height);
        percentageOfTextureCoveredByImageY = ((float) this.height) / binaryHeight;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
        this.binaryWidth = getBase2(this.width);
        percentageOfTextureCoveredByImageX = ((float) this.width) / binaryWidth;
    }

    public float getPercentageOfWidth(int x) {
        return (float) (x) / getBinaryWidth();
    }

    public float getPercentageOfHeight(int y) {
        return (float) (y) / getBinaryHeight();
    }

    public float s1() {
        return percentageOfTextureCoveredByImageX;
    }

    public float t1() {
        return percentageOfTextureCoveredByImageY;
    }

    int getBinaryWidth() {
        return binaryWidth;
    }

    int getBinaryHeight() {
        return binaryHeight;
    }

    private int getBase2(int number) {
        int base2 = 2;
        while (base2 < number) {
            base2 *= 2;
        }
        return base2;
    }
}
