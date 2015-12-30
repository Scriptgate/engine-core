package net.scriptgate.engine.opengl;

import net.scriptgate.engine.Application;
import net.scriptgate.engine.InputComponent;
import net.scriptgate.engine.Renderer;

import static net.scriptgate.common.Color3f.*;
import static net.scriptgate.engine.ApplicationHandlerBuilder.run;
import static net.scriptgate.engine.ApplicationType.OPENGL;
import static net.scriptgate.engine.Engine.HEIGHT;
import static net.scriptgate.engine.Engine.WIDTH;

public class OpenGLTrigonometry implements Application {

    public static void main(String[] args) {
        run(new OpenGLTrigonometry()).in(OPENGL);
    }

    private static final int RADIUS = 128;
    private double angle = 0;

    @Override
    public void initializeProperties() {
        WIDTH = 512;
        HEIGHT = 512;
    }

    @Override
    public void onTick(InputComponent inputComponent, double elapsedTime) {
        int deltaX = inputComponent.getMouseX() - WIDTH / 2;
        int deltaY = inputComponent.getMouseY() - HEIGHT / 2;

        angle = Math.atan2(-deltaY, deltaX);
    }

    @Override
    public void render(Renderer renderer) {
        renderBackground(renderer);

        renderer.translate(WIDTH / 2, HEIGHT / 2);
        new TrigonometryRenderer(renderer, RADIUS, angle).render();
        renderer.translate(-WIDTH / 2, -HEIGHT / 2);
    }

    private void renderBackground(Renderer renderer) {
        renderer.setColor(1, 1, 1, 1);
        renderer.fillRect(0, 0, WIDTH, HEIGHT);
    }

    private static class TrigonometryRenderer {

        public void renderCeiling() {
            drawCosecant();
            drawY();
            drawCotangent();
        }

        public void renderWall() {
            drawSecant();
            drawTangent();
            drawX();
        }

        public void renderDome() {
            drawRadius();
            drawSine();
            drawCosine();
        }

        public void render() {
            renderCeiling();
            renderWall();
            renderDome();
        }

        private final int radius;
        private final double angle;
        private Renderer renderer;

        private TrigonometryRenderer(Renderer renderer, int radius, double angle) {
            this.renderer = renderer;
            this.radius = radius;
            this.angle = angle;

            renderer.setColor(0.5f, 0, 0, 0);
            renderer.drawCircle(0, 0, radius);
            renderer.drawLine(
                    -radius, -HEIGHT / 2,
                    -radius, HEIGHT / 2);
            renderer.drawLine(
                    radius, -HEIGHT / 2,
                    radius, HEIGHT / 2);
            renderer.drawLine(
                    -WIDTH / 2, -radius,
                    WIDTH / 2, -radius);
            renderer.drawLine(
                    -WIDTH / 2, radius,
                    WIDTH / 2, radius);
            renderer.setOpacity(1);
        }

        private void setXColor() {
            renderer.setColor(BLUE);
        }

        private void setYColor() {
            renderer.setColor(GREEN);
        }

        private void setRadialColor() {
            renderer.setColor(RED);
        }

        private void drawCosine() {
            setXColor();
            drawLine(0, 0, cosine(), 0);
        }

        private void drawRadius() {
            setRadialColor();

            drawLine(0, 0, cosine(), sine());
        }

        private void drawSine() {
            setYColor();
            drawLine(cosine(), 0, cosine(), sine());
        }

        private void drawX() {
            setXColor();
            int sign = getSignX();
            drawLine(0, 0, sign * radius, 0);
        }

        private void drawSecant() {
            setRadialColor();
            int sign = getSignX();
            drawLine(0, 0, sign * radius, sign * tangent());
        }

        private void drawTangent() {
            setYColor();
            int sign = getSignX();
            drawLine(sign * radius, 0, sign * radius, sign * tangent());
        }

        private int getSignX() {
            return cosine() < 0 ? -1 : 1;
        }

        private void drawCotangent() {
            setXColor();
            int sign = getSignY();
            drawLine(0, 0, sign * cotangent(), 0);
        }

        private void drawCosecant() {
            setRadialColor();
            int sign = getSignY();
            drawLine(0, 0, sign * cotangent(), sign * radius);
        }

        private void drawY() {
            setYColor();
            int sign = getSignY();
            drawLine(sign * cotangent(), 0, sign * cotangent(), sign * radius);
        }

        private int getSignY() {
            return sine() < 0 ? -1 : 1;
        }

        private void drawLine(double x1, double y1, double x2, double y2) {
            int xi1 = castToInt(x1);
            int yi1 = castToInt(y1);
            int xi2 = castToInt(x2);
            int yi2 = -castToInt(y2);
            renderer.drawLine(xi1, yi1, xi2, yi2);
        }

        private int castToInt(double d) {
            if (d > Integer.MAX_VALUE) {
                return Integer.MAX_VALUE;
            }
            if (d < Integer.MIN_VALUE + 1) {
                return Integer.MIN_VALUE + 1;
            }
            return (int) d;
        }

        private double sine() {
            return radius * Math.sin(angle);
        }

        private double cosine() {
            return radius * Math.cos(angle);
        }

        private double tangent() {
            return radius * Math.tan(angle);
        }

        private double cotangent() {
            return radius / Math.tan(angle);
        }
    }
}
