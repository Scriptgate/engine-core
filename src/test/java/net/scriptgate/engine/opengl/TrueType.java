package net.scriptgate.engine.opengl;

import net.scriptgate.engine.Application;
import net.scriptgate.engine.Renderer;

import static net.scriptgate.engine.ApplicationHandlerBuilder.run;
import static net.scriptgate.engine.ApplicationType.OPENGL;

public class TrueType implements Application {

    private String text;

    public static void main(String[] args) {
        run(new TrueType()).in(OPENGL);
    }

    private TrueTypeFontRenderer fontRenderer;

    public TrueType() {
        this.fontRenderer = new TrueTypeFontRenderer(8, "demo/Ricasso.ttf");
        text = "Story#123";
    }

    @Override
    public void initialize() {
        fontRenderer.initialize();
    }

    @Override
    public void render(Renderer renderer) {
        fontRenderer.render(100,100, text);
    }

    @Override
    public void destroy() {
        fontRenderer.destroy();
    }
}
