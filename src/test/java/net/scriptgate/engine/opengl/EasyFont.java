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
    private String text;

    public EasyFont() {
        this.fontRenderer = new EasyFontRenderer();
    }

    @Override
    public void initialize() {
        fontRenderer.initialize();
        text = "1234567111";
        System.out.println(text.length());
    }

    @Override
    public void render(Renderer renderer) {
        fontRenderer.render(10, 10, text);
    }

    @Override
    public void destroy() {
        fontRenderer.destroy();
    }
}
