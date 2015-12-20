package net.scriptgate.engine;

public class EngineAdapter extends Engine {

    protected final Application application;
    protected final Renderer renderer;

    public EngineAdapter(Application application, Renderer renderer, InputComponent inputComponent) {
        super(inputComponent);
        this.application = application;
        this.renderer = renderer;
    }

    @Override
    protected void onTick(InputComponent inputComponent, double elapsedTime) {
        application.onTick(inputComponent, elapsedTime);
    }

    @Override
    protected void onUpdate(int ticks, int frames) {
        application.onUpdate(ticks, frames);
    }

    @Override
    protected void initialize() {
        super.initialize();
        application.initialize();
    }

    @Override
    protected void render() {
        application.render(renderer);
    }
}
