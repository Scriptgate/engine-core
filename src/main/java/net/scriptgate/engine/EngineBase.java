package net.scriptgate.engine;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;

import static java.lang.System.currentTimeMillis;
import static java.lang.System.nanoTime;
import static java.util.concurrent.Executors.newSingleThreadExecutor;

public abstract class EngineBase implements Runnable {

    protected final ExecutorService scheduler;
    protected long lastTime;
    private long maximumTicksPerRun;
    private int ticksPerRun;
    private int msPerUpdate;
    private double accumulator = 0;
    private int ticks = 0;
    private int frames = 0;
    private long lastTimer;
    private InputComponent inputComponent;

    private boolean initialized = true;

    private boolean running = false;

    protected EngineBase(InputComponent inputComponent) {
        this.scheduler = newSingleThreadExecutor();
        this.inputComponent = inputComponent;
    }

    @Override
    public void run() {
        if (initialized) {
            initializeProperties();
            initialize();
            initialized = false;
            lastTime = nanoTime();
            lastTimer = currentTimeMillis();
        }
        long now = nanoTime();
        double nsPerTick = 1_000_000_000.0 / ticksPerRun;
        double unprocessed = (now - lastTime) / nsPerTick;
        lastTime = now;

        if (unprocessed > maximumTicksPerRun) {
            unprocessed = maximumTicksPerRun;
        }

        accumulator += unprocessed;

        boolean shouldRender = Engine.verticalSyncDisabled;

        while (accumulator >= 1) {
            accumulator -= 1;
            ticks++;
            onTick(inputComponent, nsPerTick / 1_000_000);
            shouldRender = true;
        }

        if (shouldRender) {
            frames++;
            render();
        }

        if (currentTimeMillis() - lastTimer > msPerUpdate) {
            lastTimer += msPerUpdate;
            onUpdate(ticks, frames);
            frames = 0;
            ticks = 0;
        }

        if (isRunning()) {
            scheduler.execute(this);
        } else {
            destroy();
        }
    }

    protected abstract void onTick(InputComponent inputComponent, double elapsedTime);

    protected abstract void onUpdate(int ticks, int frames);

    protected abstract void initialize();

    protected void initializeProperties() {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream engineProperties = EngineBase.class.getClassLoader().getResourceAsStream("properties/engine.json")) {
            JsonNode properties = mapper.readTree(engineProperties);
            Engine.WIDTH = properties.path("width").asInt();
            Engine.HEIGHT = properties.path("height").asInt();
            Engine.TITLE = properties.path("title").asText();
            maximumTicksPerRun = properties.path("maximumTicksPerRun").asLong();
            ticksPerRun = properties.path("ticksPerRun").asInt();
            msPerUpdate = properties.path("msPerUpdate").asInt();
            Engine.verticalSyncDisabled = properties.path("verticalSyncDisabled").asBoolean();
        } catch (IOException ex) {
            throw new RuntimeException("Exception loading engine properties", ex);
        }
    }

    protected abstract void render();

    protected boolean isRunning() {
        return running;
    }

    public void start() {
        running = true;
        scheduler.execute(this);
    }

    public void stop() {
        running = false;
    }

    public abstract void destroy();
}
