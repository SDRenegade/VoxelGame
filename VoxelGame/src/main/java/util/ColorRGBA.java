package util;

public class ColorRGBA {
    private int red, green, blue;
    private float alpha;

    public ColorRGBA(int red, int green, int blue)
    {
        this.red = red;
        this.green = green;
        this.blue = blue;
        alpha = 1f;
    }

    public ColorRGBA(int red, int green, int blue, float alpha)
    {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public int getRed() { return red; }

    public void setRed(int value)
    {
        MathUtil.clamp(value, 0, 255);
        red = value;
    }

    public int getGreen() { return green; }

    public void setGreen(int value)
    {
        MathUtil.clamp(value, 0, 255);
        green = value;
    }

    public int getBlue() { return blue; }

    public void setBlue(int value)
    {
        MathUtil.clamp(value, 0, 255);
        blue = value;
    }

    public float getAlpha() { return alpha; }

    public void setAlpha(float value)
    {
        MathUtil.clamp(value, 0f, 1f);
        alpha = value;
    }
}
