package net.scriptgate.engine.opengl;

import net.scriptgate.common.Color4f;
import net.scriptgate.common.Point;
import net.scriptgate.common.Rectangle;
import net.scriptgate.engine.Renderer;
import net.scriptgate.engine.image.ImageLoader;
import net.scriptgate.engine.image.Texture;
import net.scriptgate.engine.image.TextureLoader;

import java.awt.Image;
import java.awt.image.BufferedImage;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static org.lwjgl.opengl.GL11.*;

public class OpenGLRenderer implements Renderer {

    private final Color4f color;
    private static final float DEG2RAD = 3.14159f / 180;
    private final ImageLoader<Texture> imageLoader;
    private final OpenGLTTFRenderer fontRenderer;

    public OpenGLRenderer() {
        imageLoader = new TextureLoader();
        color = new Color4f(1, 1, 1, 1);
        fontRenderer = new OpenGLTTFRenderer();
    }

    @Override
    public void drawImage(int x, int y, String path) {
        glEnable(GL_TEXTURE_2D);

        Texture texture = imageLoader.getTexture(path);
        int width = texture.getWidth();
        int height = texture.getHeight();
        texture.bind();

        glPushMatrix();
//      middle center
//      glTranslatef(x - width / 2, y - height / 2, 0);
//      top middle
        glTranslatef(x - width / 2, y, 0);
//      top left
//      glTranslatef(x, y, 0);

        glBegin(GL_QUADS);
        {
            drawBoxedTexCoords(
                    0, 0, width, height,
                    0, 0, texture.s1(), texture.t1());
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
        texture.bind();

        glPushMatrix();

        int width = texture.getWidth();

//      int height = texture.getHeight();
//      middle center
//      glTranslatef(position.x - width / 2, position.y - height / 2, 0);
//      top left
//      glTranslatef(position.x, position.y, 0);
        glTranslatef(position.x - width / 2, position.y, 0);

        glBegin(GL_QUADS);
        {
            float s0 = texture.getPercentageOfWidth(offset.x);
            float t0 = texture.getPercentageOfHeight(offset.y);
            float s1 = texture.getPercentageOfWidth(offset.x + size.x);
            float t1 = texture.getPercentageOfHeight(offset.y + size.y);

            drawBoxedTexCoords(
                    0, 0, size.x, size.y,
                    s0, t0, s1, t1);
        }
        glEnd();

        glPopMatrix();

        glDisable(GL_TEXTURE_2D);
    }

    /**
     * @param x0 the x coordinate of the first corner of the destination rectangle.
     * @param y0 the y coordinate of the first corner of the destination rectangle.
     * @param x1 the x coordinate of the second corner of the destination rectangle.
     * @param y1 the y coordinate of the second corner of the destination rectangle.
     * @param s0 the x component of the first corner of the source rectangle.
     *           This is a percentage of the width of the texture.
     * @param t0 the y component of the first corner of the source rectangle.
     *           This is a percentage of the height of the texture.
     * @param s1 the x component of the second corner of the source rectangle.
     *           This is a percentage of the width of the texture.
     * @param t1 the y component of the second corner of the source rectangle.
     *           This is a percentage of the height of the texture.
     */
    //@formatter:off
    public void drawBoxedTexCoords(float x0, float y0, float x1, float y1, float s0, float t0, float s1, float t1) {
        glTexCoord2f(s0, t0);   glVertex2f(x0, y0);
        glTexCoord2f(s1, t0);   glVertex2f(x1, y0);
        glTexCoord2f(s1, t1);   glVertex2f(x1, y1);
        glTexCoord2f(s0, t1);   glVertex2f(x0, y1);
    }
    //@formatter:on

    @Override
    public void drawRect(int x, int y, int width, int height) {
        glDisable(GL_TEXTURE_2D);

        int adjustedWidthToBorder = width - 1;
        int adjustedHeightToBorder = height - 1;

        glPushMatrix();
        glTranslatef(x, y, 0);
        glBegin(GL_LINE_LOOP);
        {
            glVertex2f(0, 0);
            glVertex2f(adjustedWidthToBorder, 0);
            glVertex2f(adjustedWidthToBorder, adjustedHeightToBorder);
            glVertex2f(0, adjustedHeightToBorder);
        }
        glEnd();
        glPopMatrix();

        glEnable(GL_TEXTURE_2D);
    }

    @Override
    public Rectangle drawText(int x, int y, String text) {
        return fontRenderer.render(this, x, y, text);
    }

    @Override
    public void fillCircle(int x, int y, int radius) {
        glDisable(GL_TEXTURE_2D);

        glPushMatrix();

        glVertex2f(x, y);
        glBegin(GL_TRIANGLE_FAN);
        {
            for (int i = 0; i < 360; i++) {
                float degInRad = i * DEG2RAD;
                glVertex2d(
                        x + radius * cos(degInRad),
                        y + radius * sin(degInRad));
            }
        }
        glEnd();

        glPopMatrix();

        glEnable(GL_TEXTURE_2D);
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {
        glDisable(GL_TEXTURE_2D);

        glPushMatrix();

        glBegin(GL_LINES);
        {
            glVertex2f(x1, y1);
            glVertex2f(x2, y2);
        }
        glEnd();

        glPopMatrix();

        glEnable(GL_TEXTURE_2D);
    }

    @Override
    public void drawCircle(int x, int y, int radius) {
        glDisable(GL_TEXTURE_2D);

        glPushMatrix();

        glVertex2f(x, y);
        glBegin(GL_LINE_LOOP);
        {
            for (int i = 0; i < 360; i++) {
                float degInRad = i * DEG2RAD;
                glVertex2d(
                        x + radius * cos(degInRad),
                        y + radius * sin(degInRad));
            }
        }
        glEnd();

        glPopMatrix();

        glEnable(GL_TEXTURE_2D);
    }

    @Override
    public void fillRect(int x, int y, int width, int height) {
        glDisable(GL_TEXTURE_2D);

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

        glEnable(GL_TEXTURE_2D);
    }

    @Override
    public BufferedImage printScreen() {
        return OpenGLScreenshotHelper.getScreenshot();
    }

    @Override
    public Rectangle getBounds(int x, int y, String text) {
        return fontRenderer.getBounds(x, y, text);
    }

    @Override
    public void setColor(float r, float g, float b) {
        color.r = r;
        color.g = g;
        color.b = b;
        glColor4f(color.r, color.g, color.b, color.a);
    }

    @Override
    public void setColor(float a, float r, float g, float b) {
        color.a = a;
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

    public void destroy() {
        fontRenderer.destroy();
    }

    public void initialize() {
        fontRenderer.initialize();
    }
}
