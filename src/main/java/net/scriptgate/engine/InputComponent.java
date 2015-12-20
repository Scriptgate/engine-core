package net.scriptgate.engine;

import java.util.ArrayList;
import java.util.List;

public abstract class InputComponent {

    private final List<Integer> pressedKeys;

    private int mouseX = 0;
    private int mouseY = 0;

    private int screenWidth = Engine.WIDTH;
    private int screenHeight = Engine.HEIGHT;

    private boolean shiftPressed = false;
    private boolean mousePressed = false;

    public InputComponent() {
        this.pressedKeys = new ArrayList<>();
    }

    public int getMouseX() {
        return this.mouseX;
    }

    public int getMouseY() {
        return this.mouseY;
    }

    public void mouseMoved(int x, int y) {
        this.mouseX = x;
        this.mouseY = y;
    }

    public boolean isShiftPressed() {
        return shiftPressed;
    }

    public void setShiftPressed(boolean shiftPressed) {
        this.shiftPressed = shiftPressed;
    }

    public Integer[] getPressedKeys() {
        Integer[] pressed = new Integer[pressedKeys.size()];
        pressedKeys.toArray(pressed);
        return pressed;
    }

    public void keyPressed(int key) {
        if (!pressedKeys.contains(key)) {
            pressedKeys.add(key);
        }
    }

    public void keyReleased(int key) {
        if (pressedKeys.contains(key)) {
            pressedKeys.remove(pressedKeys.indexOf(key));
        }
    }

    public abstract int up();

    public abstract int down();

    public abstract int left();

    public abstract int right();

    public void setMousePressed() {
        mousePressed = true;
    }

    public void setMouseReleased() {
        mousePressed = false;
    }

    public void setScreenSize(int width, int height) {
        screenWidth = width;
        screenHeight = height;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }
}

