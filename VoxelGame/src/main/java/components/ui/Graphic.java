package components.ui;

import components.Component;
import util.Color32;

public abstract class Graphic extends Component {
    protected Color32 color;
    protected int textureIndex;

    public Color32 getColor() { return color; }

    public void setColor(Color32 color) { this.color = color; }

    public int getTextureIndex() { return textureIndex; }

    public void setTextureIndex(int textureIndex) { this.textureIndex = textureIndex; }
}
