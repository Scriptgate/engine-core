package net.scriptgate.engine;

public class EngineAdapter<T extends Renderer> extends EngineBase {

    protected final Application application;
    protected final T renderer;

    public EngineAdapter(Application application, T renderer, InputComponent inputComponent) {
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
        application.initialize();
    }

    @Override
    protected void render() {
        application.render(renderer);
    }

    @Override
    public void destroy() {
        application.destroy();
    }

    @Override
    protected void initializeProperties() {
        super.initializeProperties();
        application.initializeProperties();
    }
}
