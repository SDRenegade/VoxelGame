package components;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import util.MatrixMath;
import util.Vec2f;
import window.Window;

public class RectTransform extends Transform {
    private Vec2f dimensions;
    private Vec2f anchor;

    public RectTransform()
    {
        dimensions = new Vec2f(50.0f, 50.0f);
        anchor = new Vec2f();
    }

    public RectTransform(float width, float height, float xAnchor, float yAnchor)
    {
        dimensions = new Vec2f(width, height);
        anchor = new Vec2f(xAnchor, yAnchor);
    }

    public RectTransform(Vector3f position, Vector3f rotation, Vector3f scale, float width, float height, float xAnchor, float yAnchor)
    {
        super(position, rotation, scale);
        dimensions = new Vec2f(width, height);
        anchor = new Vec2f(xAnchor, yAnchor);
    }

    public Matrix4f getTransformationMatrix()
    {
        return MatrixMath.createTransformationMatrixUI(
                new Vec2f((position.x / Window.DEFAULT_WINDOW_WIDTH) * ((float)Window.DEFAULT_WINDOW_WIDTH / (float)Window.getInstance().getWidth()), (position.y / Window.DEFAULT_WINDOW_HEIGHT) * ((float)Window.DEFAULT_WINDOW_HEIGHT / (float)Window.getInstance().getHeight()) ),
                new Vec2f(1f, 1f));
    }

    public Vec2f getDimensions() { return dimensions; }

    public void setDimensions(Vec2f dimensions) { this.dimensions = dimensions; }

    public Vec2f getAnchor() { return anchor; }

    public void setAnchor(Vec2f anchor) { this.anchor = anchor; }
}
