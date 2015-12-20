package net.scriptgate.engine.fx;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import net.scriptgate.common.Point;
import net.scriptgate.engine.Renderer;

import java.awt.*;
import java.awt.image.BufferedImage;

public class FXRenderer implements Renderer {

    private GraphicsContext context;

    private Color color = new Color(0, 0, 0, 1);

    public void loadContext(GraphicsContext context) {
        this.context = context;
    }

    @Override
    public void enableFont() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void disableFont() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void translate(int x, int y) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setOpacity(float a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setColor(float r, float g, float b) {
        this.color = new Color(r, g, b, 1);
    }

    @Override
    public void drawString(int x, int y, String text) {
        context.strokeText(text, x, y);
    }

    @Override
    public void drawImage(int x, int y, String path) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void drawImage(int x, int y, Image img) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void drawImage(String imagePath, Point position, Point offset, Point size) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {


        context.setStroke(color);
        context.setLineWidth(1);
        context.strokeLine(x1, y1, x2, y2);
    }

    @Override
    public void drawLine(Point from, net.scriptgate.common.Point to) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void fillCircle(int x, int y, int radius) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void drawRect(int x, int y, int width, int height) {
        context.setStroke(color);
        context.setLineWidth(1);
        context.strokeRect(x, y, width, height);
    }

    @Override
    public void fillRect(int x, int y, int width, int height) {
        context.setFill(color);
        context.fillRect(x, y, width, height);
    }

    @Override
    public BufferedImage printScreen() {
        throw new UnsupportedOperationException();
    }
}
