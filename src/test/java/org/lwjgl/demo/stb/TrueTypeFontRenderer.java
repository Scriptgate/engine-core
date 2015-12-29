package org.lwjgl.demo.stb;

import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.stb.STBTTBakedChar;
import org.lwjgl.system.MemoryUtil;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static net.scriptgate.engine.util.IOUtil.ioResourceToByteBuffer;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBTruetype.stbtt_BakeFontBitmap;
import static org.lwjgl.stb.STBTruetype.stbtt_GetBakedQuad;


class TrueTypeFontRenderer {

    private final int fontHeight;
    private final String fontFile;
    private FloatBuffer x;
    private FloatBuffer y;
    private STBTTAlignedQuad q;
    private STBTTBakedChar.Buffer cdata;
    private int BITMAP_W;
    private int BITMAP_H;

    public TrueTypeFontRenderer(int fontHeight, String fontFile) {
        this.fontHeight = fontHeight;
        this.fontFile = fontFile;
    }

    public void render(String text, float scale, int lineOffset) {
        glColor3f(1, 1, 1); // Text color

        float scaleFactor = 1.0f + scale * 0.25f;

        glPushMatrix();
        // Zoom
        glScalef(scaleFactor, scaleFactor, 1f);
        // Scroll
        glTranslatef(4.0f, fontHeight * 0.5f + 4.0f - lineOffset * fontHeight, 0f);

        x.put(0, 0.0f);
        y.put(0, 0.0f);
        glBegin(GL_QUADS);
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '\n') {
                y.put(0, y.get(0) + fontHeight);
                x.put(0, 0.0f);
                continue;
            } else if (c < 32 || 128 <= c)
                continue;

            stbtt_GetBakedQuad(cdata, BITMAP_W, BITMAP_H, c - 32, x, y, q, 1);

            glTexCoord2f(q.s0(), q.t0());
            glVertex2f(q.x0(), q.y0());

            glTexCoord2f(q.s1(), q.t0());
            glVertex2f(q.x1(), q.y0());

            glTexCoord2f(q.s1(), q.t1());
            glVertex2f(q.x1(), q.y1());

            glTexCoord2f(q.s0(), q.t1());
            glVertex2f(q.x0(), q.y1());
        }
        glEnd();

        glPopMatrix();
    }

    public void initialize() {
        x = BufferUtils.createFloatBuffer(1);
        y = BufferUtils.createFloatBuffer(1);
        q = STBTTAlignedQuad.malloc();

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
    }

    public void destroy() {
        q.free();
        MemoryUtil.memFree(cdata);
    }
}
