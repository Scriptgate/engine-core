package net.scriptgate.engine.opengl;

import net.scriptgate.common.Point;
import net.scriptgate.common.Rectangle;

import static net.scriptgate.common.Color3f.*;
import static net.scriptgate.engine.ApplicationHandlerBuilder.run;
import static net.scriptgate.engine.ApplicationType.OPENGL;
import static net.scriptgate.engine.Engine.HEIGHT;
import static net.scriptgate.engine.Engine.WIDTH;

public class OpenGLTTFRendererTest extends RendererTest {

    public static void main(String[] args) {
        run(new OpenGLTTFRendererTest()).in(OPENGL);
    }

    public OpenGLTTFRendererTest() {
        addTestCase("Test all characters", renderer -> {
            int textY = 100;
            Rectangle textBounds;

            textBounds = renderer.drawText(100, textY, "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
            textY += 5 + textBounds.height;

            textBounds = renderer.drawText(100, textY, "abcdefghijklmnopqrstuvwxyz");
            textY += 5 + textBounds.height;

            textBounds = renderer.drawText(100, textY, "0123456789");
            textY += 5 + textBounds.height;

            renderer.drawText(100, textY, "!@#$%^&*()-_+={}[]:;'\"<>?,./\\|~`");
        });


        addTestCase("Test text and image combination", renderer -> {
            renderer.drawText(235, 200, "Test Tile");
            renderer.drawImage(256, 220, "images/testTile");
        });

        addTestCase("Test bounds from text", renderer -> {
            Point textPosition = new Point(200, 256);
            Rectangle textBounds = renderer.drawText(textPosition.x, textPosition.y, "abcdefghijklmnopqrstuvwxyz");

            renderer.drawRect(textBounds.x - 2, textBounds.y - 2, textBounds.width + 4, textBounds.height + 4);
        });

        addTestCase("Test get bounds", renderer -> {
            Point textPosition = new Point(256, 256);
            Rectangle textBounds = renderer.getBounds(textPosition.x, textPosition.y, "abcdefghijklmnopqrstuvwxyz");

            textPosition.x -= textBounds.width / 2;
            textBounds.x -= textBounds.width / 2;

            renderer.setColor(GREEN);
            renderer.fillRect(0, 0, textBounds.x, HEIGHT);
            renderer.fillRect(textBounds.x + textBounds.width, 0, WIDTH - (textBounds.x + textBounds.width), HEIGHT);

            renderer.setColor(RED);
            renderer.fillRect(textBounds.x, 0, textBounds.width, HEIGHT);

            renderer.setColor(WHITE);
            renderer.fillRect(textBounds.x, textBounds.y, textBounds.width, textBounds.height);

            renderer.setColor(BLACK);
            renderer.drawText(textPosition.x, textPosition.y, "abcdefghijklmnopqrstuvwxyz");

            renderer.setColor(WHITE);
        });
    }
}
