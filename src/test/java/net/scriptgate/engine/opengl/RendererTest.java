package net.scriptgate.engine.opengl;

import net.scriptgate.common.Rectangle;
import net.scriptgate.engine.Application;
import net.scriptgate.engine.Renderer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static net.scriptgate.common.Color3f.BLACK;
import static net.scriptgate.common.Color3f.WHITE;
import static net.scriptgate.engine.Engine.HEIGHT;
import static net.scriptgate.engine.Engine.WIDTH;
import static org.lwjgl.glfw.GLFW.*;

public abstract class RendererTest implements Application {

    private List<RendererTestCase> testCases;
    private RendererTestCase testCase;

    public RendererTest() {
        testCases = new ArrayList<>();
    }

    protected void addTestCase(String title, Consumer<Renderer> testCase) {
        testCases.add(new RendererTestCase(title, testCase));
    }

    @Override
    public void initializeProperties() {
        WIDTH = 512;
        HEIGHT = 512;
    }

    @Override
    public void initialize() {
        if (testCases.isEmpty()) {
            throw new IllegalStateException("No renderer tests found");
        }
        testCase = testCases.iterator().next();
    }

    @Override
    public void onKeyDown(int key) {
        switch (key) {
            case GLFW_KEY_RIGHT:
                int indexOfNextTestCase = testCases.indexOf(testCase) + 1;
                if (indexOfNextTestCase >= testCases.size()) {
                    testCase = testCases.get(0);
                } else {
                    testCase = testCases.get(indexOfNextTestCase);
                }
                break;
            case GLFW_KEY_LEFT:
                int indexOfPreviousTestCase = testCases.indexOf(testCase) - 1;
                if (indexOfPreviousTestCase < 0) {
                    testCase = testCases.get(testCases.size() - 1);
                } else {
                    testCase = testCases.get(indexOfPreviousTestCase);
                }
                break;
        }
        if (key >= GLFW_KEY_1 && key <= GLFW_KEY_9) {
            int numeric = (key - GLFW_KEY_0 - 1);
            quickLookup(numeric);
        }
    }

    private void quickLookup(int numeric) {
        if (numeric >= testCases.size()) {
            return;
        }
        testCase = testCases.get(numeric);
    }

    @Override
    public void render(Renderer renderer) {
        testCase.renderTest.accept(renderer);

        Rectangle bounds = renderer.getBounds(10, 15, getTestCaseDescription());
        renderer.setColor(1, WHITE);
        renderer.fillRect(bounds.x - 3, bounds.y - 3, bounds.width + 6, bounds.height + 6);
        renderer.setColor(1, BLACK);
        renderer.drawRect(bounds.x - 3, bounds.y - 3, bounds.width + 6, bounds.height + 6);
        renderer.drawText(10, 15, getTestCaseDescription());

        renderer.setColor(1, WHITE);
    }

    private String getTestCaseDescription() {
        int testIndex = testCases.indexOf(testCase) + 1;
        return "Test Method (" + testIndex + "/" + testCases.size() + "): " + testCase.description;
    }

    private class RendererTestCase {

        private final String description;
        private final Consumer<Renderer> renderTest;

        public RendererTestCase(String description, Consumer<Renderer> renderTest) {
            this.description = description;
            this.renderTest = renderTest;
        }
    }
}
