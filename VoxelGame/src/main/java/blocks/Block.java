package blocks;

import renderer.BlockTextures;

import java.util.HashMap;
import java.util.Map;

public class Block {
    // Used for rendering individual blocks
    public static final float[] VERTEX_ARRAY = {
            // Position           // Tex Coords with z index
            // Front face
             0.0f, -1.0f,  1.0f,    0.0f, 0.0f,    1f,
             0.0f,  0.0f,  1.0f,    0.0f, 1.0f,    1f,
             1.0f,  0.0f,  1.0f,    1.0f, 1.0f,    1f,
             1.0f, -1.0f,  1.0f,    1.0f, 0.0f,    1f,

            // Back face
             1.0f, -1.0f,  0.0f,    0.0f, 0.0f,    1f,
             1.0f,  0.0f,  0.0f,    0.0f, 1.0f,    1f,
             0.0f,  0.0f,  0.0f,    1.0f, 1.0f,    1f,
             0.0f, -1.0f,  0.0f,    1.0f, 0.0f,    1f,

            // Left face
             0.0f, -1.0f,  0.0f,    0.0f, 0.0f,    1f,
             0.0f,  0.0f,  0.0f,    0.0f, 1.0f,    1f,
             0.0f,  0.0f,  1.0f,    1.0f, 1.0f,    1f,
             0.0f, -1.0f,  1.0f,    1.0f, 0.0f,    1f,

            // Right face
             1.0f, -1.0f,  1.0f,    0.0f, 0.0f,    1f,
             1.0f,  0.0f,  1.0f,    0.0f, 1.0f,    1f,
             1.0f,  0.0f,  0.0f,    1.0f, 1.0f,    1f,
             1.0f, -1.0f,  0.0f,    1.0f, 0.0f,    1f,

            // Top face
             1.0f,  0.0f,  0.0f,    0.0f, 0.0f,    0f,
             1.0f,  0.0f,  1.0f,    0.0f, 1.0f,    0f,
             0.0f,  0.0f,  1.0f,    1.0f, 1.0f,    0f,
             0.0f,  0.0f,  0.0f,    1.0f, 0.0f,    0f,

            // Bottom face
             1.0f, -1.0f,  1.0f,    0.0f, 0.0f,    2f,
             1.0f, -1.0f,  0.0f,    0.0f, 1.0f,    2f,
             0.0f, -1.0f,  0.0f,    1.0f, 1.0f,    2f,
             0.0f, -1.0f,  1.0f,    1.0f, 0.0f,    2f,
    };

    public static float[] POSITION_ARRAY = {
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

    public static float[] TEXTURE_COORDS_ARRAY = {
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,
    };

    public static Map<Byte, Block> blockMap;

    protected BlockTextures[] faceTextures;

    public Block(BlockBuilder blockBuilder)
    {
        faceTextures = blockBuilder.faceTextures;
    }

    public static void InitBlockMap()
    {
        blockMap = new HashMap<>();
        blockMap.put((byte)1, new Block(new BlockBuilder().setSideFaces(BlockTextures.GRASS_BLOCK_SIDE).setGivenFace(BlockFaces.TOP, BlockTextures.GRASS_BLOCK_TOP).setGivenFace(BlockFaces.BOTTOM, BlockTextures.DIRT)));
        blockMap.put((byte)2, new Block(new BlockBuilder().setAllFaces(BlockTextures.DIRT)));
        blockMap.put((byte)3, new Block(new BlockBuilder().setAllFaces(BlockTextures.STONE)));
        blockMap.put((byte)4, new Block(new BlockBuilder().setAllFaces(BlockTextures.COBBLESTONE)));
    }

    public BlockTextures[] getFaceTextures() { return faceTextures; }

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
