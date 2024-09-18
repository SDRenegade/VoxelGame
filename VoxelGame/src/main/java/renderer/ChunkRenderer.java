package renderer;

import blocks.Block;
import world.Chunk;
import world.ChunkData;
import world.ChunkSystem;
import world.World;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

public class ChunkRenderer {
    public static final int POS_SIZE = 3;
    public static final int TEX_COORDS_SIZE = 3;
    public static final int FACE_ID_SIZE = 1;
    public static final int AO_ID_SIZE = 1;
    public static final int POS_OFFSET = 0;
    public static final int TEX_COORDS_OFFSET = POS_SIZE * Float.BYTES + POS_OFFSET;
    public static final int FACE_ID_OFFSET = TEX_COORDS_SIZE * Float.BYTES + TEX_COORDS_OFFSET;
    public static final int AO_ID_OFFSET = FACE_ID_SIZE * Float.BYTES + FACE_ID_OFFSET;
    public static final int VERTEX_SIZE = POS_SIZE + TEX_COORDS_SIZE + FACE_ID_SIZE + AO_ID_SIZE;
    public static final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    private Chunk chunk;
    private World world;
    private float[] vertices;
    private int vboID, eboID;
    private int[] indices;
    private List<Float> vertexList;
    private boolean isInitialized;

    public ChunkRenderer(Chunk chunk, World world)
    {
        this.chunk = chunk;
        this.world = world;
        isInitialized = false;
        vertexList = new ArrayList<>();
    }

    public void updateChunkMesh(ChunkSystem chunkSystem)
    {
        for(int y = 0; y < ChunkData.CHUNK_HEIGHT; y++) {
            for(int z = 0; z < ChunkData.CHUNK_SIZE; z++) {
                for(int x = 0; x < ChunkData.CHUNK_SIZE; x++) {
                    if(chunk.getChunkData().getBlock(x, y, z) == 0)
                        continue;

                    // Front
                    if(z + 1 >= ChunkData.CHUNK_SIZE) {
                        if(chunkSystem.containsChunk(chunk.getChunkData().getXPos(), chunk.getChunkData().getZPos() + 1)) {
                            Chunk adjacentChunk = chunkSystem.getChunkFromRenderer(chunk.getChunkData().getXPos(), chunk.getChunkData().getZPos() + 1);
                            if(adjacentChunk.getChunkData().getBlock(x, y, 0) == 0)
                                addFaceToVertexArray(0, chunk, x, y, z);
                        }
                    }
                    else if(chunk.getChunkData().getBlock(x, y, z + 1) == 0)
                        addFaceToVertexArray(0, chunk, x, y, z);
                    // Back
                    if(z - 1 < 0) {
                        if(chunkSystem.containsChunk(chunk.getChunkData().getXPos(), chunk.getChunkData().getZPos() - 1)) {
                            Chunk adjacentChunk = chunkSystem.getChunkFromRenderer(chunk.getChunkData().getXPos(), chunk.getChunkData().getZPos() - 1);
                            if(adjacentChunk.getChunkData().getBlock(x, y, ChunkData.CHUNK_SIZE - 1) == 0)
                                addFaceToVertexArray(1, chunk, x, y, z);
                        }
                    }
                    else if(chunk.getChunkData().getBlock(x, y, z - 1) == 0)
                        addFaceToVertexArray(1, chunk, x, y, z);
                    // Left
                    if(x - 1 < 0) {
                        if(chunkSystem.containsChunk(chunk.getChunkData().getXPos() - 1, chunk.getChunkData().getZPos())) {
                            Chunk adjacentChunk = chunkSystem.getChunkFromRenderer(chunk.getChunkData().getXPos() - 1, chunk.getChunkData().getZPos());
                            if(adjacentChunk.getChunkData().getBlock(ChunkData.CHUNK_SIZE - 1, y, z) == 0)
                                addFaceToVertexArray(2, chunk, x, y, z);
                        }
                    }
                    else if(chunk.getChunkData().getBlock(x - 1, y, z) == 0)
                        addFaceToVertexArray(2, chunk, x, y, z);
                    // Right
                    if(x + 1 >= ChunkData.CHUNK_SIZE) {
                        if(chunkSystem.containsChunk(chunk.getChunkData().getXPos() + 1, chunk.getChunkData().getZPos())) {
                            Chunk adjacentChunk = chunkSystem.getChunkFromRenderer(chunk.getChunkData().getXPos() + 1, chunk.getChunkData().getZPos());
                            if(adjacentChunk.getChunkData().getBlock(0, y, z) == 0)
                                addFaceToVertexArray(3, chunk, x, y, z);
                        }
                    }
                    else if(chunk.getChunkData().getBlock(x + 1, y, z) == 0)
                        addFaceToVertexArray(3, chunk, x, y, z);
                    // Top
                    if(y + 1 >= ChunkData.CHUNK_HEIGHT || chunk.getChunkData().getBlock(x, y + 1, z) == 0)
                        addFaceToVertexArray(4, chunk, x, y, z);
                    // Bottom
                    if(y - 1 < 0 || chunk.getChunkData().getBlock(x, y - 1, z) == 0)
                        addFaceToVertexArray(5, chunk, x, y, z);
                }
            }
        }

        // Load vertices into the vertices array from the list;
        vertices = new float[vertexList.size()];
        for(int i = 0; i < vertexList.size(); i++)
            vertices[i] = vertexList.get(i);
        vertexList.clear();

        chunk.setDirty(false);

        start();
    }

