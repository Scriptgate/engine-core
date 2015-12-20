package net.scriptgate.engine.awt;

import net.scriptgate.common.Color4f;
import net.scriptgate.common.Point;
import net.scriptgate.engine.Renderer;
import net.scriptgate.engine.image.BufferedImageLoader;
import net.scriptgate.engine.image.ImageLoader;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static java.awt.AlphaComposite.SRC_OVER;
import static java.awt.AlphaComposite.getInstance;
import static java.awt.Font.TRUETYPE_FONT;
import static java.awt.Font.createFont;
import static java.awt.RenderingHints.*;
import static java.util.Objects.requireNonNull;

public class AWTRenderer implements Renderer {

    public static final float FONT_SIZE = 30f;
    private final Color4f color;
    private final ImageLoader<BufferedImage> imageLoader;
    private Graphics2D graphics;
    private Font font;
    private Font defaultFont;

    public AWTRenderer() {
        imageLoader = new BufferedImageLoader();
        color = new Color4f(1, 1, 1, 1);
    }

    public void loadGraphics(Graphics2D graphics) {
        this.graphics = graphics;
        this.graphics.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_OFF);

        defaultFont = this.graphics.getFont();

        this.graphics.setFont(font);
    }

    private void loadFont(String fontPath) {
        try (InputStream fontStream = new FileInputStream(fontPath)) {
            Font f = createFont(TRUETYPE_FONT, fontStream);
            font = f.deriveFont(FONT_SIZE);
        } catch (FontFormatException | IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void drawImage(int x, int y, String path) {
        BufferedImage image = imageLoader.getTexture(path);
        graphics.drawImage(image, x, y, null);
    }

    @Override
    public void drawString(int x, int y, String text) {
        graphics.drawString(text, x, y);
    }

    @Override
    public void fillCircle(int x, int y, int radius) {
        graphics.fill(new Ellipse2D.Double(x - radius, y - radius, radius * 2, radius * 2));
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {
        graphics.drawLine(x1, y1, x2, y2);
    }

    @Override
    public void setColor(float r, float g, float b) {
        color.r = r;
        color.g = g;
        color.b = b;
        graphics.setColor(new java.awt.Color(
                color.r,
                color.g,
                color.b,
                color.a
        ));
    }

    @Override
    public void setOpacity(float a) {
        color.a = a;
        graphics.setComposite(getInstance(SRC_OVER, color.a));
    }

    @Override
    public void translate(int x, int y) {
        graphics.translate(x, y);
    }

    @Override
    public void enableFont() {
        requireNonNull(font);
        this.graphics.setRenderingHint(KEY_TEXT_ANTIALIASING, VALUE_TEXT_ANTIALIAS_ON);
        this.graphics.setFont(font);
    }

    @Override
    public void disableFont() {
        requireNonNull(font);
        this.graphics.setRenderingHint(KEY_TEXT_ANTIALIASING, VALUE_TEXT_ANTIALIAS_OFF);
        this.graphics.setFont(defaultFont);
    }

    @Override
    public void drawImage(int x, int y, Image img) {
        this.graphics.drawImage(img, x, y, null);
    }

    @Override
    public void drawLine(Point from, net.scriptgate.common.Point to) {
        drawLine(from.x, from.y, to.x, to.y);
    }

    @Override
    public void drawRect(int x, int y, int width, int height) {
        this.graphics.drawRect(x, y, width, height);
    }

    @Override
    public void drawImage(String imagePath, Point position, Point offset, Point size) {
        BufferedImage image = imageLoader.getTexture(imagePath);
        this.graphics.drawImage(
                image,
                position.x, position.y,
                position.x + size.x, position.y + size.y,
                offset.x, offset.y,
                offset.x + size.x, offset.y + size.y,
                null);
    }

    @Override
    public void fillRect(int x, int y, int width, int height) {
        this.graphics.fillRect(x, y, width, height);
    }

    @Override
    public BufferedImage printScreen() {
        throw new UnsupportedOperationException();
    }
}
