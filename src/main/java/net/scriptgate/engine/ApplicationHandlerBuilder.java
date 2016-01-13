package net.scriptgate.engine;

import net.scriptgate.engine.awt.AWTApplicationHandler;
import net.scriptgate.engine.fx.FXApplicationHandler;

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;

public class ApplicationHandlerBuilder {

    private static Map<ApplicationType, Class<? extends ApplicationHandler>> applicationHandlers;

    static {
        applicationHandlers = new HashMap<>();
        applicationHandlers.put(ApplicationType.AWT, AWTApplicationHandler.class);
        applicationHandlers.put(ApplicationType.FX, FXApplicationHandler.class);
        //TODO: FIX
//        applicationHandlers.put(ApplicationType.OPENGL, OpenGLApplicationHandler.class);
    }

    public static ApplicationHandlerBuilder run(Application application) {
        return new ApplicationHandlerBuilder(application);
    }

    private final Application application;

    private ApplicationHandlerBuilder(Application application) {
        this.application = application;
    }

    public void in(ApplicationType type) {
        requireNonNull(type, "Missing ApplicationType");

        Class<? extends ApplicationHandler> applicationHandlerClass = applicationHandlers.get(type);
        requireNonNull(applicationHandlerClass, "No ApplicationHandler found for ApplicationType: " + type.name());

        ApplicationHandler applicationHandler;
        try {
            applicationHandler = applicationHandlerClass.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
        applicationHandler.start(application);
    }
}
