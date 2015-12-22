package net.scriptgate.engine.opengl;

import net.scriptgate.engine.Application;
import net.scriptgate.engine.Renderer;

import static net.scriptgate.engine.ApplicationHandlerBuilder.run;
import static net.scriptgate.engine.ApplicationType.OPENGL;

public class TrueType implements Application {

    public static void main(String[] args) {
        run(new TrueType()).in(OPENGL);
    }

    private TrueTypeFontRenderer fontRenderer;

    public TrueType() {
        this.fontRenderer = new TrueTypeFontRenderer(12, "demo/Fantasque.ttf");
    }

    @Override
    public void initialize() {
        fontRenderer.initialize();
    }

    @Override
    public void render(Renderer renderer) {
        fontRenderer.render("STOR1000", 1, 0);
    }

    @Override
    public void destroy() {
        fontRenderer.destroy();
    }
}
