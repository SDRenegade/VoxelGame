package scenes;

import blocks.Block;
import components.PlayerInteraction;
import components.RectTransform;
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
    World world;
    ChunkSystem chunkSystem;
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
                new RectTransform(new Vector3f(), new Vector3f(), new Vector3f(1f, 1f, 1f), 20f, 20f, 0f, 0f));
        UiRenderer uiRenderer = new UiRenderer();
        crosshairs.addComponent(uiRenderer);
        addGameObjectToScene(crosshairs);


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
