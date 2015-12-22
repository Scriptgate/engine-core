package org.lwjgl.demo.stb;


import net.scriptgate.engine.opengl.EasyFontRenderer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * STB Easy Font demo.
 */
public final class EasyFont extends FontDemo {

    public static void main(String[] args) {
        String filePath;
        if (args.length == 0) {
            System.out.println("Use 'ant demo -Dclass=org.lwjgl.demo.stb.EasyFont -Dargs=<path>' to load a different text file (must be UTF8-encoded).\n");
            filePath = "demo/fontTest.txt";
        } else
            filePath = args[0];

        new EasyFont(filePath).run("STB Easy Font Demo");
    }

    private static final int BASE_HEIGHT = 12;
    private EasyFontRenderer fontRenderer;

    private EasyFont(String filePath) {
        super(BASE_HEIGHT, filePath);
        fontRenderer = new EasyFontRenderer(BASE_HEIGHT);
    }

    @Override
    protected void loop() {
        fontRenderer.initialize();

        glClearColor(43f / 255f, 43f / 255f, 43f / 255f, 0f); // BG color

        while (glfwWindowShouldClose(getWindow()) == GLFW_FALSE) {
            glfwPollEvents();

            glClear(GL_COLOR_BUFFER_BIT);

            fontRenderer.render(getText(), getScale(), getLineOffset());

            glfwSwapBuffers(getWindow());
        }

        fontRenderer.destroy();

        glfwDestroyWindow(getWindow());
    }
}


