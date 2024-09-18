package world;

import org.joml.Vector3f;
import renderer.ChunkRenderer;
import renderer.Shader;
import scenes.SceneManager;
import util.AssetPool;
import util.ShaderType;

import java.util.*;

import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class ChunkSystem {
    private static int RENDER_DISTANCE = 12;
    private static int MAX_CHUNK_MESH_UPDATES_PER_FRAME = 2;

    private Vector3f camPos;
    private World world;
    private Map<Long, ChunkRenderer> chunkRenderers;
    private Shader shader;
    private int vaoID;

    public ChunkSystem(Vector3f camPos, World world)
    {
        chunkRenderers = new HashMap<>();
        this.camPos = camPos;
        this.world = world;

        shader = AssetPool.getInstance().getShader(ShaderType.DEFAULT);

        // Generate and bind a VAO
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);
    }

    public void update(double dt)
    {
        int currentChunkX = world.getChunkPos(camPos.x);
        int currentChunkZ = world.getChunkPos(camPos.z);
        // Remove any chunks no longer in render distance
        Iterator<Map.Entry<Long, ChunkRenderer>> it = chunkRenderers.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry<Long, ChunkRenderer> renderer = it.next();
            if(playerDistanceToChunk(renderer.getValue().getChunk()) > RENDER_DISTANCE * ChunkData.CHUNK_SIZE) {
                setNeighboringChunkRenderersDirty(renderer.getValue().getChunk());
                renderer.getValue().dispose();
                it.remove();
            }
        }

        // Load any new chunks that have entered render distance
        for(int z = 0; z <= RENDER_DISTANCE * 2 + 3; z++) {
            for(int x = 0; x <= RENDER_DISTANCE * 2 + 3; x++) {
                if(chunkRenderers.containsKey(Chunk.generateChunkID(currentChunkX - RENDER_DISTANCE - 1 + x, currentChunkZ - RENDER_DISTANCE - 1 + z)))
                    continue;

                Chunk chunk = world.getChunk(currentChunkX - RENDER_DISTANCE - 1 + x, currentChunkZ - RENDER_DISTANCE - 1 + z);
                if(playerDistanceToChunk(chunk) <= RENDER_DISTANCE * ChunkData.CHUNK_SIZE)
                    addChunkRenderer(chunk);
            }
        }

        updateChunkMeshes();

        double beginTime = glfwGetTime();
        render();
        double endTime = glfwGetTime();
        //System.out.println("Render fps: " + (1 / (endTime - beginTime)));
    }

    public void addChunkRenderer(Chunk chunk)
    {
        if(chunkRenderers.containsKey(chunk.getChunkID()))
            return;

        ChunkRenderer chunkRenderer = new ChunkRenderer(chunk, world);
        chunkRenderers.put(chunk.getChunkID(), chunkRenderer);

        setNeighboringChunkRenderersDirty(chunk);
    }

    public void updateChunkMeshes()
    {
        List<ChunkRenderer> nearestDirtyRenderers = new ArrayList<>();
        for (Map.Entry<Long, ChunkRenderer> rendererEntry : chunkRenderers.entrySet()) {
            if (!rendererEntry.getValue().getChunk().isDirty())
                continue;

            if (nearestDirtyRenderers.size() < MAX_CHUNK_MESH_UPDATES_PER_FRAME)
                nearestDirtyRenderers.add(rendererEntry.getValue());
            else {
                for (int i = 0; i < nearestDirtyRenderers.size(); i++) {
                    if (playerDistanceToChunk(rendererEntry.getValue().getChunk()) < playerDistanceToChunk(nearestDirtyRenderers.get(i).getChunk())) {
                        nearestDirtyRenderers.set(i, rendererEntry.getValue());
                        break;
                    }
                }
            }
        }

        int chunkMeshUpdatesThisFrame = 0;
        for (int i = 0; i < nearestDirtyRenderers.size(); i++) {
            nearestDirtyRenderers.get(i).updateChunkMesh(this);
            chunkMeshUpdatesThisFrame++;
        }
        //System.out.println("Chunk Mesh Updates this frame: " + chunkMeshUpdatesThisFrame);
    }

    public Chunk getChunkFromRenderer(int xPos, int zPos)
    {
        ChunkRenderer chunkRenderer = chunkRenderers.get(Chunk.generateChunkID(xPos, zPos));
        return chunkRenderer != null ? chunkRenderer.getChunk() : null;
    }

    public boolean containsChunk(int xPos, int zPos)
    {
        return chunkRenderers.containsKey(Chunk.generateChunkID(xPos, zPos));
    }

    public void render()
    {
        shader.use();
        shader.loadUniform("textureArray", 0);
        shader.loadUniform("projection", SceneManager.getCurrentScene().getCamera().getProjectionMatrix());
        shader.loadUniform("view", SceneManager.getCurrentScene().getCamera().getViewMatrix());

        glBindVertexArray(vaoID);

        int drawCallsPerFrame = 0;
        for(Map.Entry<Long, ChunkRenderer> renderer : chunkRenderers.entrySet()) {
            renderer.getValue().render();
            drawCallsPerFrame++;
        }

        //System.out.println("Draw Calls this frame: " + drawCallsPerFrame);

        glBindVertexArray(0);

        shader.detach();
    }

    private float playerDistanceToChunk(Chunk chunk)
    {
        float xDis = 0;
        float zDis = 0;
        if(camPos.x > chunk.getChunkData().getXPos() * ChunkData.CHUNK_SIZE + ChunkData.CHUNK_SIZE)
            xDis = camPos.x - chunk.getChunkData().getXPos() * ChunkData.CHUNK_SIZE - ChunkData.CHUNK_SIZE;
        else if(camPos.x < chunk.getChunkData().getXPos() * ChunkData.CHUNK_SIZE)
            xDis = chunk.getChunkData().getXPos() * ChunkData.CHUNK_SIZE - camPos.x;

        if(camPos.z > chunk.getChunkData().getZPos() * ChunkData.CHUNK_SIZE + ChunkData.CHUNK_SIZE)
            zDis = camPos.z - chunk.getChunkData().getZPos() * ChunkData.CHUNK_SIZE - ChunkData.CHUNK_SIZE;
        else if(camPos.z < chunk.getChunkData().getZPos() * ChunkData.CHUNK_SIZE)
            zDis = chunk.getChunkData().getZPos() * ChunkData.CHUNK_SIZE - camPos.z;

        return (float)Math.sqrt(Math.pow(xDis, 2) + Math.pow(zDis, 2));
    }

    private void setNeighboringChunkRenderersDirty(Chunk chunk)
    {
        // +X Neighbor
        if(containsChunk(chunk.getChunkData().getXPos() + 1, chunk.getChunkData().getZPos()))
            chunkRenderers.get(Chunk.generateChunkID(chunk.getChunkData().getXPos() + 1, chunk.getChunkData().getZPos())).getChunk().setDirty(true);
        // -X Neighbor
        if(containsChunk(chunk.getChunkData().getXPos() - 1, chunk.getChunkData().getZPos()))
            chunkRenderers.get(Chunk.generateChunkID(chunk.getChunkData().getXPos() - 1, chunk.getChunkData().getZPos())).getChunk().setDirty(true);
        // +Z Neighbor
        if(containsChunk(chunk.getChunkData().getXPos(), chunk.getChunkData().getZPos() + 1))
            chunkRenderers.get(Chunk.generateChunkID(chunk.getChunkData().getXPos(), chunk.getChunkData().getZPos() + 1)).getChunk().setDirty(true);
        // -Z Neighbor
        if(containsChunk(chunk.getChunkData().getXPos(), chunk.getChunkData().getZPos() - 1))
            chunkRenderers.get(Chunk.generateChunkID(chunk.getChunkData().getXPos(), chunk.getChunkData().getZPos() - 1)).getChunk().setDirty(true);
    }

}
