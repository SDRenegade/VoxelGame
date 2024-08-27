package main;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseListener {
    private static MouseListener instance;

    private double scrollX, scrollY;
    private double xPos, yPos;
    private double lastX, lastY;
    private boolean mouseButtonPressed[];
    private boolean mouseButtonFirstFramePressed[];
    private boolean isDragging;

    private MouseListener()
    {
        scrollX = 0.0;
        scrollY = 0.0;
        xPos = 0;
        yPos = 0;
        lastX = 0;
        lastY = 0;
        mouseButtonPressed = new boolean[3];
        mouseButtonFirstFramePressed = new boolean[3];
        isDragging = false;
    }

    public static void mousePosCallback(long window, double xPos, double yPos)
    {
        getInstance().lastX = getInstance().xPos;
        getInstance().lastY = getInstance().yPos;
        getInstance().xPos = xPos;
        getInstance().yPos = yPos;
        getInstance().isDragging = getInstance().mouseButtonPressed[0] || getInstance().mouseButtonPressed[1] || getInstance().mouseButtonPressed[2];
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods)
    {
        if(button >= getInstance().mouseButtonPressed.length)
            return;

        if(action == GLFW_PRESS) {
            getInstance().mouseButtonPressed[button] = true;
            getInstance().mouseButtonFirstFramePressed[button] = true;
        }
        else if(action == GLFW_RELEASE) {
            getInstance().mouseButtonPressed[button] = false;
            getInstance().mouseButtonFirstFramePressed[button] = false;
            getInstance().isDragging = false;
        }
    }

    public static void mouseScrollCallback(long window, double xOffset, double yOffset)
    {
        getInstance().scrollX = xOffset;
        getInstance().scrollY = yOffset;
    }

    public static void endFrame()
    {
        getInstance().scrollX = 0;
        getInstance().scrollY = 0;
        getInstance().lastX = getInstance().xPos;
        getInstance().lastY = getInstance().yPos;
    }

    public static float getX() { return (float)getInstance().xPos; }

    public static float getY() { return (float)getInstance().yPos; }

    public static float getDx() { return (float)(getInstance().lastX - getInstance().xPos); }

    public static float getDy() { return (float)(getInstance().lastY - getInstance().yPos); }

    public static float getScrollX() { return (float)getInstance().scrollX; }

    public static float getScrollY() { return (float)getInstance().scrollY; }

    public static boolean isDragging() { return getInstance().isDragging; }

    public static boolean mouseButtonHeldDown(int button)
    {
        return button < getInstance().mouseButtonPressed.length ? getInstance().mouseButtonPressed[button] : false;
    }

    public static boolean mouseButtonDown(int button)
    {
        return button < getInstance().mouseButtonFirstFramePressed.length ? getInstance().mouseButtonFirstFramePressed[button] : false;
    }

    public static void resetMouseButtonDown()
    {
        for(int i = 0; i < getInstance().mouseButtonFirstFramePressed.length; i++)
            getInstance().mouseButtonFirstFramePressed[i] = false;
    }

    public static MouseListener getInstance()
    {
        if(instance == null)
            instance = new MouseListener();

        return instance;
    }
}
