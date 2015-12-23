package net.scriptgate.engine;

public interface Application {

    default void onTick(InputComponent inputComponent, double elapsedTime) {
    }

    default void onUpdate(int ticks, int frames) {
    }

    default void initialize() {
    }

    default void render(Renderer renderer) {
    }

    default void onClick(int x, int y) {
    }

    default void onKeyDown(int key) {
    }

    default void destroy() {
        
    }

    default void initializeProperties() {

    }
}
