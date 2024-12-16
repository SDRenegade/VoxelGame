package components.ui;

import util.Color32;

public class Button extends Graphic {
    private Color32 color;
    private int textureIndex;

    public Button()
    {
        color = new Color32();
        textureIndex = -1;
    }

    public Button(Color32 color)
    {
        this.color = color;
        textureIndex = -1;
    }

    public Button(int textureIndex)
    {
        color = new Color32();
        this.textureIndex = textureIndex;
    }

    public Color32 getColor() { return color; }

    public void setColor(Color32 color) { this.color = color; }

    public int getTextureIndex() { return textureIndex; }

    public void setTextureIndex(int textureIndex) { this.textureIndex = textureIndex; }
}
