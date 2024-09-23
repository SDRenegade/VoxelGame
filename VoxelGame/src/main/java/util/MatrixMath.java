package util;


import org.joml.Matrix4f;

public class MatrixMath {
    public static Matrix4f createTransformationMatrixUI(Vec2f translation, Vec2f scale)
    {
        Matrix4f matrix = new Matrix4f();
        matrix.identity();
        matrix.translate(translation.x, translation.y, 0f);
        matrix.scale(scale.x, scale.y, 1f);
        return matrix;
    }
}
