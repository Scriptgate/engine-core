package net.scriptgate.engine.opengl;

import net.scriptgate.engine.util.IOUtil;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.stb.STBTTBakedChar;
import org.lwjgl.system.MemoryUtil;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBTruetype.stbtt_BakeFontBitmap;
import static org.lwjgl.stb.STBTruetype.stbtt_GetBakedQuad;


class OpenGLTTFRenderer {

    private static FloatBuffer xBuffer;
    private static FloatBuffer yBuffer;
    private STBTTAlignedQuad quad;

    //TODO: intialize font height and file through properties
    private static final int FONT_HEIGHT = 8;
    private static final String FONT_FILE = "fonts/Ricasso.ttf";
    private STBTTBakedChar.Buffer cdata;
    private int BITMAP_W;
    private int BITMAP_H;

    private int fontTextureId;

    public OpenGLTTFRenderer() {
    }

    private static int[] toASCII(String text) {
        return text.chars()
                .filter(c -> c >= 32 && c < 128)
                .map(c -> c - 32)
                .toArray();
    }

    public void initialize() {
        glEnable(GL_TEXTURE_2D);
        quad = STBTTAlignedQuad.malloc();

        BITMAP_W = 512;
        BITMAP_H = 512;


        cdata = STBTTBakedChar.mallocBuffer(96);

        try {
            ByteBuffer ttf = IOUtil.ioResourceToByteBuffer(FONT_FILE, 160 * 1024);
            ByteBuffer bitmap = BufferUtils.createByteBuffer(BITMAP_W * BITMAP_H);
            stbtt_BakeFontBitmap(ttf, FONT_HEIGHT, bitmap, BITMAP_W, BITMAP_H, 32, cdata);
//          can free ttf at this point
            fontTextureId = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, fontTextureId);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_ALPHA,
                    BITMAP_W, BITMAP_H, 0,
                    GL_ALPHA, GL_UNSIGNED_BYTE, bitmap);
//          can free bitmap at this point

            //The texture magnification function is used when the pixel being textured maps to an area less than or equal to one texture element
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            //The texture minifying function is used whenever the pixel being textured maps to an area greater than one texture element
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        glDisable(GL_TEXTURE_2D);
    }

    public void render(int x, int y, String text) {
        glEnable(GL_TEXTURE_2D);

        glBindTexture(GL_TEXTURE_2D, fontTextureId);

        glPushMatrix();

        glTranslatef(x, y + FONT_HEIGHT, 0f);
        glBegin(GL_QUADS);
        {
            FloatBuffer bX = getXBuffer();
            FloatBuffer bY = getYBuffer();
            for (int c : toASCII(text)) {

                stbtt_GetBakedQuad(cdata, BITMAP_W, BITMAP_H, c, bX, bY, quad, 1);
                //TODO: remove static call
                OpenGLRenderer.drawBoxedTexCoords(
                        quad.x0(), quad.y0(),
                        quad.x1(), quad.y1(),
                        quad.s0(), quad.t0(),
                        quad.s1(), quad.t1()
                );
            }
        }
        glEnd();

        glPopMatrix();

        glDisable(GL_TEXTURE_2D);
    }

    private static FloatBuffer getXBuffer() {
        if (xBuffer == null) {
            xBuffer = BufferUtils.createFloatBuffer(1);
        }
        xBuffer.put(0, 0.0f);
        return xBuffer;
    }

    private static FloatBuffer getYBuffer() {
        if (yBuffer == null) {
            yBuffer = BufferUtils.createFloatBuffer(1);
        }
        yBuffer.put(0, 0.0f);
        return yBuffer;
    }

    public void destroy() {
        quad.free();
        MemoryUtil.memFree(cdata);
    }

    public int getFontHeight() {
        return FONT_HEIGHT;
    }
}
