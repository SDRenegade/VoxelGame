package main;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyListener {
    private static KeyListener instance;

    private boolean keyPressed[];

    private KeyListener()
    {
        keyPressed = new boolean[350];
    }

    public static void keyCallback(long window, int key, int scanCode, int action, int mods)
    {
        if(key >= getInstance().keyPressed.length)
            return;

        if(action == GLFW_PRESS)
            getInstance().keyPressed[key] = true;
        else if(action == GLFW_RELEASE) {
            getInstance().keyPressed[key] = false;
        }
    }

    public static boolean isKeyPressed(int keyCode)
    {
        return keyCode < getInstance().keyPressed.length ? getInstance().keyPressed[keyCode] : false;
    }

    public static KeyListener getInstance()
    {
        if(instance == null)
            instance = new KeyListener();

        return instance;
    }
}
