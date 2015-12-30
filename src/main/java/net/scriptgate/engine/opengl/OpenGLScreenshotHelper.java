package net.scriptgate.engine.opengl;

import net.scriptgate.engine.Engine;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.IntBuffer;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static net.scriptgate.engine.util.ArrayUtil.flipVertically;
import static net.scriptgate.engine.util.FileUtil.getUniqueFileNameWithTimestamp;

public class OpenGLScreenshotHelper {

    private static IntBuffer buffer;
    private static int[] pixels;

    private static File screenshotFolder = new File("screenshots");

    public static String saveScreenshot(int displayWidth, int displayHeight) {
        return saveScreenshot(null, displayWidth, displayHeight);
    }

    public static BufferedImage getScreenshot() {
        return getScreenshot(0, 0, Engine.WIDTH, Engine.HEIGHT);
    }

    public static BufferedImage getScreenshot(int x, int y, int width, int height) {
        int bufferSize = width * height;

        if (buffer == null || buffer.capacity() < bufferSize) {
            buffer = BufferUtils.createIntBuffer(bufferSize);
            pixels = new int[bufferSize];
        }

        GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
        buffer.clear();
        GL11.glReadPixels(x, y, width, height, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, buffer);
        buffer.get(pixels);
        flipVertically(pixels, width, height);
        BufferedImage screenshotImage = new BufferedImage(width, height, TYPE_INT_RGB);
        screenshotImage.setRGB(0, 0, width, height, pixels, 0, width);

        return screenshotImage;
    }


    public static String saveScreenshot(String fileName, int displayWidth, int displayHeight) {
        try {
            //noinspection ResultOfMethodCallIgnored
            screenshotFolder.mkdir();

            BufferedImage screenshotImage = getScreenshot(0, 0, displayWidth, displayHeight);

            File screenshotFile;

            if (fileName == null) {
                screenshotFile = getUniqueFileNameWithTimestamp(screenshotFolder, "png");
            } else {
                screenshotFile = new File(screenshotFolder, fileName);
            }

            ImageIO.write(screenshotImage, "png", screenshotFile);
            return "Saved screenshot as " + screenshotFile.getName();
        } catch (Exception ex) {
            return "Failed to save: " + ex;
        }
    }
}
