package net.scriptgate.engine;

import net.scriptgate.common.Color3f;
import net.scriptgate.common.Color4f;
import net.scriptgate.common.Point;

import java.awt.*;
import java.awt.image.BufferedImage;

public interface Renderer {
    void enableFont();

    void disableFont();

    void translate(int x, int y);

    default void setColor(Color3f color) {
        setColor(color.r, color.g, color.b);
    }

    default void setColor(Color4f color) {
        setColor(color.a, color.r, color.g, color.b);
    }

    void setOpacity(float a);

    void setColor(float r, float g, float b);

    void setColor(float a, float r, float g, float b);

    void drawString(int x, int y, String text);

    void drawImage(int x, int y, String path);

    void drawImage(int x, int y, Image img);

    void drawImage(String imagePath, Point position, Point offset, Point size);

    void drawLine(int x1, int y1, int x2, int y2);

    default void drawLine(Point from, Point to) {
        drawLine(from.x, from.y, to.x, to.y);
    }

    void fillCircle(int x, int y, int radius);

    void drawRect(int x, int y, int width, int height);

    void fillRect(int x, int y, int width, int height);

    BufferedImage printScreen();
}
