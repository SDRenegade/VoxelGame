package util;

public class Vec3i {
    public int x, y, z;

    public Vec3i()
    {
        x = y = z = 0;
    }

    public Vec3i(int x, int y, int z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void copy(Vec3i copyVec)
    {
        x = copyVec.x;
        y = copyVec.y;
        z = copyVec.z;
    }
}
