package net.scriptgate.engine.opengl;

import net.scriptgate.engine.Application;
import net.scriptgate.engine.EngineAdapter;
import net.scriptgate.engine.InputComponent;
import org.lwjgl.Sys;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.system.glfw.ErrorCallback.Util;
import org.lwjgl.system.glfw.GLFW;
import org.lwjgl.system.glfw.GLFWvidmode;
import org.lwjgl.system.glfw.WindowCallback;
import org.lwjgl.system.glfw.WindowCallbackAdapter;
import org.lwjgl.system.windows.GLFWWin32;
import org.lwjgl.system.windows.WGL;
import org.lwjgl.system.windows.WinGDI;
import org.lwjgl.system.windows.WinUser;

import java.nio.ByteBuffer;

import static java.lang.String.format;
import static java.lang.System.exit;
import static org.lwjgl.system.glfw.GLFW.glfwCreateWindow;

public class OpenGLEngine extends EngineAdapter {

    private final InputComponent input;
    private long window;

    public OpenGLEngine(Application application, InputComponent input) {
        super(application, new OpenGLRenderer(), input);
        this.input = input;
    }

    @Override
    protected void onTick(InputComponent inputComponent, double elapsedTime) {
        GLFW.glfwPollEvents();
        super.onTick(inputComponent, elapsedTime);
    }

    @Override
    protected boolean isRunning() {
        return GLFW.glfwWindowShouldClose(window) == GL11.GL_FALSE;
    }

    @Override
    public void start() {
        Sys.touch();

        GLFW.glfwSetErrorCallback(Util.getDefault());

        if (GLFW.glfwInit() != GL11.GL_TRUE) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GL11.GL_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GL11.GL_FALSE);

        scheduler.execute(this);
    }

    @Override
    protected void initialize() {
        super.initialize();
        window = glfwCreateWindow(WIDTH, HEIGHT, TITLE, MemoryUtil.NULL, MemoryUtil.NULL);

        if (window == MemoryUtil.NULL) {
            GLFW.glfwTerminate();
            throw new RuntimeException("Failed to create the GLFW window");
        }

        addWindowCallbacks();

        ByteBuffer vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
        GLFW.glfwSetWindowPos(window,
                (GLFWvidmode.width(vidmode) - WIDTH) / 2,
                (GLFWvidmode.height(vidmode) - HEIGHT) / 2);
        GLFW.glfwShowWindow(window);

        GLFW.glfwMakeContextCurrent(window);
        GLFW.glfwSwapInterval(verticalSyncDisabled ? 0 : 1);

        //Create context
        GLContext.createFromCurrent();
        //glEnable(GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        //Camera
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();

        //Model for objects
        //glOrtho(0, WIDTH, HEIGHT, 0, -1, 1);
        GL11.glOrtho(-0.5f, WIDTH, HEIGHT, -0.5f, 0.0f, 1.0f);

        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();

        //View
        long HWND = GLFWWin32.glfwGetWin32Window(window);
        long HDC = WinUser.GetDC(HWND);
        GL11.glViewport(0, 0, WIDTH, HEIGHT);
        WinGDI.SelectObject(HDC, WinGDI.GetStockObject(WinGDI.DEFAULT_GUI_FONT));

        int fontBase = GL11.glGenLists(256);
        WGL.wglUseFontBitmaps(HDC, 0, 256, fontBase);
        GL11.glListBase(fontBase);
        //Clear screen
    }

    private void addWindowCallbacks() {
        WindowCallback.set(window, new WindowCallbackAdapter() {
            @Override
            public void windowPos(long window, int xpos, int ypos) {

            }

            @Override
            public void windowSize(long window, int width, int height) {

            }

            @Override
            public void windowClose(long window) {

            }

            @Override
            public void windowRefresh(long window) {

            }

            @Override
            public void windowFocus(long window, int focused) {

            }

            @Override
            public void windowIconify(long window, int iconified) {

            }

            @Override
            public void framebufferSize(long window, int width, int height) {

            }

            @Override
            public void key(long window, int key, int scancode, int action, int mods) {
                String state;
                switch (action) {
                    case GLFW.GLFW_RELEASE:
                        state = "released";
                        input.keyReleased(key);
                        break;
                    case GLFW.GLFW_PRESS:
                        state = "pressed";
                        input.keyPressed(key);
                        application.onKeyDown(key);
                        break;
                    case GLFW.GLFW_REPEAT:
                        state = "repeated";
                        break;
                    default:
                        throw new IllegalArgumentException(format("Unsupported key action: 0x%X", action));
                }

                if (mods == 0) {
                }

                StringBuilder modState = new StringBuilder(16);
                if ((mods & GLFW.GLFW_MOD_SHIFT) != 0) {
                    modState.append("SHIFT+");
                    input.setShiftPressed((mods & GLFW.GLFW_MOD_SHIFT) != 0);
                }
                if ((mods & GLFW.GLFW_MOD_CONTROL) != 0) {
                    modState.append("CONTROL+");
                }
                if ((mods & GLFW.GLFW_MOD_ALT) != 0) {
                    modState.append("ALT+");
                }
                if ((mods & GLFW.GLFW_MOD_SUPER) != 0) {
                    modState.append("SUPER+");
                }

                //modState.toString();
                if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE) {
                    GLFW.glfwSetWindowShouldClose(window, GL11.GL_TRUE);
                }
            }

            @Override
            public void character(long window, int codepoint) {
                application.onKeyDown(codepoint);
            }

            @Override
            public void charMods(long window, int codepoint, int mods) {

            }

            @Override
            public void mouseButton(long window, int button, int action, int mods) {
                String state;
                switch (action) {
                    case GLFW.GLFW_RELEASE:
                        input.setMouseReleased();
                        application.onClick(input.getMouseX(), input.getMouseY());
                        state = "released";
                        break;
                    case GLFW.GLFW_PRESS:
                        input.setMousePressed();
                        state = "pressed";
                        break;
                    default:
                        throw new IllegalArgumentException(format("Unsupported mouse button action: 0x%X", action));
                }
                StringBuilder modState = new StringBuilder(16);
                if ((mods & GLFW.GLFW_MOD_SHIFT) != 0) {
                    modState.append("SHIFT+");
                }
                if ((mods & GLFW.GLFW_MOD_CONTROL) != 0) {
                    modState.append("CONTROL+");
                }
                if ((mods & GLFW.GLFW_MOD_ALT) != 0) {
                    modState.append("ALT+");
                }
                if ((mods & GLFW.GLFW_MOD_SUPER) != 0) {
                    modState.append("SUPER+");
                }

                //modState.toString();
            }

            @Override
            public void cursorPos(long window, double xpos, double ypos) {
                input.mouseMoved((int) xpos, (int) ypos);
            }

            @Override
            public void cursorEnter(long window, int entered) {

            }

            @Override
            public void scroll(long window, double xoffset, double yoffset) {

            }

        });
    }

    @Override
    protected void render() {
        GL11.glClearColor(BG_COLOR.r, BG_COLOR.g, BG_COLOR.b, BG_COLOR.a);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();

        application.render(renderer);

        GLFW.glfwSwapBuffers(window);
    }

    @Override
    public void shutdown() {
        GLFW.glfwDestroyWindow(window);
        GLFW.glfwTerminate();
        exit(0);
    }

}
