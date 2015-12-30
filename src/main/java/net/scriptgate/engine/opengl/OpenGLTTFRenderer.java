package net.scriptgate.engine.opengl;

import net.scriptgate.common.Rectangle;
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
    public static int FONT_HEIGHT = 13;
    public static String FONT_FILE = "fonts/RedAlert.ttf";
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
//          TODO: 13 - 0.2f -> 12.8f gives RedAlert.tff a sharper look, artifacts still remain, needs some tuning
            stbtt_BakeFontBitmap(ttf, FONT_HEIGHT - 0.2f, bitmap, BITMAP_W, BITMAP_H, 32, cdata);
//          can free ttf at this point
            fontTextureId = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, fontTextureId);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_ALPHA,
                    BITMAP_W, BITMAP_H, 0,
                    GL_ALPHA, GL_UNSIGNED_BYTE, bitmap);
//          can free bitmap at this point

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        glDisable(GL_TEXTURE_2D);
    }

    public Rectangle render(OpenGLRenderer renderer, int x, int y, String text) {
        glEnable(GL_TEXTURE_2D);

        int maxAboveBaseline = 0;
        int maxBelowBaseline = 0;

        glBindTexture(GL_TEXTURE_2D, fontTextureId);

        glPushMatrix();
        //font is rendered from bottom to top, starting above coordinate-Y, offset by 1 pixel
        glTranslatef(x, y + 1, 0f);
        glBegin(GL_QUADS);
        {
            FloatBuffer bX = getXBuffer();
            FloatBuffer bY = getYBuffer();
            for (int character : toASCII(text)) {
                stbtt_GetBakedQuad(cdata, BITMAP_W, BITMAP_H, character, bX, bY, quad, 1);
                //TODO: remove static call
                renderer.drawBoxedTexCoords(
                        quad.x0(), quad.y0(),
                        quad.x1(), quad.y1(),
                        quad.s0(), quad.t0(),
                        quad.s1(), quad.t1()
                );
                maxAboveBaseline = Math.max(maxAboveBaseline, -(int) quad.y0());
                maxBelowBaseline = Math.max(maxBelowBaseline, (int) quad.y1());
            }
        }
        glEnd();
        glPopMatrix();

        glDisable(GL_TEXTURE_2D);

        return new Rectangle(
                x, y + 1 - maxAboveBaseline,
                (int) quad.x1(), maxAboveBaseline + maxBelowBaseline);
    }

    public Rectangle getBounds(int x, int y, String text) {
        int maxAboveBaseline = 0;
        int maxBelowBaseline = 0;

        FloatBuffer bX = getXBuffer();
        FloatBuffer bY = getYBuffer();
        for (int character : toASCII(text)) {
            stbtt_GetBakedQuad(cdata, BITMAP_W, BITMAP_H, character, bX, bY, quad, 1);
            maxAboveBaseline = Math.min(maxAboveBaseline, (int) quad.y0());
            maxBelowBaseline = Math.max(maxBelowBaseline, (int) quad.y1());
        }

        return new Rectangle(
                x, y + maxAboveBaseline + 1,
                (int) quad.x1(), -maxAboveBaseline + maxBelowBaseline);
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
}
