package window;

import components.Component;
import components.RectTransform;
import components.Transform;

import java.util.ArrayList;
import java.util.List;

public class GameObject {
    private String name;
    private List<Component> components;

    public GameObject(String name, boolean isGuiObject)
    {
        this.name = name;
        components = new ArrayList<>();
        Transform transform = isGuiObject ? new RectTransform() : new Transform();
        addComponent(transform);
    }

    public GameObject(String name, Transform transform)
    {
        this.name = name;
        components = new ArrayList<>();
        addComponent(transform);
    }

    public GameObject(String name, RectTransform transform)
    {
        this.name = name;
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
        for(int i = 0; i < components.size(); i++)
            components.get(i).start(this);
    }

    public void update(double dt)
    {
        for(int i = 0; i < components.size(); i++)
            components.get(i).update(dt);
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }
}
