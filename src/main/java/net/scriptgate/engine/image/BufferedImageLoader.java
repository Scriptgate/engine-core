package net.scriptgate.engine.image;

import java.awt.image.BufferedImage;

public class BufferedImageLoader extends ImageLoader<BufferedImage> {

    public BufferedImageLoader() {
        super();
    }

    @Override
    protected BufferedImage loadTexture(String path) {
        return loadImage(path);
    }
}
