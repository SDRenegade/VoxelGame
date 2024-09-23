package components;

import window.GameObject;
import window.MouseListener;
import renderer.Shader;
import scenes.SceneManager;
import util.AssetPool;
import util.ShaderType;
import util.Vec3f;
import util.Vec3i;
import world.World;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class PlayerInteraction extends Component {
    public static final float OUTLINE_OFFSET = 0.0f;
    public static final float[] CUBE_OUTLINE_VERTEX_POSITIONS = {
            // Top edges
            0.0f - OUTLINE_OFFSET,  0.0f + OUTLINE_OFFSET,  1.0f + OUTLINE_OFFSET,
            0.0f - OUTLINE_OFFSET,  0.0f + OUTLINE_OFFSET,  0.0f - OUTLINE_OFFSET,
            0.0f - OUTLINE_OFFSET,  0.0f + OUTLINE_OFFSET,  0.0f - OUTLINE_OFFSET,
            1.0f + OUTLINE_OFFSET,  0.0f + OUTLINE_OFFSET,  0.0f - OUTLINE_OFFSET,
            1.0f + OUTLINE_OFFSET,  0.0f + OUTLINE_OFFSET,  0.0f - OUTLINE_OFFSET,
            1.0f + OUTLINE_OFFSET,  0.0f + OUTLINE_OFFSET,  1.0f + OUTLINE_OFFSET,
            1.0f + OUTLINE_OFFSET,  0.0f + OUTLINE_OFFSET,  1.0f + OUTLINE_OFFSET,
            0.0f - OUTLINE_OFFSET,  0.0f + OUTLINE_OFFSET,  1.0f + OUTLINE_OFFSET,

            // Side edges
            0.0f - OUTLINE_OFFSET,  0.0f + OUTLINE_OFFSET,  0.0f - OUTLINE_OFFSET,
            0.0f - OUTLINE_OFFSET, -1.0f - OUTLINE_OFFSET,  0.0f - OUTLINE_OFFSET,
            1.0f + OUTLINE_OFFSET,  0.0f + OUTLINE_OFFSET,  0.0f - OUTLINE_OFFSET,
            1.0f + OUTLINE_OFFSET, -1.0f - OUTLINE_OFFSET,  0.0f - OUTLINE_OFFSET,
            1.0f + OUTLINE_OFFSET,  0.0f + OUTLINE_OFFSET,  1.0f + OUTLINE_OFFSET,
            1.0f + OUTLINE_OFFSET, -1.0f - OUTLINE_OFFSET,  1.0f + OUTLINE_OFFSET,
            0.0f - OUTLINE_OFFSET,  0.0f + OUTLINE_OFFSET,  1.0f + OUTLINE_OFFSET,
            0.0f - OUTLINE_OFFSET, -1.0f - OUTLINE_OFFSET,  1.0f + OUTLINE_OFFSET,

            // Bottom edges
            0.0f - OUTLINE_OFFSET, -1.0f - OUTLINE_OFFSET,  0.0f - OUTLINE_OFFSET,
            0.0f - OUTLINE_OFFSET, -1.0f - OUTLINE_OFFSET,  1.0f + OUTLINE_OFFSET,
            0.0f - OUTLINE_OFFSET, -1.0f - OUTLINE_OFFSET,  1.0f + OUTLINE_OFFSET,
            1.0f + OUTLINE_OFFSET, -1.0f - OUTLINE_OFFSET,  1.0f + OUTLINE_OFFSET,
            1.0f + OUTLINE_OFFSET, -1.0f - OUTLINE_OFFSET,  1.0f + OUTLINE_OFFSET,
            1.0f + OUTLINE_OFFSET, -1.0f - OUTLINE_OFFSET,  0.0f - OUTLINE_OFFSET,
            1.0f + OUTLINE_OFFSET, -1.0f - OUTLINE_OFFSET,  0.0f - OUTLINE_OFFSET,
            0.0f - OUTLINE_OFFSET, -1.0f - OUTLINE_OFFSET,  0.0f - OUTLINE_OFFSET
    };
    public static final float[] VERTEX_COLOR_FACTORS = {
            // Top edges
            2.25f, 1.5f, 1.5f, 0.75f, 0.75f, 1.5f, 1.5f, 2.25f,
            // Side edges
            1.5f, 0.75f, 0.75f, 0, 1.5f, 0.75f, 2.25f, 1.5f,
            // Bottom edges
            0.75f, 1.5f, 1.5f, 0.75f, 0.75f, 0, 0, 0.75f
    };
    private static final int POS_SIZE = 3;
    private static final int COLOR_FACTOR_SIZE = 1;
    private static final int POS_OFFSET = 0;
    private static final int COLOR_FACTOR_OFFSET = POS_SIZE * Float.BYTES + POS_OFFSET;
    private static final int VERTEX_SIZE = POS_SIZE + COLOR_FACTOR_SIZE;
    private static final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;
    private static final float DEFAULT_PLAYER_REACH = 5.5f;

    private Camera cam;
    private World world;
    private float playerReach;
    private Shader shader;
    private int vaoID, vboID;
    private float[] vertices;

    @Override
    public void start(GameObject gameObject)
    {
        this.gameObject = gameObject;

        playerReach = DEFAULT_PLAYER_REACH;

        shader = AssetPool.getInstance().getShader(ShaderType.BLOCK_OUTLINE);

        // Generate and bind a VAO
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);
    }


    @Override
    public void update(double dt)
    {
        Vec3f rayStepSize = new Vec3f(
                (float)Math.sqrt(1 + Math.pow(cam.getForward().y / cam.getForward().x, 2) + Math.pow(cam.getForward().z / cam.getForward().x, 2)),
                (float)Math.sqrt(1 + Math.pow(cam.getForward().x / cam.getForward().y, 2) + Math.pow(cam.getForward().z / cam.getForward().y, 2)),
                (float)Math.sqrt(1 + Math.pow(cam.getForward().x / cam.getForward().z, 2) + Math.pow(cam.getForward().y / cam.getForward().z, 2))
        );
        // Coordinates of the block that the raycast is currently passing through
        Vec3i worldCoords = new Vec3i(
                cam.getPos().x >= 0 ? (int)cam.getPos().x : (int)cam.getPos().x - 1,
                cam.getPos().y >= 0 ? (int)cam.getPos().y + 1 : (int)cam.getPos().y,
                cam.getPos().z >= 0 ? (int)cam.getPos().z : (int)cam.getPos().z - 1
        );
        Vec3i previousPassCoords = new Vec3i();
        // Stores the length of each axis given their steps
        Vec3f rayLength1D = new Vec3f();
        Vec3i step = new Vec3i();

        // Calculate the starting lengths of each axis
        if(cam.getForward().x >= 0) {
            step.x = 1;
            rayLength1D.x = ((worldCoords.x + 1) - cam.getPos().x) * rayStepSize.x;
        }
        else {
            step.x = -1;
            rayLength1D.x = (cam.getPos().x - worldCoords.x) * rayStepSize.x;
        }

        if(cam.getForward().y >= 0) {
            step.y = 1;
            rayLength1D.y = (worldCoords.y - cam.getPos().y) * rayStepSize.y;
        }
        else {
            step.y = -1;
            rayLength1D.y = (cam.getPos().y - (worldCoords.y - 1)) * rayStepSize.y;
        }

        if(cam.getForward().z >= 0) {
            step.z = 1;
            rayLength1D.z = ((worldCoords.z + 1) - cam.getPos().z) * rayStepSize.z;
        }
        else {
            step.z = -1;
            rayLength1D.z = (cam.getPos().z - worldCoords.z) * rayStepSize.z;
        }

        previousPassCoords.copy(worldCoords);
        Byte hitBlock = world.getBlock(worldCoords.x, worldCoords.y, worldCoords.z);
        float distanceTraveled = 0;
        while((hitBlock == null || hitBlock == 0) && distanceTraveled <= playerReach) {
            previousPassCoords.copy(worldCoords);
            if(rayLength1D.x < rayLength1D.y && rayLength1D.x < rayLength1D.z) {
                worldCoords.x += step.x;
                distanceTraveled = rayLength1D.x;
                rayLength1D.x += rayStepSize.x;
            }
            else if (rayLength1D.y < rayLength1D.x && rayLength1D.y < rayLength1D.z) {
                worldCoords.y += step.y;
                distanceTraveled = rayLength1D.y;
                rayLength1D.y += rayStepSize.y;
            }
            else {
                worldCoords.z += step.z;
                distanceTraveled = rayLength1D.z;
                rayLength1D.z += rayStepSize.z;
            }

            hitBlock = world.getBlock(worldCoords.x, worldCoords.y, worldCoords.z);
        }

        if(hitBlock != null && hitBlock != 0) {
            if(MouseListener.mouseButtonDown(0))
                world.setBlock(worldCoords.x, worldCoords.y, worldCoords.z, (byte)0);
            else if(MouseListener.mouseButtonDown(1) && previousPassCoords != null && world.getBlock(previousPassCoords.x, previousPassCoords.y, previousPassCoords.z) == 0)
                world.setBlock(previousPassCoords.x, previousPassCoords.y, previousPassCoords.z, (byte)4);
            else
                renderBlockOutline(worldCoords);
        }
    }

    private void renderBlockOutline(Vec3i worldCoords)
    {
        vertices = new float[24 * VERTEX_SIZE];
        for(int i = 0; i < 24; i++) {
            vertices[i * VERTEX_SIZE] = CUBE_OUTLINE_VERTEX_POSITIONS[i * 3] + worldCoords.x;
            vertices[i * VERTEX_SIZE + 1] = CUBE_OUTLINE_VERTEX_POSITIONS[i * 3 + 1] + worldCoords.y;
            vertices[i * VERTEX_SIZE + 2] = CUBE_OUTLINE_VERTEX_POSITIONS[i * 3 + 2] + worldCoords.z;
            vertices[i * VERTEX_SIZE + 3] = VERTEX_COLOR_FACTORS[i];
        }

        // Allocate space for the vertices
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

        shader.use();
        shader.loadUniform("projection", SceneManager.getCurrentScene().getCamera().getProjectionMatrix());
        shader.loadUniform("view", SceneManager.getCurrentScene().getCamera().getViewMatrix());
        shader.loadUniform("sysTime", System.currentTimeMillis() % 10000000);

        glBindVertexArray(vaoID);

        glBindBuffer(GL_ARRAY_BUFFER, vboID);

        // Enable buffer attribute pointers
        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
        glVertexAttribPointer(1, COLOR_FACTOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_FACTOR_OFFSET);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glLineWidth(3f);

        glDrawArrays(GL_LINES, 0, vertices.length / VERTEX_SIZE);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
        shader.detach();
    }

    public void setCamera(Camera cam) { this.cam = cam; }

    public void setWorld(World world) { this.world = world; }
}
