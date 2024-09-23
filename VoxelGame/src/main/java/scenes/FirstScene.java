package scenes;

import blocks.Block;
import components.PlayerInteraction;
import components.RectTransform;
import util.Color32;
import util.ShaderType;
import window.GameObject;
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
        GameObject cameraObj = new GameObject("Main Camera", false);
        camera = new Camera();
        cameraObj.addComponent(camera);
        addGameObjectToScene(cameraObj);

        GameObject player = new GameObject("Player", false);
        PlayerInteraction playerInteraction = new PlayerInteraction();
        playerInteraction.setCamera(camera);
        playerInteraction.setWorld(world);
        player.addComponent(playerInteraction);
        addGameObjectToScene(player);

        GameObject crosshairs = new GameObject("Crosshairs",
                new RectTransform(25f, 25f, 0.5f, 0.5f));
        UiRenderer rendererCrosshairs = new UiRenderer(
                AssetPool.getInstance().getShader(ShaderType.UI), 0, new Color32(255, 255, 255, 255));
        crosshairs.addComponent(rendererCrosshairs);
        addGameObjectToScene(crosshairs);

        GameObject hotbarSlot1 = new GameObject("Hotbar Slot 1",
                new RectTransform(new Vector3f(0f, 65f, 0f), new Vector3f(), new Vector3f(1f, 1f, 1f), 60f, 60f, 0.5f, 0f));
        UiRenderer rendererHotbar1 = new UiRenderer(
                AssetPool.getInstance().getShader(ShaderType.UI), -1, new Color32(100, 100, 100, 175));
        hotbarSlot1.addComponent(rendererHotbar1);
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
                uiRenderer.renderCrosshairs();
            }
        }
    }
}
