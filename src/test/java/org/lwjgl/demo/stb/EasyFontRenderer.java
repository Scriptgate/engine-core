package org.lwjgl.demo.stb;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBEasyFont.stb_easy_font_print;

class EasyFontRenderer {
    private int fontHeight;

    public EasyFontRenderer(int fontHeight) {
        this.fontHeight = fontHeight;
    }

    public void initialize() {
        glEnableClientState(GL_VERTEX_ARRAY);
    }

    public void render(String text, int scale, int lineOffset) {
        glColor3f(169f / 255f, 183f / 255f, 198f / 255f); // Text color

        ByteBuffer charBuffer = BufferUtils.createByteBuffer(text.length() * 270);
        int quads = stb_easy_font_print(0, 0, text, null, charBuffer);
        glVertexPointer(2, GL_FLOAT, 16, charBuffer);

        float scaleFactor = 1.0f + scale * 0.25f;

        glPushMatrix();
        // Zoom
        glScalef(scaleFactor, scaleFactor, 1f);
        // Scroll
        glTranslatef(4.0f, 4.0f - lineOffset * fontHeight, 0f);

        glDrawArrays(GL_QUADS, 0, quads * 4);

        glPopMatrix();
    }

    public void destroy() {
        glDisableClientState(GL_VERTEX_ARRAY);
    }
}
