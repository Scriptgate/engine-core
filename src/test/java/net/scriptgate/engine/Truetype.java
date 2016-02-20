package net.scriptgate.engine;

import static net.scriptgate.engine.ApplicationHandlerBuilder.run;
import static net.scriptgate.engine.ApplicationType.OPENGL;

public class Truetype implements Application {

    public static void main(String[] args) {
        run(new Truetype()).in(OPENGL);
    }

    private TrueTypeFontRenderer fontRenderer;

    public Truetype() {
        this.fontRenderer = new TrueTypeFontRenderer();
    }

    @Override
    public void initialize() {

    }
}
