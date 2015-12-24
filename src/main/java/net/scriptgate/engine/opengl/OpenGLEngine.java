package net.scriptgate.engine.opengl;

import net.scriptgate.engine.Application;
import net.scriptgate.engine.EngineAdapter;
import net.scriptgate.engine.InputComponent;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.system.libffi.Closure;

import static java.lang.System.exit;
import static org.lwjgl.glfw.Callbacks.glfwInvoke;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;


public class OpenGLEngine extends EngineAdapter {

    private long window;

    private final GLFWWindowSizeCallback windowSizeCallback;
    private final GLFWFramebufferSizeCallback framebufferSizeCallback;

    private final GLFWKeyCallback keyCallback;

    private final GLFWMouseButtonCallback mouseButtonCallback;
    private final GLFWCursorPosCallback cursorPosCallback;

    private final GLFWErrorCallback errorCallback;
    private Closure debugCallback;


    public OpenGLEngine(Application application, InputComponent input) {
        super(application, new OpenGLRenderer(), input);

//      TODO: Is it possible to redirect error callback to log?
        errorCallback = GLFWErrorCallback.createPrint(System.err);

        keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                switch (action) {
                    case GLFW_RELEASE:
                        input.keyReleased(key);
                        break;
                    case GLFW_PRESS:
                        input.keyPressed(key);
                        application.onKeyDown(key);
                        break;
                    case GLFW_REPEAT:
                        break;
                    default:
                        throw new IllegalArgumentException(String.format("Unsupported key action: 0x%X", action));
                }

                if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                    GLFW.glfwSetWindowShouldClose(window, GL_TRUE);
                }
            }
        };

        mouseButtonCallback = new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                switch (action) {
                    case GLFW_RELEASE:
                        input.setMouseReleased();
                        application.onClick(input.getMouseX(), input.getMouseY());
                        break;
                    case GLFW_PRESS:
                        input.setMousePressed();
                        break;
                    default:
                        throw new IllegalArgumentException(String.format("Unsupported mouse button action: 0x%X", action));
                }
            }
        };

        cursorPosCallback = new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xpos, double ypos) {
                input.mouseMoved((int) xpos, (int) ypos);
            }
        };

        windowSizeCallback = new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long window, int width, int height) {
//              TODO: Support resizing
//              Engine.WIDTH = width;
//              Engine.HEIGHT = height;

                glMatrixMode(GL_PROJECTION);
                glLoadIdentity();
                glOrtho(0.0, width, height, 0.0, -1.0, 1.0);
                glMatrixMode(GL_MODELVIEW);
                glLoadIdentity();
            }
        };

        framebufferSizeCallback = new GLFWFramebufferSizeCallback() {
            @Override
            public void invoke(long window, int width, int height) {
/*
                While the size of a window is measured in screen coordinates, OpenGL works with pixels.
                The size you pass into glViewport, for example, should be in pixels. On some machines screen
                coordinates and pixels are the same, but on others they will not be. There is a second
                set of functions to retrieve the size, in pixels, of the framebuffer of a window.

                If you wish to be notified when the framebuffer of a window is resized, whether by the user
                or the system, set a size callback.
*/
                glViewport(0, 0, width, height);
            }
        };
    }

    @Override
    protected void onTick(InputComponent inputComponent, double elapsedTime) {
        glfwPollEvents();
        super.onTick(inputComponent, elapsedTime);
    }

    @Override
    protected boolean isRunning() {
        return glfwWindowShouldClose(window) == GL_FALSE;
    }

    @Override
    public void start() {
        errorCallback.set();

        if (glfwInit() != GL_TRUE) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GL_FALSE);

        scheduler.execute(this);
    }

    @Override
    protected void initialize() {
        window = glfwCreateWindow(WIDTH, HEIGHT, TITLE, NULL, NULL);

        if (window == NULL) {
            throw new IllegalStateException("Failed to create the GLFW window");
        }

        windowSizeCallback.set(window);
        framebufferSizeCallback.set(window);
        keyCallback.set(window);
        mouseButtonCallback.set(window);
        cursorPosCallback.set(window);

//      Center window
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window,
                (vidmode.width() - WIDTH) / 2,
                (vidmode.height() - HEIGHT) / 2);


//      Create context
        glfwMakeContextCurrent(window);
        GL.createCapabilities();
        debugCallback = GLUtil.setupDebugMessageCallback();

        glfwSwapInterval(verticalSyncDisabled ? 0 : 1);

/*
        https://www.opengl.org/wiki/GLSL_:_common_mistakes#Enable_Or_Not_To_Enable
        Enable Or Not To Enable

        With fixed pipeline, you needed to call glEnable(GL_TEXTURE_2D) to enable 2D texturing.
        You also needed to call glEnable(GL_LIGHTING). Since shaders override these functionalities,
        you don't need to glEnable/glDisable. If you don't want texturing, you either need to write
        another shader that doesn't do texturing, or you can attach an all-white or all-black texture,
        depending on your needs. You can also write one shader that does lighting and one that doesn't.
        For things that are not overriden by shaders, like the alpha test, depth test, stencil test,
        calling glEnable/glDisable will have an effect.
*/

//      2D, in-order-rendering, disable depth test
        glDisable(GL_DEPTH_TEST);
//      We use transparency, so enable blending
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glClearColor(BG_COLOR.r, BG_COLOR.g, BG_COLOR.b, 1.0f);

        glfwShowWindow(window);
        glfwInvoke(window, windowSizeCallback, framebufferSizeCallback);
        super.initialize();
    }

    @Override
    protected void render() {
        glClear(GL_COLOR_BUFFER_BIT);

        application.render(renderer);

        glfwSwapBuffers(window);
    }

    @Override
    public void destroy() {
        super.destroy();
        try {
            if (debugCallback != null) {
                debugCallback.release();
            }
            glfwDestroyWindow(window);
            keyCallback.release();
            mouseButtonCallback.release();
            cursorPosCallback.release();
            framebufferSizeCallback.release();
            windowSizeCallback.release();
        } finally {
            glfwTerminate();
            errorCallback.release();
        }
        exit(0);
    }

}
