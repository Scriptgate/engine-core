package net.scriptgate.engine.fx;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import net.scriptgate.engine.Application;
import net.scriptgate.engine.EngineAdapter;
import net.scriptgate.engine.InputComponent;

public class FXEngine extends EngineAdapter {

    private final Canvas canvas;


    public FXEngine(final Application application, Canvas canvas, final InputComponent inputComponent) {
        super(application, new FXRenderer(), inputComponent);
        this.canvas = canvas;
//        this.canvas.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                application.onClick(e.getX(), e.getY());
//            }
//        });
//        this.canvas.addMouseMotionListener(new MouseMotionAdapter() {
//            @Override
//            public void mouseMoved(MouseEvent e) {
//                inputComponent.mouseMoved(e.getX(), e.getY());
//            }
//        });
//        this.canvas.addKeyListener(new KeyListener() {
//            @Override
//            public void keyTyped(KeyEvent e) {
//                application.onKeyDown(e.getKeyCode());
//            }
//
//            @Override
//            public void keyPressed(KeyEvent e) {
//                inputComponent.setShiftPressed(e.isShiftDown());
//                inputComponent.keyPressed(e.getKeyCode());
//            }
//
//            @Override
//            public void keyReleased(KeyEvent e) {
//                inputComponent.setShiftPressed(e.isShiftDown());
//                inputComponent.keyReleased(e.getKeyCode());
//            }
//        });


        //canvas.addEventHandler();

        //--fx
        // Clear away portions as the user drags the mouse
//        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED,
//                new EventHandler<MouseEvent>() {
//                    @Override
//                    public void handle(MouseEvent e) {
//                        gc.clearRect(e.getX() - 2, e.getY() - 2, 5, 5);
//                    }
//                });
//
//        // Fill the Canvas with a Blue rectangle when the user double-clicks
//        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED,
//                new EventHandler<MouseEvent>() {
//                    @Override
//                    public void handle(MouseEvent t) {
//                        if (t.getClickCount() >1) {
//                            reset(canvas, Color.BLUE);
//                        }
//                    }
//                });
    }

    @Override
    public void render() {
        GraphicsContext graphicsContext2D = canvas.getGraphicsContext2D();

        clearScreen(graphicsContext2D);

        ((FXRenderer) renderer).loadContext(graphicsContext2D);

        application.render(renderer);
    }

    private void clearScreen(GraphicsContext graphicsContext2D) {
        graphicsContext2D.setFill(new Color(BG_COLOR.r, BG_COLOR.g, BG_COLOR.b, BG_COLOR.a));
        graphicsContext2D.fillRect(0, 0, WIDTH, HEIGHT);
    }
}
