package scenes;

import blocks.Block;
import components.PlayerInteraction;
import components.QuadRenderer;
import main.GameObject;
import components.Camera;
import main.KeyListener;
import main.Transform;
import main.Window;
import org.joml.Vector3f;
import util.AssetPool;
import world.ChunkSystem;
import world.World;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;

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
        World world = new World();

        // Instantiate GameObjects into scene
        GameObject cameraObj = new GameObject("Main Camera");
        camera = new Camera();
        cameraObj.addComponent(camera);
        addGameObjectToScene(cameraObj);

        GameObject player = new GameObject("Player");
        PlayerInteraction playerInteraction = new PlayerInteraction();
        playerInteraction.setCamera(camera);
        playerInteraction.setWorld(world);
        player.addComponent(playerInteraction);
        addGameObjectToScene(player);

        GameObject crosshair = new GameObject("Crosshair");


        // Init ChunkSystem
        chunkSystem = new ChunkSystem(camera.getCamPos(), world);

        // Old rendering
        /*for(int i = 0; i < 20; i++) {
            for(int j = 0; j < 50; j++) {
                for(int k = 0; k < 50; k++) {
                    GameObject go = new GameObject(
                            "X: " + k + ", Y: " + i + " Z: " + j,
                            new Transform(new Vector3f(k * 1f, i * 1f, j * 1f), new Vector3f(0f, 0f, 0f), new Vector3f(1f, 1f, 1f))
                    );
                    go.addComponent(new QuadRenderer(Block.VERTEX_ARRAY));
                    addGameObjectToScene(go);
                }
            }
        }*/

    }

    @Override
    public void update(double dt)
    {
        //if(1/dt < 50)
            //System.out.println("Frame Rate: " + 1/dt + " At time: " + System.nanoTime());

        // Temporary application closing hotkey
        if(KeyListener.isKeyPressed(GLFW_KEY_ESCAPE))
            System.exit(0);

        for(GameObject go : gameObjects)
            go.update(dt);

        //renderer.render();
        chunkSystem.update(dt);
    }
}
