package world;

import org.joml.Vector3f;

public class Location {
    private World world;
    private float x, y, z;

    public Location(World world, float x, float y, float z)
    {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Location(World world, Vector3f coords)
    {
        this.world = world;
        x = coords.x;
        y = coords.y;
        z = coords.z;
    }

    public boolean equals(Location loc)
    {
        return (this.x == loc.x && this.y == loc.y && this.z == loc.z);
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }
}
