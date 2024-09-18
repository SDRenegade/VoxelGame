package components;

import window.Window;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_SHIFT;

import window.KeyListener;
import window.MouseListener;
import world.ChunkData;

public class Camera extends Component {
    private Vector3f camPos;
    private Vector3f camForward;
    private Vector3f camUp;
    private float camSpeed;
    private float yaw, pitch;
    private float lastX, lastY;
    private float mouseSensitivity;

    private float updateTimer = -1;

    public Camera()
    {
        camPos = new Vector3f(0f, 155f, 3f);
        camForward = new Vector3f(0f, 0f, -1f);
        camUp = new Vector3f(0f, 1f, 0f);
        camSpeed = 10f;
        yaw = -90;
        pitch = 0;
        lastX = 0;
        lastY = 0;
        mouseSensitivity = 0.07f;
    }

    @Override
    public void update(double dt)
    {
        if(KeyListener.isKeyPressed(GLFW_KEY_W))
            camPos.add(new Vector3f(camForward.x, 0f, camForward.z).normalize().mul((float)(camSpeed * dt)));
        if(KeyListener.isKeyPressed(GLFW_KEY_S))
            camPos.sub(new Vector3f(camForward.x, 0f, camForward.z).normalize().mul((float)(camSpeed * dt)));
        if(KeyListener.isKeyPressed(GLFW_KEY_A))
            camPos.sub(new Vector3f(camForward).cross(camUp).normalize().mul((float)(camSpeed * dt)));
        if(KeyListener.isKeyPressed(GLFW_KEY_D))
            camPos.add(new Vector3f(camForward).cross(camUp).normalize().mul((float)(camSpeed * dt)));

        if(KeyListener.isKeyPressed(GLFW_KEY_SPACE))
            camPos.y += camSpeed * dt;
        if(KeyListener.isKeyPressed(GLFW_KEY_LEFT_SHIFT))
            camPos.y -= camSpeed * dt;

        yaw += (MouseListener.getX() - lastX) * mouseSensitivity;
        lastX = MouseListener.getX();
        pitch += (lastY - MouseListener.getY()) * mouseSensitivity;
        lastY = MouseListener.getY();
        if(pitch > 89.0f)
            pitch = 89.0f;
        if(pitch < -89.0f)
            pitch = -89.0f;
        Vector3f dir = new Vector3f();
        dir.x = (float)(Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
        dir.y = (float)(Math.sin(Math.toRadians(pitch)));
        dir.z = (float)(Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
        camForward = dir.normalize();

        // Print camera pos to console
        updateTimer -= dt;
        if(updateTimer < 0) {
            System.out.println("Cam Position x: " + camPos.x + " y: " + camPos.y + " z: " + camPos.z);
            int x = camPos.x >= 0 ? (int)(camPos.x / 16) : (int)(camPos.x / ChunkData.CHUNK_SIZE) - 1;
            int z = -64 >= 0 ? (int)(camPos.z / 16) : (int)(camPos.z / ChunkData.CHUNK_SIZE) - 1;
            //System.out.println("Chunk X: " + x + " Chunk Z: " + z);
            updateTimer = 0.35f;
        }
    }

    public Vector3f getPos() { return camPos; }

    public void setPos(Vector3f camPos) { this.camPos = camPos; }

    public Vector3f getForward() { return camForward; }

    public void setForward(Vector3f camForward) { this.camForward = camForward; }

    public Vector3f getUp() { return camUp; }

    public void setUp(Vector3f camUp) { this.camUp = camUp; }

    public float getSpeed() { return camSpeed; }

    public void setSpeed(float camSpeed) { this.camSpeed = camSpeed; }

    public float getYaw() { return yaw; }

    public void setYaw(float yaw) { this.yaw = yaw; }

    public float getPitch() { return pitch; }

    public void setPitch(float pitch) { this.pitch = pitch; }

    public float getMouseSensitivity() { return mouseSensitivity; }

    public void setMouseSensitivity(float mouseSensitivity) { this.mouseSensitivity = mouseSensitivity; }

    public Matrix4f getViewMatrix()
    {
        return new Matrix4f().lookAt(camPos, new Vector3f(camPos.x + camForward.x, camPos.y + camForward.y, camPos.z + camForward.z), camUp);
    }

    public Matrix4f getProjectionMatrix()
    {
        return new Matrix4f().perspective(45.0f, (float)Window.getInstance().getWidth() / Window.getInstance().getHeight(), 0.1f, 600f);
    }
}
