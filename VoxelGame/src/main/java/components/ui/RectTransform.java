package components.ui;

import components.Transform;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import util.MatrixMath;
import util.RectPivot;
import util.Vec2f;
import window.Window;

public class RectTransform extends Transform {
    private Vec2f dimensions;
    private RectPivot anchor;
    private RectPivot pivot;

    public RectTransform() {
        dimensions = new Vec2f(50.0f, 50.0f);
        anchor = RectPivot.CENTER;
        pivot = RectPivot.CENTER;
    }

    public RectTransform(float width, float height, RectPivot anchor, RectPivot pivot) {
        dimensions = new Vec2f(width, height);
        this.anchor = anchor;
        this.pivot = pivot;
    }

    public RectTransform(Vector3f position, Vector3f rotation, Vector3f scale, float width, float height, RectPivot anchor, RectPivot pivot) {
        super(position, rotation, scale);
        dimensions = new Vec2f(width, height);
        this.anchor = anchor;
        this.pivot = pivot;
    }

    public Matrix4f getTransformationMatrix() {
        return MatrixMath.createTransformationMatrixUI(
                new Vec2f((position.x / Window.DEFAULT_WINDOW_WIDTH) * ((float)Window.DEFAULT_WINDOW_WIDTH / (float)Window.getInstance().getWidth()) * 2, (position.y / Window.DEFAULT_WINDOW_HEIGHT) * ((float)Window.DEFAULT_WINDOW_HEIGHT / (float)Window.getInstance().getHeight()) * 2),
                new Vec2f(1f, 1f));
    }

    public Vec2f getDimensions() {
        return dimensions;
    }

    public void setDimensions(float width, float height) {
        dimensions.x = width;
        dimensions.y = height;
    }

    public RectPivot getAnchor() {
        return anchor;
    }

    public void setAnchor(RectPivot anchor) {
        this.anchor = anchor;
    }

    public RectPivot getPivot() {
        return pivot;
    }

    public void setPivot(RectPivot pivot) {
        this.pivot = pivot;
    }
}
