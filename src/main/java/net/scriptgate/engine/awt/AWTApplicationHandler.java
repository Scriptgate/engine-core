package net.scriptgate.engine.awt;

import net.scriptgate.engine.Application;
import net.scriptgate.engine.ApplicationHandler;
import net.scriptgate.engine.Engine;

import javax.swing.JFrame;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

public class AWTApplicationHandler implements ApplicationHandler {

    @Override
    public void start(Application application) {
        Canvas screen = new Canvas();
        screen.setPreferredSize(new Dimension(Engine.WIDTH, Engine.HEIGHT));

        final JFrame window = new JFrame(Engine.TITLE);
        final AWTEngine engine = new AWTEngine(application, screen, new AWTInputComponent());

        window.setIgnoreRepaint(true);
        screen.setIgnoreRepaint(true);

        window.add(screen);
        window.setResizable(false);
        window.setVisible(true);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                engine.stop();
            }
        });

        engine.start();
    }
}
