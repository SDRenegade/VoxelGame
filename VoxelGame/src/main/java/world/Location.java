package world;

public class Location {
    private double x, y, z;

    public Location(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public boolean equals(Location loc)
    {
        return (this.x == loc.x && this.y == loc.y && this.z == loc.z);
    }

}
