package net.scriptgate.engine.opengl;

import net.scriptgate.engine.Application;
import net.scriptgate.engine.Engine;
import net.scriptgate.engine.Renderer;

import static net.scriptgate.common.Color3f.RED;
import static net.scriptgate.engine.ApplicationHandlerBuilder.run;
import static net.scriptgate.engine.ApplicationType.OPENGL;

public class OpenGLRendererTest implements Application {

    public static void main(String[] args) {
        run(new OpenGLRendererTest()).in(OPENGL);
    }

    @Override
    public void initializeProperties() {
        Engine.WIDTH = 512;
        Engine.HEIGHT = 512;
    }

    @Override
    public void render(Renderer renderer) {
        renderer.setColor(RED);
        renderer.drawLine(0, 0, 0, 512);
    }
}
