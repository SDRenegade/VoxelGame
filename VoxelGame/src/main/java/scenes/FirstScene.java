package scenes;

import blocks.Block;
import components.*;
import components.ui.Image;
import components.ui.RectTransform;
import gameobjects.Prefabs;
import util.Color32;
import util.RectPivot;
import util.ShaderType;
import gameobjects.GameObject;
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
    public void init() {
        // Load assets into AssetPool
        AssetPool.getInstance().loadAssets();
        Block.InitBlockMap();

        // Init World
        world = new World();

        // Instantiate GameObjects into scene
        GameObject cameraObj = new GameObject(
                new Transform(new Vector3f(0f, 155f, 3f), new Vector3f(), new Vector3f(1f, 1f, 1f)));
        camera = new Camera();
        cameraObj.addComponent(camera);
        addGameObjectToScene(cameraObj);

        GameObject player = new GameObject();
        PlayerInteraction playerInteraction = new PlayerInteraction();
        playerInteraction.setCamera(camera);
        playerInteraction.setWorld(world);
        player.addComponent(playerInteraction);
        PlayerInventory playerInventory = new PlayerInventory();
        player.addComponent(playerInventory);
        addGameObjectToScene(player);

        //=========== UI ===========
        GameObject crosshairs = new GameObject(new RectTransform(25f, 25f, RectPivot.CENTER, RectPivot.CENTER));
        UiRenderer chrosshairsRenderer = new UiRenderer(AssetPool.getInstance().getShader(ShaderType.UI));
        crosshairs.addComponent(chrosshairsRenderer);
        Image crosshairsImage = new Image(0);
        crosshairs.addComponent(crosshairsImage);
        addGameObjectToScene(crosshairs);

        GameObject hotBarBackground = new GameObject(new RectTransform(590f, 70f, RectPivot.BOTTOM_CENTER, RectPivot.BOTTOM_CENTER));
        UiRenderer hotbarBackgroundRenderer = new UiRenderer(AssetPool.getInstance().getShader(ShaderType.UI));
        hotBarBackground.addComponent(hotbarBackgroundRenderer);
        Image hotBarBackgroundImage = new Image(new Color32(160, 160, 160, 255), -1);
        hotBarBackground.addComponent(hotBarBackgroundImage);
        addGameObjectToScene(hotBarBackground);

        GameObject hotbarSlot1 = Prefabs.instantiateHotbarSlot();
        hotbarSlot1.getTransform().setPosition(5f, 0f, 0f);
        hotbarSlot1.setParent(hotBarBackground);
        addGameObjectToScene(hotbarSlot1);

        GameObject hotbarSlot2 = Prefabs.instantiateHotbarSlot();
        hotbarSlot2.getTransform().setPosition(70f, 0f, 0f);
        hotbarSlot2.setParent(hotBarBackground);
        addGameObjectToScene(hotbarSlot2);

        GameObject hotbarSlot3 = Prefabs.instantiateHotbarSlot();
        hotbarSlot3.getTransform().setPosition(135f, 0f, 0f);
        hotbarSlot3.setParent(hotBarBackground);
        addGameObjectToScene(hotbarSlot3);

        GameObject hotbarSlot4 = Prefabs.instantiateHotbarSlot();
        hotbarSlot4.getTransform().setPosition(200f, 0f, 0f);
        hotbarSlot4.setParent(hotBarBackground);
        addGameObjectToScene(hotbarSlot4);

        GameObject hotbarSlot5 = Prefabs.instantiateHotbarSlot();
        hotbarSlot5.getTransform().setPosition(265f, 0f, 0f);
        hotbarSlot5.setParent(hotBarBackground);
        addGameObjectToScene(hotbarSlot5);

        GameObject hotbarSlot6 = Prefabs.instantiateHotbarSlot();
        hotbarSlot6.getTransform().setPosition(330f, 0f, 0f);
        hotbarSlot6.setParent(hotBarBackground);
        addGameObjectToScene(hotbarSlot6);

        GameObject hotbarSlot7 = Prefabs.instantiateHotbarSlot();
        hotbarSlot7.getTransform().setPosition(395f, 0f, 0f);
        hotbarSlot7.setParent(hotBarBackground);
        addGameObjectToScene(hotbarSlot7);

        GameObject hotbarSlot8 = Prefabs.instantiateHotbarSlot();
        hotbarSlot8.getTransform().setPosition(460f, 0f, 0f);
        hotbarSlot8.setParent(hotBarBackground);
        addGameObjectToScene(hotbarSlot8);

        GameObject hotbarSlot9 = Prefabs.instantiateHotbarSlot();
        hotbarSlot9.getTransform().setPosition(525f, 0f, 0f);
        hotbarSlot9.setParent(hotBarBackground);
        addGameObjectToScene(hotbarSlot9);

        // TODO Add hotbar slot cursor

        //=========== Systems ===========
        GameObject inventoryManagerObj = new GameObject();
        InventoryManager inventoryManager = new InventoryManager(playerInventory);
        inventoryManager.addPlayerInteractionListener(playerInteraction);
        inventoryManagerObj.addComponent(inventoryManager);
        addGameObjectToScene(inventoryManagerObj);

        // Init ChunkSystem
        chunkSystem = new ChunkSystem(cameraObj.getTransform().getPosition(), world);

    }

    @Override
    public void update(double dt) {
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
