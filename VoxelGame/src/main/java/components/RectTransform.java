package components;

import org.joml.Vector3f;
import util.Vec2f;

public class RectTransform extends Transform {
    private Vec2f dimensions;
    private Vec2f anchorPivot;

    public RectTransform()
    {
        super();
        dimensions = new Vec2f(0.5f, 0.5f);
        anchorPivot = new Vec2f();
    }

    public RectTransform(Vector3f position, Vector3f rotation, Vector3f scale, float width, float height, float xPivot, float yPivot)
    {
        super(position, rotation, scale);
        dimensions = new Vec2f(width, height);
        anchorPivot = new Vec2f(xPivot, yPivot);
    }

    public Vec2f getDimensions() { return dimensions; }

    public void setDimensions(Vec2f dimensions) { this.dimensions = dimensions; }

    public Vec2f getAnchorPivot() { return anchorPivot; }

    public void setAnchorPivot(Vec2f anchorPivot) { this.anchorPivot = anchorPivot; }
}
