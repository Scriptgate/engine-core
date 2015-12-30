package net.scriptgate.engine.opengl;

import net.scriptgate.common.Point;
import net.scriptgate.common.Rectangle;
import net.scriptgate.engine.Application;
import net.scriptgate.engine.Renderer;

import java.util.function.Consumer;

import static net.scriptgate.common.Color3f.*;
import static net.scriptgate.engine.ApplicationHandlerBuilder.run;
import static net.scriptgate.engine.ApplicationType.OPENGL;
import static net.scriptgate.engine.Engine.HEIGHT;
import static net.scriptgate.engine.Engine.WIDTH;
import static org.lwjgl.glfw.GLFW.*;

@SuppressWarnings("unused")
public class OpenGLTTFRendererTest implements Application {

    private enum TestCase {
        TEST_ALL_CHARACTERS(renderer -> {
            int textY = 100;
            Rectangle textBounds;

            textBounds = renderer.drawText(100, textY, "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
            textY += 5 + textBounds.height;

            textBounds = renderer.drawText(100, textY, "abcdefghijklmnopqrstuvwxyz");
            textY += 5 + textBounds.height;

            textBounds = renderer.drawText(100, textY, "0123456789");
            textY += 5 + textBounds.height;

            renderer.drawText(100, textY, "!@#$%^&*()-_+={}[]:;'\"<>?,./\\|~`");
        }),

        TEST_TEXT_IMAGE_COMBO(renderer -> {
            renderer.drawText(235, 200, "Test Tile");
            renderer.drawImage(256, 220, "images/testTile");
        }),

        TEST_BOUNDS_FROM_TEXT(renderer -> {
            Point textPosition = new Point(200, 256);
            Rectangle textBounds = renderer.drawText(textPosition.x, textPosition.y, "abcdefghijklmnopqrstuvwxyz");

            renderer.drawRect(textBounds.x - 2, textBounds.y - 2, textBounds.width + 4, textBounds.height + 4);
        }),

        TEST_GET_BOUNDS(renderer -> {
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

        //--- Boilerplate code below ---

        private Consumer<Renderer> testMethod;

        TestCase(Consumer<Renderer> testMethod) {
            this.testMethod = testMethod;
        }
    }

    private static TestCase[] testCases = TestCase.values();
    private TestCase testCase = TestCase.TEST_ALL_CHARACTERS;

    public static void main(String[] args) {
        run(new OpenGLTTFRendererTest()).in(OPENGL);
    }

    @Override
    public void initializeProperties() {
        WIDTH = 512;
        HEIGHT = 512;
    }

    @Override
    public void onKeyDown(int key) {
        switch (key) {
            case GLFW_KEY_RIGHT:
                int indexOfNextTextCase = testCase.ordinal() + 1;
                if (indexOfNextTextCase >= testCases.length) {
                    testCase = testCases[0];
                } else {
                    testCase = testCases[indexOfNextTextCase];
                }
                break;
            case GLFW_KEY_LEFT:
                int indexOfPreviousTextCase = testCase.ordinal() - 1;
                if (indexOfPreviousTextCase < 0) {
                    testCase = testCases[testCases.length - 1];
                } else {
                    testCase = testCases[indexOfPreviousTextCase];
                }
                break;
        }
        if (key >= GLFW_KEY_1 && key <= GLFW_KEY_9) {
            int numeric = (key - GLFW_KEY_0 - 1);
            quickLookup(numeric);
        }
    }

    private void quickLookup(int numeric) {
        if (numeric >= testCases.length) {
            return;
        }
        testCase = testCases[numeric];
    }

    @Override
    public void render(Renderer renderer) {
        testCase.testMethod.accept(renderer);
    }
}
