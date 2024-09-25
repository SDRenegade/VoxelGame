package window;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import scenes.SceneManager;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    public static final int DEFAULT_WINDOW_WIDTH = 1920;
    public static final int DEFAULT_WINDOW_HEIGHT = 1080;

    private static Window instance;

    private int width, height;
    private String title;
    private long glfwWindow;

    private Window()
    {
        width = 960;
        height = 540;
        title = "Voxel Game main.Window";
    }

    public void run()
    {
        init();
        loop();

        // Free memory & terminate GLFW
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void init()
    {
        // Setup an error callback
        GLFWErrorCallback.createPrint(System.err);

        // Initialize GLFW
        if(!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW.");

        // Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_FALSE);

        // Create the window. The variable glfwWindow is storing the memory location
        // of the window.
        glfwWindow = glfwCreateWindow(width, height, title, NULL, NULL);
        if(glfwWindow == NULL)
            throw new IllegalStateException("Failed to create the GLFW window");

        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);
        // Callback function for whenever the window size changes
        glfwSetFramebufferSizeCallback(glfwWindow, Window::framebuffer_size_callback);

        // Make the OpenGL context current
        glfwMakeContextCurrent(glfwWindow);
        // Enable v-sync
        glfwSwapInterval(0);
        // Make window visible
        glfwShowWindow(glfwWindow);
        // Necessary code to run LWJGL with GLFW
        GL.createCapabilities();

        // Enable any OpenGL functionality that we will want. In this case
        // we want to enable depth testing.
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
        glEnable(GL_LINE_SMOOTH);
        // Enabling antialiasing
        glfwWindowHint(GLFW_SAMPLES, 4);

        //glfwSetInputMode(glfwWindow, GLFW_CURSOR, GLFW_CURSOR_DISABLED);

        //glfwMaximizeWindow(glfwWindow);

        SceneManager.switchScene(0);
    }

    public void loop()
    {
        double beginTime = glfwGetTime();
        double endTime;
        double dt = -1;

        while(!glfwWindowShouldClose(glfwWindow)) {
            endTime = glfwGetTime();
            dt = endTime - beginTime;
            beginTime = endTime;

            MouseListener.resetMouseButtonDown();
            // Poll events
            glfwPollEvents();

            glClearColor(0.75f, 0.75f, 0.75f, 1f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            SceneManager.getCurrentScene().update(dt);

            glfwSwapBuffers(glfwWindow);
        }
    }

    public static void framebuffer_size_callback(long window, int width, int height)
    {
        // make sure the viewport matches the new window dimensions; note that width and
        // height will be significantly larger than specified on retina displays.
        Window.getInstance().setWidth(width);
        Window.getInstance().setHeight(height);
        glViewport(0, 0, width, height);
    }

    public int getWidth() { return width; }

    public void setWidth(int width) { this.width = width; }

    public int getHeight() { return height; }

    public void setHeight(int height) { this.height = height; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public static float getAspectRatio() { return (float)getInstance().getWidth() / (float)getInstance().getHeight(); }

    public static Window getInstance()
    {
        if(instance == null)
            instance = new Window();

        return instance;
    }
}
