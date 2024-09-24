package util;

public class RectPivot {
    public static final RectPivot CENTER = new RectPivot(0.5f, 0.5f);
    public static final RectPivot TOP_LEFT = new RectPivot(0f, 1f);
    public static final RectPivot TOP_RIGHT = new RectPivot(1f, 1f);
    public static final RectPivot BOTTOM_LEFT = new RectPivot(0f, 0f);
    public static final RectPivot BOTTOM_RIGHT = new RectPivot(1f, 0f);
    public static final RectPivot TOP_CENTER = new RectPivot(0.5f, 1f);
    public static final RectPivot BOTTOM_CENTER = new RectPivot(0.5f, 0f);
    public static final RectPivot LEFT_CENTER = new RectPivot(0f, 0.5f);
    public static final RectPivot RIGHT_CENTER = new RectPivot(1f, 0.5f);

    private float xPos, yPos;

    private RectPivot(float xPos, float yPos)
    {
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public float getX() { return xPos; }

    public float getY() { return yPos; }
}
