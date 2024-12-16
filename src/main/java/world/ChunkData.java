package world;

import blocks.Block;

public class ChunkData {
    public static final int CHUNK_SIZE = 16;
    public static final int CHUNK_HEIGHT = 256;

    private byte[] blocks;
    private int[] heightMap;
    private int xPos, zPos;

    public ChunkData(int xPos, int zPos, World world) {
        blocks = new byte[CHUNK_SIZE * CHUNK_SIZE * CHUNK_HEIGHT];
        heightMap = new int[CHUNK_SIZE * CHUNK_SIZE];
        this.xPos = xPos;
        this.zPos = zPos;

        int minBlockHeight = 120;
        int maxBlockHeight = 185;

        // Create heightmap and fill block array with stone based on heightmap
        for(int z = 0; z < CHUNK_SIZE; z++) {
            for(int x = 0; x < CHUNK_SIZE; x++) {
                float height = world.evaluateFastNoise(xPos * CHUNK_SIZE + x, zPos * CHUNK_SIZE + z);
                height = height * (maxBlockHeight - minBlockHeight) + minBlockHeight;
                int highestBlockPos = 0;
                for(int y = 0; y < (int)height; y++) {
                    if(y >= 135 && noiseEvaluationWorld3D(x, y, z, world) > 0.5)
                        continue;

                    blocks[x + (z * CHUNK_SIZE) + (y * CHUNK_SIZE * CHUNK_SIZE)] = Block.COBBLESTONE.getBlockID();
                    highestBlockPos = y;
                }

                heightMap[x + (z * CHUNK_SIZE)] = highestBlockPos;
            }
        }

        // Add surface blocks
        for(int z = 0; z < CHUNK_SIZE; z++) {
            for(int x = 0; x < CHUNK_SIZE; x++) {
                int highestBlockPos = heightMap[x + (z * CHUNK_SIZE)];
                for(int y = highestBlockPos; y >= 0; y--) {
                    if(blocks[x + (z * CHUNK_SIZE) + (y * CHUNK_SIZE * CHUNK_SIZE)] == Block.COBBLESTONE.getBlockID() &&
                            blocks[x + (z * CHUNK_SIZE) + ((y + 1) * CHUNK_SIZE * CHUNK_SIZE)] == (byte)0) {
                        blocks[x + (z * CHUNK_SIZE) + (y * CHUNK_SIZE * CHUNK_SIZE)] = Block.GRASS_BLOCK.getBlockID();

                        // Add dirt blocks below grass block
                        for(int i = 1; i <= 2; i++) {
                            if(blocks[x + (z * CHUNK_SIZE) + ((y - i) * CHUNK_SIZE * CHUNK_SIZE)] == Block.COBBLESTONE.getBlockID())
                                blocks[x + (z * CHUNK_SIZE) + ((y - i) * CHUNK_SIZE * CHUNK_SIZE)] = Block.DIRT.getBlockID();
                            else
                                break;
                        }
                    }
                }
            }
        }

        // Create caves
        /*for(int z = 0; z < CHUNK_SIZE; z++) {
            for(int x = 0; x < CHUNK_SIZE; x++) {
                for(int y = 135; y >= 0; y--) {
                    if(noiseEvaluationCave(x, y, z, world) > 0.68)
                        blocks[x + (z * CHUNK_SIZE) + (y * CHUNK_SIZE * CHUNK_SIZE)] = (byte)0;
                }
            }
        }*/

    }

    private float noiseEvaluationWorld3D(int x, int y, int z, World world) {
        float minVal = 0.6f;
        float maxVal = 1.25f;
        float multiplier = (float)(y - 135) / (185 - 135) * (maxVal - minVal) + minVal;
        return world.evaluateFast3DNoise(xPos * CHUNK_SIZE + x, y, zPos * CHUNK_SIZE + z) * multiplier;
    }

    private float noiseEvaluationCave(int x, int y, int z, World world) {
        float minVal = 0.7f;
        float maxVal = 1f;
        float multiplier = (float)(135 - y) / 50 * (maxVal - minVal) + minVal;
        multiplier = multiplier <= 1 ? multiplier : 1f;
        return world.evaluateCaveNoise(xPos * CHUNK_SIZE + x, y, zPos * CHUNK_SIZE + z) * multiplier;
    }

    public Byte getBlock(int x, int y, int z) {
        if(x < 0 || y < 0 || z < 0 || x >= CHUNK_SIZE || y >= CHUNK_HEIGHT || z >= CHUNK_SIZE)
            return null;

        return blocks[x + (z * CHUNK_SIZE) + (y * CHUNK_SIZE * CHUNK_SIZE)];
    }

    public void setBlock(int x, int y, int z, byte block) {
        if(x < 0 || y < 0 || z < 0 || x >= CHUNK_SIZE || y >= CHUNK_HEIGHT || z >= CHUNK_SIZE)
            return;

        blocks[x + (z * CHUNK_SIZE) + (y * CHUNK_SIZE * CHUNK_SIZE)] = block;
    }

    public int getXPos() {
        return xPos;
    }

    public int getZPos() {
        return zPos;
    }
}