    public void start()
    {
        // Allocate space for the vertices
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

        // Create and upload indices buffer
        indices = generateIndices();
        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        isInitialized = true;
    }

    public void render()
    {
        if(!isInitialized)
            return;

        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);

        // Enable buffer attribute pointers
        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
        glVertexAttribPointer(1, TEX_COORDS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_COORDS_OFFSET);
        glVertexAttribPointer(2, FACE_ID_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, FACE_ID_OFFSET);
        glVertexAttribPointer(3, AO_ID_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, AO_ID_OFFSET);

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        glEnableVertexAttribArray(3);

        glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glDisableVertexAttribArray(3);
    }

    private void addFaceToVertexArray(int faceIndex, Chunk chunk, int chunkX, int chunkY, int chunkZ)
    {
        int[] aoIDs = calculateAmbientOcclusionIDs(world, faceIndex, chunk, chunkX, chunkY, chunkZ);

        int faceOffset = faceIndex * 3 * 4;
        for(int i = 0; i < 4; i++) {
            vertexList.add((float)chunk.getChunkData().getXPos() * ChunkData.CHUNK_SIZE + chunkX + Block.CUBE_VERTICES[faceOffset + (i * 3)]);
            vertexList.add((float)chunkY + Block.CUBE_VERTICES[faceOffset + (i * 3) + 1]);
            vertexList.add((float)chunk.getChunkData().getZPos() * ChunkData.CHUNK_SIZE + chunkZ + Block.CUBE_VERTICES[faceOffset + (i * 3) + 2]);

            vertexList.add(Block.TEXTURE_COORDS_ARRAY[i * 2]);
            vertexList.add(Block.TEXTURE_COORDS_ARRAY[(i * 2) + 1]);
            vertexList.add((float)Block.blockMap.get(chunk.getChunkData().getBlock(chunkX, chunkY, chunkZ)).getFaceTextures()[faceIndex].getTextureIndex());

            vertexList.add((float)faceIndex);

            vertexList.add((float)aoIDs[i]);
        }

        aoIDs = null;
    }

    private int[] calculateAmbientOcclusionIDs(World world, int faceIndex, Chunk chunk, int x, int y, int z)
    {
        int[] aoIDs = new int[4];

        int worldX = chunk.getChunkData().getXPos() * ChunkData.CHUNK_SIZE + x;
        int worldZ = chunk.getChunkData().getZPos() * ChunkData.CHUNK_SIZE + z;
        byte a, b, c, d, e, f, g, h ;
        a = b = c = d = e = f = g = h = 0;
        Byte block;
        // Front
        if(faceIndex == 0) {
            if(y + 1 <= ChunkData.CHUNK_HEIGHT) {
                block = world.getBlock(worldX, y + 1, worldZ + 1);
                a = block != null && block != 0 ? (byte)1 : (byte)0;
            }

            if(y + 1 <= ChunkData.CHUNK_HEIGHT) {
                block = world.getBlock(worldX - 1, y + 1, worldZ + 1);
                b = block != null && block != 0 ? (byte)1 : (byte)0;
            }

            block = world.getBlock(worldX - 1, y, worldZ + 1);
            c = block != null && block != 0 ? (byte)1 : (byte)0;

            if(y - 1 >= 0) {
                block = world.getBlock(worldX - 1, y - 1, worldZ + 1);
                d = block != null && block != 0 ? (byte)1 : (byte)0;
            }

            if(y - 1 >= 0) {
                block = world.getBlock(worldX, y - 1, worldZ + 1);
                e = block != null && block != 0 ? (byte)1 : (byte)0;
            }

            if(y - 1 >= 0) {
                block = world.getBlock(worldX + 1, y - 1, worldZ + 1);
                f = block != null && block != 0 ? (byte)1 : (byte)0;
            }

            block = world.getBlock(worldX + 1, y, worldZ + 1);
            g = block != null && block != 0 ? (byte)1 : (byte)0;

            if(y + 1 <= ChunkData.CHUNK_HEIGHT) {
                block = world.getBlock(worldX + 1, y + 1, worldZ + 1);
                h = block != null && block != 0 ? (byte)1 : (byte)0;
            }
        }
        // Back
        else if(faceIndex == 1) {
            if(y + 1 <= ChunkData.CHUNK_HEIGHT) {
                block = world.getBlock(worldX, y + 1, worldZ - 1);
                a = block != null && block != 0 ? (byte)1 : (byte)0;
            }

            if(y + 1 <= ChunkData.CHUNK_HEIGHT) {
                block = world.getBlock(worldX + 1, y + 1, worldZ - 1);
                b = block != null && block != 0 ? (byte)1 : (byte)0;
            }

            block = world.getBlock(worldX + 1, y, worldZ - 1);
            c = block != null && block != 0 ? (byte)1 : (byte)0;

            if(y - 1 >= 0) {
                block = world.getBlock(worldX + 1, y - 1, worldZ - 1);
                d = block != null && block != 0 ? (byte)1 : (byte)0;
            }

            if(y - 1 >= 0) {
                block = world.getBlock(worldX, y - 1, worldZ - 1);
                e = block != null && block != 0 ? (byte)1 : (byte)0;
            }

            if(y - 1 >= 0) {
                block = world.getBlock(worldX - 1, y - 1, worldZ - 1);
                f = block != null && block != 0 ? (byte)1 : (byte)0;
            }

            block = world.getBlock(worldX - 1, y, worldZ - 1);
            g = block != null && block != 0 ? (byte)1 : (byte)0;

            if(y + 1 <= ChunkData.CHUNK_HEIGHT) {
                block = world.getBlock(worldX - 1, y + 1, worldZ - 1);
                h = block != null && block != 0 ? (byte)1 : (byte)0;
            }
        }
        // Left
        else if(faceIndex == 2) {
            if(y + 1 <= ChunkData.CHUNK_HEIGHT) {
                block = world.getBlock(worldX - 1, y + 1, worldZ);
                a = block != null && block != 0 ? (byte)1 : (byte)0;
            }

            if(y + 1 <= ChunkData.CHUNK_HEIGHT) {
                block = world.getBlock(worldX - 1, y + 1, worldZ - 1);
                b = block != null && block != 0 ? (byte)1 : (byte)0;
            }

            block = world.getBlock(worldX - 1, y, worldZ - 1);
            c = block != null && block != 0 ? (byte)1 : (byte)0;

            if(y - 1 >= 0) {
                block = world.getBlock(worldX - 1, y - 1, worldZ - 1);
                d = block != null && block != 0 ? (byte)1 : (byte)0;
            }

            if(y - 1 >= 0) {
                block = world.getBlock(worldX - 1, y - 1, worldZ);
                e = block != null && block != 0 ? (byte)1 : (byte)0;
            }

            if(y - 1 >= 0) {
                block = world.getBlock(worldX - 1, y - 1, worldZ + 1);
                f = block != null && block != 0 ? (byte)1 : (byte)0;
            }

            block = world.getBlock(worldX - 1, y, worldZ + 1);
            g = block != null && block != 0 ? (byte)1 : (byte)0;

            if(y + 1 <= ChunkData.CHUNK_HEIGHT) {
                block = world.getBlock(worldX - 1, y + 1, worldZ + 1);
                h = block != null && block != 0 ? (byte)1 : (byte)0;
            }
        }
        // Right
        else if(faceIndex == 3) {
            if(y + 1 <= ChunkData.CHUNK_HEIGHT) {
                block = world.getBlock(worldX + 1, y + 1, worldZ);
                a = block != null && block != 0 ? (byte)1 : (byte)0;
            }

            if(y + 1 <= ChunkData.CHUNK_HEIGHT) {
                block = world.getBlock(worldX + 1, y + 1, worldZ + 1);
                b = block != null && block != 0 ? (byte)1 : (byte)0;
            }

            block = world.getBlock(worldX + 1, y, worldZ + 1);
            c = block != null && block != 0 ? (byte)1 : (byte)0;

            if(y - 1 >= 0) {
                block = world.getBlock(worldX + 1, y - 1, worldZ + 1);
                d = block != null && block != 0 ? (byte)1 : (byte)0;
            }

            if(y - 1 >= 0) {
                block = world.getBlock(worldX + 1, y - 1, worldZ);
                e = block != null && block != 0 ? (byte)1 : (byte)0;
            }

            if(y - 1 >= 0) {
                block = world.getBlock(worldX + 1, y - 1, worldZ - 1);
                f = block != null && block != 0 ? (byte)1 : (byte)0;
            }

            block = world.getBlock(worldX + 1, y, worldZ - 1);
            g = block != null && block != 0 ? (byte)1 : (byte)0;

            if(y + 1 <= ChunkData.CHUNK_HEIGHT) {
                block = world.getBlock(worldX + 1, y + 1, worldZ - 1);
                h = block != null && block != 0 ? (byte)1 : (byte)0;
            }
        }
        // Top
        else if(faceIndex == 4 && y + 1 <= ChunkData.CHUNK_HEIGHT) {
            block = world.getBlock(worldX, y + 1, worldZ - 1);
            a = block != null && block != 0 ? (byte)1 : (byte)0;

            block = world.getBlock(worldX - 1, y + 1, worldZ - 1);
            b = block != null && block != 0 ? (byte)1 : (byte)0;

            block = world.getBlock(worldX - 1, y + 1, worldZ);
            c = block != null && block != 0 ? (byte)1 : (byte)0;

            block = world.getBlock(worldX - 1, y + 1, worldZ + 1);
            d = block != null && block != 0 ? (byte)1 : (byte)0;

            block = world.getBlock(worldX, y + 1, worldZ + 1);
            e = block != null && block != 0 ? (byte)1 : (byte)0;

            block = world.getBlock(worldX + 1, y + 1, worldZ + 1);
            f = block != null && block != 0 ? (byte)1 : (byte)0;

            block = world.getBlock(worldX + 1, y + 1, worldZ);
            g = block != null && block != 0 ? (byte)1 : (byte)0;

            block = world.getBlock(worldX + 1, y + 1, worldZ - 1);
            h = block != null && block != 0 ? (byte)1 : (byte)0;
        }
        // Bottom
        else if(faceIndex == 5 && y - 1 >= 0) {
            block = world.getBlock(worldX, y - 1, worldZ + 1);
            a = block != null && block != 0 ? (byte)1 : (byte)0;

            block = world.getBlock(worldX - 1, y - 1, worldZ + 1);
            b = block != null && block != 0 ? (byte)1 : (byte)0;

            block = world.getBlock(worldX - 1, y - 1, worldZ);
            c = block != null && block != 0 ? (byte)1 : (byte)0;

            block = world.getBlock(worldX - 1, y - 1, worldZ - 1);
            d = block != null && block != 0 ? (byte)1 : (byte)0;

            block = world.getBlock(worldX, y - 1, worldZ - 1);
            e = block != null && block != 0 ? (byte)1 : (byte)0;

            block = world.getBlock(worldX + 1, y - 1, worldZ - 1);
            f = block != null && block != 0 ? (byte)1 : (byte)0;

            block = world.getBlock(worldX + 1, y - 1, worldZ);
            g = block != null && block != 0 ? (byte)1 : (byte)0;

            block = world.getBlock(worldX + 1, y - 1, worldZ + 1);
            h = block != null && block != 0 ? (byte)1 : (byte)0;
        }

        aoIDs[0] = c + d + e;
        aoIDs[1] = a + b + c;
        aoIDs[2] = g + h + a;
        aoIDs[3] = e + f + g;

        return aoIDs;
    }

    private int[] generateIndices()
    {
        int indicesPerQuad = 6;
        int numQuads = vertices.length / indicesPerQuad / 4;
        int[] elements = new int[numQuads * indicesPerQuad];
        for(int i = 0; i < numQuads; i++)
            loadElementIndices(elements, i);

        return elements;
    }

    private void loadElementIndices(int[] elements, int index)
    {
        int offsetArrayIndex = index * 6;
        int offset = index * 4;

        // Quad indices: 3, 2, 0, 0, 2, 1       Next Quad: 7, 6, 4, 4, 6, 5
        // Triangle 1
        elements[offsetArrayIndex] = offset + 3;
        elements[offsetArrayIndex + 1] = offset + 2;
        elements[offsetArrayIndex + 2] = offset;

        // Triangle 2
        elements[offsetArrayIndex + 3] = offset;
        elements[offsetArrayIndex + 4] = offset + 2;
        elements[offsetArrayIndex + 5] = offset + 1;
    }

    public void dispose()
    {
        glDeleteBuffers(vboID);
        glDeleteBuffers(eboID);
    }

    public Chunk getChunk() { return chunk; }
}
