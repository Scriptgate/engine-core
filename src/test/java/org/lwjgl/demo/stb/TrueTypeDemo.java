/*
 * Copyright LWJGL. All rights reserved.
 * License terms: http://lwjgl.org/license.php
 */
package org.lwjgl.demo.stb;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * STB TrueType demo.
 */
public final class TrueTypeDemo extends FontDemo {

    private TrueTypeFontRenderer fontRenderer;

    private TrueTypeDemo(String filePath) {
        super(14, filePath);
        fontRenderer = new TrueTypeFontRenderer(14, "demo/Fantasque.ttf");
    }

    public static void main(String[] args) {
        String filePath;
        if (args.length == 0) {
            System.out.println("Use 'ant demo -Dclass=org.lwjgl.demo.stb.TrueTypeDemo -Dargs=<path>' to load a different text file (must be UTF8-encoded).\n");
            filePath = "demo/fontTest.txt";
        } else
            filePath = args[0];

        new TrueTypeDemo(filePath).run("STB TrueType Demo");
    }

    @Override
    protected void loop() {
        glClearColor(43f / 255f, 43f / 255f, 43f / 255f, 0f); // BG color

        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        fontRenderer.initialize();

        while (glfwWindowShouldClose(getWindow()) == GLFW_FALSE) {
            glfwPollEvents();
            glClear(GL_COLOR_BUFFER_BIT);

            fontRenderer.render(text, getScale(), getLineOffset());

            glfwSwapBuffers(getWindow());
        }

        fontRenderer.destroy();

        glfwDestroyWindow(getWindow());
    }
}