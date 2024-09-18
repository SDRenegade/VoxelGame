package util;


import org.joml.Matrix4f;
import org.joml.Vector2f;

public class MatrixMath {
    public static Matrix4f createTransformationMatrixUI(Vector2f translation, Vector2f scale)
    {
        Matrix4f matrix = new Matrix4f();
        matrix.identity();
        matrix.translate(translation.x, translation.y, 0f);
        matrix.scale(scale.x, scale.y, 1f);
        return matrix;
    }
}
