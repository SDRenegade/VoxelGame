package gameobjects;

import components.Component;
import components.ui.RectTransform;
import components.Transform;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GameObject {
    private UUID uuid;
    private Transform transform;
    private List<Component> components;
    private GameObject parent;
    private boolean isActive;

    public GameObject() {
        uuid = UUID.randomUUID();
        transform = new RectTransform();
        components = new ArrayList<>();
        addComponent(transform);
        parent = null;
        isActive = true;
    }

    public GameObject(Transform transform) {
        uuid = UUID.randomUUID();
        this.transform = transform;
        components = new ArrayList<>();
        addComponent(transform);
        parent = null;
        isActive = true;
    }

    public GameObject(RectTransform transform) {
        uuid = UUID.randomUUID();
        this.transform = transform;
        components = new ArrayList<>();
        addComponent(transform);
        parent = null;
        isActive = true;
    }

    public void start() {
        for(int i = 0; i < components.size(); i++) {
            components.get(i).setGameObject(this);
            components.get(i).start();
        }
    }

    public void update(double dt) {
        for(int i = 0; i < components.size(); i++)
            components.get(i).update(dt);
    }

    public <T extends Component> T getComponent(Class<T> componentClass) {
        for(Component c : components) {
            if(componentClass.isAssignableFrom(c.getClass())) {
                try {
                    return componentClass.cast(c);
                }
                catch(ClassCastException e) {
                    e.printStackTrace();
                    assert false : "Error casting component.";
                }
            }
        }

        return null;
    }

    public void addComponent(Component component) {
        components.add(component);
        component.setGameObject(this);
    }

    public <T extends Component> void removeComponent(Class<T> componentClass) {
        for(int i = 0; i < components.size(); i++) {;
            if(componentClass.isAssignableFrom(components.get(i).getClass())) {
                components.remove(i);
                return;
            }
        }
    }

    public UUID getUUID() {
        return uuid;
    }

    public Transform getTransform() {
        return transform;
    }

    public List<Component> getComponentList() {
        return components;
    }

    public GameObject getParent() {
        return parent;
    }

    public void setParent(GameObject parent) {
        this.parent = parent != this ? parent : null;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }
}
