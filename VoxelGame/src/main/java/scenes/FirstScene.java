package scenes;

import blocks.Block;
import components.Image;
import components.PlayerInteraction;
import components.RectTransform;
import util.Color32;
import util.RectPivot;
import util.ShaderType;
import gameobjects.GameObject;
import components.Camera;
import window.KeyListener;
import org.joml.Vector3f;
import renderer.UiRenderer;
import util.AssetPool;
import world.ChunkSystem;
import world.World;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;

public class FirstScene extends Scene {
    private World world;
    private ChunkSystem chunkSystem;

    public FirstScene() {}

    @Override
    public void init()
    {
        // Load assets into AssetPool
        AssetPool.getInstance().loadAssets();
        Block.InitBlockMap();

        // Init World
        world = new World();

        // Instantiate GameObjects into scene
        GameObject cameraObj = new GameObject(false);
        camera = new Camera();
        cameraObj.addComponent(camera);
        addGameObjectToScene(cameraObj);

        GameObject player = new GameObject(false);
        PlayerInteraction playerInteraction = new PlayerInteraction();
        playerInteraction.setCamera(camera);
        playerInteraction.setWorld(world);
        player.addComponent(playerInteraction);
        addGameObjectToScene(player);

        GameObject crosshairs = new GameObject(new RectTransform(25f, 25f, RectPivot.CENTER, RectPivot.CENTER));
        UiRenderer chrosshairsRenderer = new UiRenderer(AssetPool.getInstance().getShader(ShaderType.UI));
        crosshairs.addComponent(chrosshairsRenderer);
        Image crosshairsImage = new Image(0);
        crosshairs.addComponent(crosshairsImage);
        addGameObjectToScene(crosshairs);

        GameObject hotbarSlot1 = new GameObject(
                new RectTransform(new Vector3f(0f, 50f, 0f), new Vector3f(), new Vector3f(1f, 1f, 1f), 70f, 70f, RectPivot.BOTTOM_CENTER, RectPivot.CENTER));
        UiRenderer rendererHotbar1 = new UiRenderer(AssetPool.getInstance().getShader(ShaderType.UI));
        hotbarSlot1.addComponent(rendererHotbar1);
        Image hotbarSlot1Image = new Image(new Color32(100, 100, 100, 175), -1);
        hotbarSlot1.addComponent(hotbarSlot1Image);
        addGameObjectToScene(hotbarSlot1);

        // Init ChunkSystem
        chunkSystem = new ChunkSystem(camera.getPos(), world);

    }

    @Override
    public void update(double dt)
    {
        //if(1/dt < 50)
            //System.out.println("Frame Rate: " + 1/dt + " At time: " + System.nanoTime());

        // Temporary close application hotkey
        if(KeyListener.isKeyPressed(GLFW_KEY_ESCAPE))
            System.exit(0);

        for(GameObject go : gameObjects)
            go.update(dt);

        chunkSystem.update(dt);

        for(GameObject go : gameObjects) {
            if(go.getComponent(UiRenderer.class) != null) {
                UiRenderer uiRenderer = go.getComponent(UiRenderer.class);
                uiRenderer.render();
            }
        }
    }
}
