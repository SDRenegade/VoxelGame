package gameobjects;

import components.ui.Image;
import components.ui.RectTransform;
import renderer.UiRenderer;
import util.AssetPool;
import util.Color32;
import util.RectPivot;
import util.ShaderType;

public class Prefabs  {
    public static GameObject instantiateHotbarSlot()
    {
        GameObject hotbarSlot = new GameObject(new RectTransform(60f, 60f, RectPivot.LEFT_CENTER, RectPivot.LEFT_CENTER));
        UiRenderer renderer = new UiRenderer(AssetPool.getInstance().getShader(ShaderType.UI));
        hotbarSlot.addComponent(renderer);
        Image image = new Image(new Color32(80, 80, 80, 200), -1);
        hotbarSlot.addComponent(image);

        return hotbarSlot;
    }
}
