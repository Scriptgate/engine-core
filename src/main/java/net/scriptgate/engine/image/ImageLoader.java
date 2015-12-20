package net.scriptgate.engine.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment;
import static java.awt.Transparency.TRANSLUCENT;
import static java.lang.Integer.toHexString;

public abstract class ImageLoader<T> {

    private static final String resources = "";
    private static final String extension = ".png";
    protected final Map<String, T> repository;

    public ImageLoader() {
        this.repository = new HashMap<>();
    }

    public static BufferedImage loadImage(String name) {
        BufferedImage imageFromResource = readImageFromResource(name);
        return createCompatibleImage(imageFromResource);
    }

    private static BufferedImage readImageFromResource(String name) {
        try (InputStream imageStream = ImageLoader.class.getClassLoader().getResourceAsStream(getPath(name))) {
            return ImageIO.read(imageStream);
        } catch (IOException | IllegalArgumentException ex) {
            throw new RuntimeException("Exception reading image with filepath: " + getPath(name));
        }
    }

    private static BufferedImage createCompatibleImage(BufferedImage image) {
        GraphicsConfiguration graphicsConfiguration = getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration();

        BufferedImage compatibleImage = graphicsConfiguration.createCompatibleImage(image.getWidth(), image.getHeight(), TRANSLUCENT);
        Graphics g = compatibleImage.getGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();

        return compatibleImage;
    }

    private static String getPath(String name) {
        return resources + name + extension;
    }

    public T getTexture(String resourceName) {
        T tex = repository.get(resourceName);
        if (tex != null) {
            return tex;
        }
        tex = loadTexture(resourceName);
        repository.put(resourceName, tex);
        return tex;
    }

    protected abstract T loadTexture(String path);

    public String[] pixelsToHexArray(int[] pixels) {
        String[] hexValues = new String[pixels.length];

        for (int i = 0; i < pixels.length; i++) {
            hexValues[i] = toHexString(pixels[i]).substring(2).toUpperCase();
        }

        return hexValues;
    }

    public String[] imageToHexArray(BufferedImage image) {
        int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        return pixelsToHexArray(pixels);
    }
}
