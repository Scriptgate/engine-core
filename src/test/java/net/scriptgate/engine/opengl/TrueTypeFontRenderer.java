package net.scriptgate.engine.opengl;

import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.stb.STBTTBakedChar;
import org.lwjgl.system.MemoryUtil;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.demo.util.IOUtil.ioResourceToByteBuffer;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBTruetype.stbtt_BakeFontBitmap;
import static org.lwjgl.stb.STBTruetype.stbtt_GetBakedQuad;


class TrueTypeFontRenderer {

    private final int fontHeight;
    private final String fontFile;
    private FloatBuffer xBuffer;
    private FloatBuffer yBuffer;
    private STBTTAlignedQuad quad;
    private STBTTBakedChar.Buffer cdata;
    private int BITMAP_W;
    private int BITMAP_H;

    public TrueTypeFontRenderer(int fontHeight, String fontFile) {
        this.fontHeight = fontHeight;
        this.fontFile = fontFile;
    }

    public void render(int x, int y, String text) {

        glEnable(GL_TEXTURE_2D);

        glColor3f(1, 1, 1); // Text color

        glPushMatrix();

        glTranslatef(x, y + fontHeight, 0f);

        xBuffer.put(0, 0.0f);
        yBuffer.put(0, 0.0f);
        glBegin(GL_QUADS);
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '\n') {
                yBuffer.put(0, yBuffer.get(0) + fontHeight);
                xBuffer.put(0, 0.0f);
                continue;
            } else if (c < 32 || 128 <= c)
                continue;

            stbtt_GetBakedQuad(cdata, BITMAP_W, BITMAP_H, c - 32, xBuffer, yBuffer, quad, 1);

            glTexCoord2f(quad.s0(), quad.t0());
            glVertex2f(quad.x0(), quad.y0());

            glTexCoord2f(quad.s1(), quad.t0());
            glVertex2f(quad.x1(), quad.y0());

            glTexCoord2f(quad.s1(), quad.t1());
            glVertex2f(quad.x1(), quad.y1());

            glTexCoord2f(quad.s0(), quad.t1());
            glVertex2f(quad.x0(), quad.y1());
        }
        glEnd();

        glPopMatrix();

        glDisable(GL_TEXTURE_2D);
    }

    public void initialize() {
        glEnable(GL_TEXTURE_2D);
        xBuffer = BufferUtils.createFloatBuffer(1);
        yBuffer = BufferUtils.createFloatBuffer(1);
        quad = STBTTAlignedQuad.malloc();

        BITMAP_W = 512;
        BITMAP_H = 512;

        int texID = glGenTextures();
        cdata = STBTTBakedChar.mallocBuffer(96);

        try {
            ByteBuffer ttf = ioResourceToByteBuffer(fontFile, 160 * 1024);

            ByteBuffer bitmap = BufferUtils.createByteBuffer(BITMAP_W * BITMAP_H);
            stbtt_BakeFontBitmap(ttf, fontHeight, bitmap, BITMAP_W, BITMAP_H, 32, cdata);

            glBindTexture(GL_TEXTURE_2D, texID);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_ALPHA, BITMAP_W, BITMAP_H, 0, GL_ALPHA, GL_UNSIGNED_BYTE, bitmap);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        glDisable(GL_TEXTURE_2D);
    }

    public void destroy() {
        quad.free();
        MemoryUtil.memFree(cdata);
    }
}
