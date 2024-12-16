package items;

import org.joml.Vector3f;
import world.Location;

public interface InventoryItem {
    void renderItem();

    void interact(Location location);
}
