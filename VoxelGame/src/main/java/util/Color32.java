package util;

public class Color32 {
    public int r, g, b, a;

    public Color32()
    {
        r = g = b = a = 255;
    }

    public Color32(int r, int g, int b, int a)
    {
        setRed32(r);
        setGreen32(g);
        setBlue32(b);
        setAlpha32(a);
    }

    public int getRed32() { return r; }

    public int getGreen32() { return g; }

    public int getBlue32() { return b; }

    public int getAlpha32() { return a; }

    public Color32 setRed32(int r) {
        this.r = MathUtil.clamp(r, 0, 255);

        return this;
    }

    public Color32 setGreen32(int g) {
        this.g = MathUtil.clamp(g, 0, 255);

        return this;
    }

    public Color32 setBlue32(int b) {
        this.b = MathUtil.clamp(b, 0, 255);

        return this;
    }

    public Color32 setAlpha32(int a) {
        this.a = MathUtil.clamp(a, 0, 255);

        return this;
    }

    public float getRed01() { return (float)r / (float)255; }

    public float getGreen01() { return (float)g / (float)255; }

    public float getBlue01() { return (float)b / (float)255; }

    public float getAlpha01() { return (float)a / (float)255; }
}
