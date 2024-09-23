package scenes;

import components.Camera;
import window.GameObject;
import renderer.Renderer;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene {
    protected Renderer renderer;
    protected Camera camera;
    protected List<GameObject> gameObjects;
    protected boolean isRunning;

    public Scene()
    {
        renderer = new Renderer();
        gameObjects = new ArrayList<>();
        isRunning = false;
    }

    public abstract void init();

    public void start()
    {
        isRunning = true;
        for(GameObject go : gameObjects) {
            go.start();
        }
    }

    public void addGameObjectToScene(GameObject go)
    {
        gameObjects.add(go);
        if(isRunning) {
            go.start();
        }
    }

    public abstract void update(double dt);

    public Camera getCamera() { return camera; }

    public Renderer getRenderer() { return renderer; }
}
