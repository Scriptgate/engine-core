package net.scriptgate.engine.fx;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import net.scriptgate.engine.Application;
import net.scriptgate.engine.ApplicationHandler;
import net.scriptgate.engine.Engine;

public class FXApplicationHandler extends javafx.application.Application implements ApplicationHandler {

    private static Application application;

    @Override
    public void start(Stage stage) throws Exception {
        Canvas canvas = new Canvas();
        canvas.setHeight(Engine.HEIGHT);
        canvas.setWidth(Engine.WIDTH);

        Group root = new Group(canvas);
        Scene scene = new Scene(root, Engine.WIDTH, Engine.HEIGHT);

        stage.setTitle(Engine.TITLE);
        stage.setScene(scene);
        stage.show();

        FXEngine engine = new FXEngine(application, canvas, new FXInputComponent());

        stage.setOnCloseRequest(we -> {
            stage.close();
            engine.stop();
        });

        engine.start();
    }


    @Override
    public void start(Application application) {
        throw new UnsupportedOperationException("JavaFX support is not fully implemented.");
//        FXApplicationHandler.application = application;
//        launch();
    }
}
