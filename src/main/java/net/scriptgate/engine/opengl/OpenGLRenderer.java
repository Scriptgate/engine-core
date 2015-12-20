package net.scriptgate.engine.opengl;

import net.scriptgate.common.Color4f;
import net.scriptgate.common.Point;
import net.scriptgate.engine.Renderer;
import net.scriptgate.engine.image.ImageLoader;
import net.scriptgate.engine.image.Texture;
import net.scriptgate.engine.image.TextureLoader;

import java.awt.*;
import java.awt.image.BufferedImage;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static org.lwjgl.opengl.GL11.*;

public class OpenGLRenderer implements Renderer {

    private final Color4f color;
    private final float DEG2RAD = 3.14159f / 180;
    private final ImageLoader<Texture> imageLoader;

    public OpenGLRenderer() {
        imageLoader = new TextureLoader();
        color = new Color4f(1, 1, 1, 1);
    }

    @Override
    public void disableFont() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void drawImage(int x, int y, String path) {

        glEnable(GL_TEXTURE_2D);

        Texture texture = imageLoader.getTexture(path);
        int width = texture.getImageWidth();
        int height = texture.getImageHeight();

        glPushMatrix();
        texture.bind();

//        middle center
//        glTranslatef(x - width / 2, y - height / 2, 0);
//        top middle
        glTranslatef(x - width / 2, y, 0);
//        top left
//        glTranslatef(x, y, 0);

        glBegin(GL_QUADS);
        {
            glTexCoord2f(0, 0);
            glVertex2f(0, 0);

            glTexCoord2f(0, texture.getHeightToBinaryHeightRatio());
            glVertex2f(0, height);

            glTexCoord2f(texture.getWidthToBinaryWidthRatio(), texture.getHeightToBinaryHeightRatio());
            glVertex2f(width, height);

            glTexCoord2f(texture.getWidthToBinaryWidthRatio(), 0);
            glVertex2f(width, 0);
        }
        glEnd();

        glPopMatrix();
        glDisable(GL_TEXTURE_2D);
    }

    @Override
    public void drawImage(int x, int y, Image img) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void drawImage(String imagePath, Point position, Point offset, Point size) {

        glEnable(GL_TEXTURE_2D);

        Texture texture = imageLoader.getTexture(imagePath);

        glPushMatrix();
        texture.bind();

        int width = texture.getImageWidth();
        int height = texture.getImageHeight();

//        middle center
//        glTranslatef(position.x - width / 2, position.y - height / 2, 0);
//        top left
//        glTranslatef(position.x, position.y, 0);
        glTranslatef(position.x - width / 2, position.y, 0);


        glBegin(GL_QUADS);
        {
            float ox = (float) (offset.x) / texture.getBinaryWidth();
            float oy = (float) (offset.y) / texture.getBinaryHeight();
            float sx = (float) (size.x) / texture.getBinaryWidth();
            float sy = (float) (size.y) / texture.getBinaryHeight();

            glTexCoord2f(ox, oy);
            glVertex2f(0, 0);

            glTexCoord2f(ox, oy + sy);
            glVertex2f(0, size.y);

            glTexCoord2f(ox + sx, oy + sy);
            glVertex2f(size.x, size.y);

            glTexCoord2f(ox + sx, oy);
            glVertex2f(size.x, 0);
        }
        glEnd();

        glPopMatrix();
        glDisable(GL_TEXTURE_2D);
    }

    @Override
    public void drawLine(Point from, net.scriptgate.common.Point to) {
        drawLine(from.x, from.y, to.x, to.y);
    }

    @Override
    public void drawRect(int x, int y, int width, int height) {
        glPushMatrix();
        glTranslatef(x, y, 0);
        glBegin(GL_LINE_STRIP);
        {

            glVertex2f(0, 0);
            glVertex2f(width, 0);

            glVertex2f(width, 0);
            glVertex2f(width, height);

            glVertex2f(0, height);
            glVertex2f(width, height);

            glVertex2f(0, height);
            glVertex2f(0, 0);
        }
        glEnd();
        glPopMatrix();

    }

    @Override
    public void drawString(int x, int y, String text) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void enableFont() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void fillCircle(int x, int y, int radius) {
        glPushMatrix();
        glVertex2f(x, y);
        glBegin(GL_TRIANGLE_FAN);
        {
            for (int i = 0; i < 360; i++) {
                float degInRad = i * DEG2RAD;
                glVertex2f((float) (cos(degInRad) * radius + x), (float) (sin(degInRad) * radius + y));
            }
        }
        glEnd();
        glPopMatrix();
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {
        glPushMatrix();
        glBegin(GL_LINE_STRIP);
        {
            glVertex2f(x1, y1);
            glVertex2f(x2, y2);
        }
        glEnd();
        glPopMatrix();
    }

    @Override
    public void fillRect(int x, int y, int width, int height) {
        glPushMatrix();
        glBegin(GL_QUADS);
        {
            glVertex2f(x, y);
            glVertex2f(x + width, y);
            glVertex2f(x + width, y + height);
            glVertex2f(x, y + height);
        }
        glEnd();
        glPopMatrix();
    }

    @Override
    public BufferedImage printScreen() {
        return OpenGLScreenshotHelper.getScreenshot();
    }

    @Override
    public void setColor(float r, float g, float b) {
        color.r = r;
        color.g = g;
        color.b = b;
        glColor4f(color.r, color.g, color.b, color.a);
    }

    @Override
    public void setOpacity(float a) {
        color.a = a;
        glColor4f(color.r, color.g, color.b, color.a);
    }

    @Override
    public void translate(int x, int y) {
        glTranslatef(x, y, 0);
    }
}
