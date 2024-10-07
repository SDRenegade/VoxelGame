package world;

import de.articdive.jnoise.core.api.functions.Interpolation;
import de.articdive.jnoise.generators.noise_parameters.fade_functions.FadeFunction;
import de.articdive.jnoise.pipeline.JNoise;
import util.FastNoiseLite;

import java.util.HashMap;
import java.util.Map;

public class World {
    private final int worldSeed;
    private final int terrainDensitySeed;
    private FastNoiseLite worldNoise2D;
    private FastNoiseLite worldNoise3D;
    private FastNoiseLite caveNoise;
    private Map<Long, Chunk> chunks;

    public World()
    {
        worldSeed = 121499;
        terrainDensitySeed = 122799;

        worldNoise2D = new FastNoiseLite(worldSeed);
        worldNoise2D.SetNoiseType(FastNoiseLite.NoiseType.Perlin);
        worldNoise2D.SetFrequency(1f/64f);
        worldNoise2D.SetFractalOctaves(4);

        worldNoise3D = new FastNoiseLite(terrainDensitySeed);
        worldNoise3D.SetNoiseType(FastNoiseLite.NoiseType.Perlin);
        worldNoise3D.SetFrequency(1f/24f);
        worldNoise3D.SetFractalOctaves(2);

        caveNoise = new FastNoiseLite(terrainDensitySeed);
        caveNoise.SetNoiseType(FastNoiseLite.NoiseType.Perlin);
        caveNoise.SetFrequency(1f/54f);
        caveNoise.SetFractalOctaves(2);

        chunks = new HashMap<>();
    }

    public void addChunk(Chunk chunk)
    {
        if(!chunks.containsKey(chunk.getChunkID()))
            chunks.put(chunk.getChunkID(), chunk);
    }

    public Chunk getChunk(int xPos, int zPos)
    {
        long chunkID = (((long)xPos) << 32) | (zPos & 0xffffffffL);
        if(!chunks.containsKey(chunkID))
            chunks.put(chunkID, new Chunk(xPos, zPos, this));

        return chunks.get(chunkID);
    }

    // Returns true if a chunk is found in the loaded chunk map based on the given chunk coordinates
    public boolean containsChunk(int xPos, int zPos)
    {
        long chunkID = (((long)xPos) << 32) | (zPos & 0xffffffffL);
        return chunks.containsKey(chunkID);
    }

    // Returns true if a chunk is found in the loaded chunk map based on the given world coordinates
    public boolean containsChunk(float worldX, float worldZ)
    {
        int chunkX = getChunkPos(worldX);
        int chunkZ = getChunkPos(worldZ);
        long chunkID = (((long)chunkX) << 32) | (chunkZ & 0xffffffffL);
        return chunks.containsKey(chunkID);
    }

    public float evaluateFastNoise(float x, float y)
    {
        return (worldNoise2D.GetNoise(x, y) + 1) / 2;
    }

    public float evaluateFast3DNoise(float x, float y, float z)
    {
        return (worldNoise3D.GetNoise(x, y, z) + 1) / 2;
    }

    public float evaluateCaveNoise(float x, float y, float z)
    {
        return (caveNoise.GetNoise(x / 1.5f, y, z / 1.5f) + 1) / 2;
    }

    // If the chunk that the block belongs to was never been loaded before, null is returned
    public Byte getBlock(int x, int y, int z)
    {
        int chunkX = getChunkPos(x);
        int chunkZ = getChunkPos(z);
        int xPosInChunk = x - (chunkX * ChunkData.CHUNK_SIZE);
        int zPosInChunk = z - (chunkZ * ChunkData.CHUNK_SIZE);

        return containsChunk(chunkX, chunkZ) ? getChunk(chunkX, chunkZ).getChunkData().getBlock(xPosInChunk, y, zPosInChunk) : null;
    }

    public void setBlock(Location location, byte block) {
        setBlock((int)location.getX(), (int)location.getY(), (int)location.getZ(), block);
    }

    // Given world coordinates, sets the given block type at the given location
    public void setBlock(int x, int y, int z, byte block)
    {
        int chunkX = getChunkPos(x);
        int chunkZ = getChunkPos(z);
        int xPosInChunk = x - (chunkX * ChunkData.CHUNK_SIZE);
        int zPosInChunk = z - (chunkZ * ChunkData.CHUNK_SIZE);

        if(containsChunk(chunkX, chunkZ)) {
            getChunk(chunkX, chunkZ).getChunkData().setBlock(xPosInChunk, y, zPosInChunk, block);
            getChunk(chunkX, chunkZ).setDirty(true);

            // If a block on the border of the chunk was updated, set neighboring chunk dirty
            if(xPosInChunk == ChunkData.CHUNK_SIZE - 1 && containsChunk(chunkX + 1, chunkZ))
                getChunk(chunkX + 1, chunkZ).setDirty(true);
            else if(xPosInChunk == 0 && containsChunk(chunkX - 1, chunkZ))
                getChunk(chunkX - 1, chunkZ).setDirty(true);

            if(zPosInChunk == ChunkData.CHUNK_SIZE - 1 && containsChunk(chunkX, chunkZ + 1))
                getChunk(chunkX, chunkZ + 1).setDirty(true);
            else if(zPosInChunk == 0 && containsChunk(chunkX, chunkZ - 1))
                getChunk(chunkX, chunkZ - 1).setDirty(true);

            if(xPosInChunk == ChunkData.CHUNK_SIZE - 1 && zPosInChunk == ChunkData.CHUNK_SIZE - 1 && containsChunk(chunkX + 1, chunkZ + 1))
                getChunk(chunkX + 1, chunkZ + 1).setDirty(true);
            else if(xPosInChunk == ChunkData.CHUNK_SIZE - 1 && zPosInChunk == 0 && containsChunk(chunkX + 1, chunkZ - 1))
                getChunk(chunkX + 1, chunkZ - 1).setDirty(true);
            else if(xPosInChunk == 0 && zPosInChunk == ChunkData.CHUNK_SIZE - 1 && containsChunk(chunkX - 1, chunkZ + 1))
                getChunk(chunkX - 1, chunkZ + 1).setDirty(true);
            else if(xPosInChunk == 0 && zPosInChunk == 0 && containsChunk(chunkX - 1, chunkZ - 1))
                getChunk(chunkX - 1, chunkZ - 1).setDirty(true);

        }
    }

    public int getChunkPos(float worldAxisPos)
    {
        return worldAxisPos >= 0 ? (int)(worldAxisPos / ChunkData.CHUNK_SIZE) : (int)((worldAxisPos + 1) / ChunkData.CHUNK_SIZE) - 1;
    }
}
