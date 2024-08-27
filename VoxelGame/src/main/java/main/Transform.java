package main;

import org.joml.Vector3f;

public class Transform {
    private Vector3f position;
    private Vector3f rotation;
    private Vector3f scale;

    public Transform()
    {
        position = new Vector3f(0f, 0f, 0f);
        rotation = new Vector3f(0f, 0f, 0f);
        scale = new Vector3f(0f, 0f, 0f);
    }

    public Transform(Vector3f position, Vector3f rotation, Vector3f scale)
    {
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }

    public Vector3f getPosition() { return position; }

    public void setPosition(Vector3f position) { this.position = position; }

    public Vector3f getRotation() { return rotation; }

    public void setRotation(Vector3f rotation) { this.rotation = rotation; }

    public Vector3f getScale() { return scale; }

    public void setScale(Vector3f scale) { this.scale = scale; }
}
