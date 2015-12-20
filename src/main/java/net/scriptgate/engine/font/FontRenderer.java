package net.scriptgate.engine.font;

import net.scriptgate.common.Point;
import net.scriptgate.engine.Renderer;

public class FontRenderer {


    private String fontImagePath;

    public void draw(Renderer renderer, int x, int y, String message) {
        for (int i = 0; i < message.length(); i++) {
            fontImagePath = "images/font01";
            renderer.drawImage(fontImagePath, new Point(x + i * 6, y), getOffset(message.charAt(i)), new Point(6, 9));
        }
    }

    private Point getOffset(char c) {
        int index = (int) c;
        if (index >= 97 && index <= 122) {
            return new Point((index - 97) * 6, 9);
        }
        if (index >= 65 && index <= 90) {
            return new Point((index - 65) * 6, 0);
        }

        return new Point(0, 18);
    }

}
