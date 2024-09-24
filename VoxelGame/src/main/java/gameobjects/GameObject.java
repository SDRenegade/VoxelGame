package gameobjects;

import components.Component;
import components.RectTransform;
import components.Transform;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GameObject {
    private UUID uuid;
    private Transform transform;
    private List<Component> components;

    public GameObject(boolean isGuiObject)
    {
        uuid = UUID.randomUUID();
        transform = isGuiObject ? new RectTransform() : new Transform();
        components = new ArrayList<>();
        addComponent(transform);
    }

    public GameObject(Transform transform)
    {
        uuid = UUID.randomUUID();
        this.transform = transform;
        components = new ArrayList<>();
        addComponent(transform);
    }

    public GameObject(RectTransform transform)
    {
        uuid = UUID.randomUUID();
        this.transform = transform;
        components = new ArrayList<>();
        addComponent(transform);
    }

    public <T extends Component> T getComponent(Class<T> componentClass)
    {
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

    public <T extends Component> void removeComponent(Class<T> componentClass)
    {
        for(int i = 0; i < components.size(); i++) {;
            if(componentClass.isAssignableFrom(components.get(i).getClass())) {
                components.remove(i);
                return ;
            }
        }
    }

    public void addComponent(Component component)
    {
        components.add(component);
        component.setGameObject(this);
    }

    public void start()
    {
        for(int i = 0; i < components.size(); i++) {
            components.get(i).setGameObject(this);
            components.get(i).start();
        }
    }

    public void update(double dt)
    {
        for(int i = 0; i < components.size(); i++)
            components.get(i).update(dt);
    }

    public UUID getUUID() { return uuid; }

    public Transform getTransform() { return transform; }
}
