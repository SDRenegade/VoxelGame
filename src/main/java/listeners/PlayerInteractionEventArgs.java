package listeners;

import world.Location;

public class PlayerInteractionEventArgs {
    private Location location;

    public PlayerInteractionEventArgs(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }
}
