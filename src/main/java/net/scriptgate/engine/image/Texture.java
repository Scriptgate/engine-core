package net.scriptgate.engine.image;

import org.lwjgl.opengl.GL11;

public class Texture {

    private final int target;
    private final int textureID;
    private int binaryWidth;
    private int binaryHeight;
    private int height;
    private int width;
    private float widthToBinaryWidthRatio;
    private float heightToBinaryHeightRatio;

    public Texture(int target, int textureID) {
        this.target = target;
        this.textureID = textureID;
    }

    public void bind() {
        GL11.glBindTexture(target, textureID);
    }

    public int getImageHeight() {
        return height;
    }

    public void setImageHeight(int height) {
        this.height = height;
        initializeHeightRatio();
    }

    public int getImageWidth() {
        return width;
    }

    public void setImageWidth(int width) {
        this.width = width;
        initializeWidthRatio();
    }

    public float getHeightToBinaryHeightRatio() {
        return heightToBinaryHeightRatio;
    }

    public float getWidthToBinaryWidthRatio() {
        return widthToBinaryWidthRatio;
    }

    private void initializeHeightRatio() {
        this.binaryHeight = getBase2(height);
        heightToBinaryHeightRatio = ((float) height) / binaryHeight;
    }

    private void initializeWidthRatio() {
        this.binaryWidth = getBase2(width);
        widthToBinaryWidthRatio = ((float) width) / binaryWidth;
    }

    public int getBinaryWidth() {
        return binaryWidth;
    }

    public int getBinaryHeight() {
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
