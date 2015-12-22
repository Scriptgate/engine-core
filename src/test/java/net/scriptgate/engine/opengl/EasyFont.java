package net.scriptgate.engine.opengl;

import net.scriptgate.engine.Application;
import net.scriptgate.engine.Renderer;

import static net.scriptgate.engine.ApplicationHandlerBuilder.run;
import static net.scriptgate.engine.ApplicationType.OPENGL;

public class EasyFont implements Application {

    public static void main(String[] args) {
        run(new EasyFont()).in(OPENGL);
    }

    private EasyFontRenderer fontRenderer;

    public EasyFont() {
        this.fontRenderer = new EasyFontRenderer(12);
    }

    @Override
    public void initialize() {
        fontRenderer.initialize();
    }

    @Override
    public void render(Renderer renderer) {
        fontRenderer.render("STOR1000000000000000000000000000000000000000000000000000000000000000000000000000000002313212312312312312312315555", 1, 0);
    }

    @Override
    public void destroy() {
        fontRenderer.destroy();
    }
}
