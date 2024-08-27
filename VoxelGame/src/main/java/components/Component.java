package components;

import main.GameObject;

public abstract class Component {
    protected GameObject gameObject;

    public Component() {}

    public void start() {}

    public abstract void update(double dt);

    public GameObject getGameObject() { return gameObject; }

    public void setGameObject(GameObject gameObject) { this.gameObject = gameObject; }
}
