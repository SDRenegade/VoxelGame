package blocks;

import renderer.BlockTextures;

import java.util.HashMap;
import java.util.Map;

public class Block {
    public static final float[] CUBE_VERTICES = {
            // Front face
             0.0f, -1.0f,  1.0f, // Bottom left vert
             0.0f,  0.0f,  1.0f, // Top left vert
             1.0f,  0.0f,  1.0f, // Top right vert
             1.0f, -1.0f,  1.0f, // Bottom right vert

            // Back face
             1.0f, -1.0f,  0.0f,
             1.0f,  0.0f,  0.0f,
             0.0f,  0.0f,  0.0f,
             0.0f, -1.0f,  0.0f,

            // Left face
             0.0f, -1.0f,  0.0f,
             0.0f,  0.0f,  0.0f,
             0.0f,  0.0f,  1.0f,
             0.0f, -1.0f,  1.0f,

            // Right face
             1.0f, -1.0f,  1.0f,
             1.0f,  0.0f,  1.0f,
             1.0f,  0.0f,  0.0f,
             1.0f, -1.0f,  0.0f,

            // Top face
             0.0f,  0.0f,  1.0f,
             0.0f,  0.0f,  0.0f,
             1.0f,  0.0f,  0.0f,
             1.0f,  0.0f,  1.0f,

            // Bottom face
             0.0f, -1.0f,  0.0f,
             0.0f, -1.0f,  1.0f,
             1.0f, -1.0f,  1.0f,
             1.0f, -1.0f,  0.0f
    };

    public static final float[] TEXTURE_COORDS_ARRAY = {
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,
    };

    public static final Block GRASS_BLOCK = new Block((byte)1, new BlockBuilder().setSideFaces(BlockTextures.GRASS_BLOCK_SIDE).setGivenFace(BlockFaces.TOP, BlockTextures.GRASS_BLOCK_TOP).setGivenFace(BlockFaces.BOTTOM, BlockTextures.DIRT));
    public static final Block DIRT = new Block((byte)2, new BlockBuilder().setAllFaces(BlockTextures.DIRT));
    public static final Block STONE = new Block((byte)3, new BlockBuilder().setAllFaces(BlockTextures.STONE));
    public static final Block COBBLESTONE = new Block((byte)4, new BlockBuilder().setAllFaces(BlockTextures.COBBLESTONE));

    public static Map<Byte, Block> blockMap;

    private byte blockID;
    protected BlockTextures[] faceTextures;

    private Block(byte blockID, BlockBuilder blockBuilder)
    {
        this.blockID = blockID;
        faceTextures = blockBuilder.faceTextures;
    }

    public static void InitBlockMap()
    {
        blockMap = new HashMap<>();
        blockMap.put(GRASS_BLOCK.blockID, GRASS_BLOCK);
        blockMap.put(DIRT.blockID, DIRT);
        blockMap.put(STONE.blockID, STONE);
        blockMap.put(COBBLESTONE.blockID, COBBLESTONE);
    }

    public byte getBlockID() {
        return blockID;
    }

    public BlockTextures[] getFaceTextures() {
        return faceTextures;
    }

    public static class BlockBuilder
    {
        protected BlockTextures[] faceTextures;

        public BlockBuilder()
        {
            faceTextures = new BlockTextures[6];
        }

        public BlockBuilder setAllFaces(BlockTextures blockTexture)
        {
            for(int i = 0; i < faceTextures.length; i++)
                faceTextures[i] = blockTexture;

            return this;
        }

        public BlockBuilder setSideFaces(BlockTextures blockTexture)
        {
            for(int i = 0; i < 4; i++)
                faceTextures[i] = blockTexture;

            return this;
        }

        public BlockBuilder setGivenFace(BlockFaces face, BlockTextures blockTexture)
        {
            faceTextures[face.getFaceIndex()] = blockTexture;
            return this;
        }
    }
}
