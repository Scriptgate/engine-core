package net.scriptgate.engine.opengl;

import net.scriptgate.engine.InputComponent;

import static org.lwjgl.system.glfw.GLFW.*;

public class OpenGLInputComponent extends InputComponent {
    @Override
    public int up() {
        return GLFW_KEY_UP;
    }

    @Override
    public int down() {
        return GLFW_KEY_DOWN;
    }

    @Override
    public int left() {
        return GLFW_KEY_LEFT;
    }

    @Override
    public int right() {
        return GLFW_KEY_RIGHT;
    }
}
