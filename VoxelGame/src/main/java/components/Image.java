package components;

import util.Color32;

public class Image extends Graphic {
    private Color32 color;
    private int textureIndex;

    public Image()
    {
        color = new Color32();
        textureIndex = -1;
    }

    public Image(Color32 color)
    {
        this.color = color;
        textureIndex = -1;
    }

    public Image(int textureIndex)
    {
        color = new Color32();
        this.textureIndex = textureIndex;
    }

    public Image(Color32 color, int textureIndex)
    {
        this.color = color;
        this.textureIndex = textureIndex;
    }

    public Color32 getColor() { return color; }

    public void setColor(Color32 color) { this.color = color; }

    public int getTextureIndex() { return textureIndex; }

    public void setTextureIndex(int textureIndex) { this.textureIndex = textureIndex; }
}
