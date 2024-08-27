package components;

import main.MouseListener;
import world.World;

public class PlayerInteraction extends Component {
    private float maxRayCastLength;

    private Camera cam;
    private World world;

    @Override
    public void start()
    {
        maxRayCastLength = 5f;
    }


    @Override
    public void update(double dt)
    {
        if(!MouseListener.mouseButtonDown(0))
            return;

        float startingCellFactorX = cam.getCamPos().x - (float)Math.floor(cam.getCamPos().x);
        float startingCellFactorY = 1 - (cam.getCamPos().y - (float)Math.floor(cam.getCamPos().y));
        float startingCellFactorZ = cam.getCamPos().z - (float)Math.floor(cam.getCamPos().z);
        float sx = (float)Math.sqrt(1 + Math.pow(cam.getCamForward().y / cam.getCamForward().x, 2) + Math.pow(cam.getCamForward().z / cam.getCamForward().x, 2));
        float sy = (float)Math.sqrt(1 + Math.pow(cam.getCamForward().x / cam.getCamForward().y, 2) + Math.pow(cam.getCamForward().z / cam.getCamForward().y, 2));
        float sz = (float)Math.sqrt(1 + Math.pow(cam.getCamForward().x / cam.getCamForward().z, 2) + Math.pow(cam.getCamForward().y / cam.getCamForward().z, 2));
        int xFactor, yFactor, zFactor;
        xFactor = yFactor = zFactor = 1;

        float rayLength = 0f;
        Byte hitBlock = null;
        Vector3VG hitBlockCoords = null;
        while(rayLength <= maxRayCastLength && hitBlockCoords == null) {
            if(sx * xFactor < sy * yFactor && sx * xFactor < sz * zFactor) {
                float firstCellLength = (float)Math.sqrt(Math.pow(cam.getCamForward().x * (sx * startingCellFactorX), 2) + Math.pow(cam.getCamForward().y * (sx * startingCellFactorX), 2) + Math.pow(cam.getCamForward().z * (sx * startingCellFactorX), 2));
                rayLength = xFactor > 1 ? (float)Math.sqrt(Math.pow(cam.getCamForward().x * (sx * (xFactor - 1)), 2) + Math.pow(cam.getCamForward().y * (sx * (xFactor - 1)), 2) + Math.pow(cam.getCamForward().z * (sx * (xFactor - 1)), 2)) + firstCellLength : firstCellLength;
                xFactor++;
            }
            else if(sy * yFactor < sx * xFactor && sy * yFactor < sz * zFactor) {
                float firstCellLength = (float)Math.sqrt(Math.pow(cam.getCamForward().x * (sy * startingCellFactorY), 2) + Math.pow(cam.getCamForward().y * (sy * startingCellFactorY), 2) + Math.pow(cam.getCamForward().z * (sy * startingCellFactorY), 2));
                rayLength = yFactor > 1 ? (float)Math.sqrt(Math.pow(cam.getCamForward().x * (sy * (yFactor - 1)), 2) + Math.pow(cam.getCamForward().y * (sy * (yFactor - 1)), 2) + Math.pow(cam.getCamForward().z * (sy * (yFactor - 1)), 2)) + firstCellLength : firstCellLength;
                yFactor++;
            }
            else {
                float firstCellLength = (float)Math.sqrt(Math.pow(cam.getCamForward().x * (sz * startingCellFactorZ), 2) + Math.pow(cam.getCamForward().y * (sz * startingCellFactorZ), 2) + Math.pow(cam.getCamForward().z * (sz * startingCellFactorZ), 2));
                rayLength = zFactor > 1 ? (float)Math.sqrt(Math.pow(cam.getCamForward().x * (sz * (zFactor - 1)), 2) + Math.pow(cam.getCamForward().y * (sz * (zFactor - 1)), 2) + Math.pow(cam.getCamForward().z * (sz * (zFactor - 1)), 2)) + firstCellLength : firstCellLength;
                zFactor++;
            }

            // Positive coordinates need to be rounded down while negative coordinates need to be rounded up
            int hitBlockX = cam.getCamPos().x + (cam.getCamForward().x * rayLength) >= 0 ? (int)(cam.getCamPos().x + (cam.getCamForward().x * rayLength)) : (int)(cam.getCamPos().x + (cam.getCamForward().x * rayLength) - 1);
            int hitBlockY = (int)Math.ceil((cam.getCamPos().y + (cam.getCamForward().y * rayLength)));
            int hitBlockZ = cam.getCamPos().z + (cam.getCamForward().z * rayLength) >= 0 ? (int)(cam.getCamPos().z + (cam.getCamForward().z * rayLength)) : (int)(cam.getCamPos().z + (cam.getCamForward().z * rayLength) - 1);
            hitBlock = world.getBlock(hitBlockX, hitBlockY, hitBlockZ);
            if(hitBlock != null && hitBlock != 0)
                hitBlockCoords = new Vector3VG(hitBlockX, hitBlockY, hitBlockZ);
        }

        if(hitBlock != null && hitBlockCoords != null) {
            //System.out.println("Block interacted at X: " + hitBlockCoords.x + ", Y: " + hitBlockCoords.y + ", Z: " + hitBlockCoords.z);
            world.setBlock((int)hitBlockCoords.x, (int)hitBlockCoords.y, (int)hitBlockCoords.z, (byte)0);
        }
    }

    public void setCamera(Camera cam) { this.cam = cam; }

    public void setWorld(World world) { this.world = world; }

    public class Vector3VG {
        public float x;
        public float y;
        public float z;

        public Vector3VG(float x, float y, float z)
        {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Vector3VG mul(float value)
        {
            return new Vector3VG(x * value, y * value, z * value);
        }
    }
}
