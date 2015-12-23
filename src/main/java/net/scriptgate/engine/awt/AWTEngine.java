package net.scriptgate.engine.awt;

import net.scriptgate.engine.Application;
import net.scriptgate.engine.EngineAdapter;
import net.scriptgate.engine.InputComponent;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;

public class AWTEngine extends EngineAdapter {

    private final Canvas screen;

    public AWTEngine(final Application application, Canvas screen, final InputComponent inputComponent) {
        super(application, new AWTRenderer(), inputComponent);
        this.screen = screen;

        addMouseListener(application);
        addMouseMotionListener(inputComponent);
        addKeyListener(application, inputComponent);
        addComponentListener(inputComponent);
    }

    private void addComponentListener(final InputComponent inputComponent) {
        this.screen.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                //TODO: Will we support resizing? If not, remove this
                inputComponent.setScreenSize(e.getComponent().getSize().width, e.getComponent().getSize().height);
            }
        });
    }

    private void addKeyListener(final Application application, final InputComponent inputComponent) {
        this.screen.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                application.onKeyDown(e.getKeyCode());
            }

            @Override
            public void keyPressed(KeyEvent e) {
                inputComponent.setShiftPressed(e.isShiftDown());
                inputComponent.keyPressed(e.getKeyCode());
            }

            @Override
            public void keyReleased(KeyEvent e) {
                inputComponent.setShiftPressed(e.isShiftDown());
                inputComponent.keyReleased(e.getKeyCode());
            }
        });
    }

    private void addMouseMotionListener(final InputComponent inputComponent) {
        this.screen.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                inputComponent.mouseMoved(e.getX(), e.getY());
            }
        });
    }

    private void addMouseListener(final Application application) {
        this.screen.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                application.onClick(e.getX(), e.getY());
            }
        });
    }

    @Override
    public void render() {
        BufferStrategy bs = screen.getBufferStrategy();
        if (bs == null) {
            screen.createBufferStrategy(2);
            screen.requestFocus();
            return;
        }
        Graphics2D graphics = (Graphics2D) bs.getDrawGraphics();
        clearScreen(graphics);

        ((AWTRenderer) renderer).loadGraphics(graphics);
        application.render(renderer);

        graphics.dispose();
        bs.show();
    }

    private void clearScreen(Graphics2D graphics) {
        graphics.setColor(new Color(BG_COLOR.r, BG_COLOR.g, BG_COLOR.b, 1.0f));
        graphics.fillRect(0, 0, WIDTH, HEIGHT);
    }

    @Override
    public void destroy() {
        super.destroy();
        System.exit(0);
    }
}
