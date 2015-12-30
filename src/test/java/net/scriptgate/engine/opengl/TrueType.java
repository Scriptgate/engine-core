package net.scriptgate.engine.opengl;

import net.scriptgate.engine.Application;
import net.scriptgate.engine.Engine;
import net.scriptgate.engine.Renderer;

import static net.scriptgate.engine.ApplicationHandlerBuilder.run;
import static net.scriptgate.engine.ApplicationType.OPENGL;

public class TrueType implements Application {


    public static void main(String[] args) {
        run(new TrueType()).in(OPENGL);
    }

    private OpenGLTTFRenderer fontRenderer;

    public TrueType() {
        this.fontRenderer = new OpenGLTTFRenderer();
    }

    @Override
    public void initializeProperties() {
        Engine.WIDTH = 512;
        Engine.HEIGHT = 512;
    }

    @Override
    public void initialize() {
        fontRenderer.initialize();
    }

    @Override
    public void render(Renderer renderer) {
        int textY = 100;

        fontRenderer.render(100, textY, "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        textY += 5 + fontRenderer.getFontHeight();

        fontRenderer.render(100, textY, "abcdefghijklmnopqrstuvwxyz");
        textY += 5 + fontRenderer.getFontHeight();

        fontRenderer.render(100, textY, "0123456789");
        textY += 5 + fontRenderer.getFontHeight();

        fontRenderer.render(100, textY, "! @ # $ % ^ & * ( ) - _ + = { } [ ] : ; ' \" < > ? , . / \\ | ~ `");


        renderer.drawImage(256, 220, "images/testTile");
    }

    @Override
    public void destroy() {
        fontRenderer.destroy();
    }
}
