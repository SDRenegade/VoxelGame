package renderer;

import components.QuadRenderer;
import scenes.SceneManager;
import util.AssetPool;
import util.ShaderType;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class RenderBatch {
    public static final int POS_SIZE = 3;
    public static final int TEX_COORDS_SIZE = 3;
    public static final int POS_OFFSET = 0;
    public static final int TEX_COORDS_OFFSET = POS_SIZE * Float.BYTES + POS_OFFSET;
    public static final int VERTEX_SIZE = POS_SIZE + TEX_COORDS_SIZE;
    public static final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    private QuadRenderer[] quadRenderers;
    private int numQuadRenderers;
    private boolean isFull;
    private float[] vertices;
    private int vboID, vaoID;
    private int maxBatchSize;
    private Shader shader;

    private static int batches = 0;

    public RenderBatch(int maxBatchSize)
    {
        batches++;
        System.out.println("Batch #" + batches);
        this.maxBatchSize = maxBatchSize;
        quadRenderers = new QuadRenderer[maxBatchSize];

        vertices = new float[maxBatchSize * 4 * VERTEX_SIZE];

        numQuadRenderers = 0;
        isFull = false;

        shader = AssetPool.getInstance().getShader(ShaderType.DEFAULT);
    }

    public void start()
    {
        // Generate and bind a VAO
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Allocate space for the vertices
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);

        // Create and upload indices buffer
        int eboID = glGenBuffers();
        int[] indices = generateIndices();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        // Enable buffer attribute pointers
        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
        glVertexAttribPointer(1, TEX_COORDS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_COORDS_OFFSET);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        shader.use();
    }

    public void addQuadToBatch(QuadRenderer quadRenderer, int quadIndex)
    {
        // Add new Renderer
        int index = numQuadRenderers;

        quadRenderers[index] = quadRenderer;
        numQuadRenderers++;

        // Add properties to local vertex array
        int offset = index * 4 * VERTEX_SIZE;
        int quadRendererOffset = quadIndex * 4 * VERTEX_SIZE;
        for(int i = 0; i < 4; i++) {
            // Add position values
            vertices[offset + (VERTEX_SIZE * i)] = (quadRenderer.getVertexArray()[quadRendererOffset + (VERTEX_SIZE * i)] + quadRenderer.getGameObject().getTransform().getPosition().x) * quadRenderer.getGameObject().getTransform().getScale().x;
            vertices[offset + (VERTEX_SIZE * i) + 1] = (quadRenderer.getVertexArray()[quadRendererOffset + (VERTEX_SIZE * i) + 1] + quadRenderer.getGameObject().getTransform().getPosition().y) * quadRenderer.getGameObject().getTransform().getScale().y;
            vertices[offset + (VERTEX_SIZE * i) + 2] = (quadRenderer.getVertexArray()[quadRendererOffset + (VERTEX_SIZE * i) + 2] + quadRenderer.getGameObject().getTransform().getPosition().z) * quadRenderer.getGameObject().getTransform().getScale().z;

            // Add texture coordinate values
            vertices[offset + (VERTEX_SIZE * i) + 3] = quadRenderer.getVertexArray()[quadRendererOffset + (VERTEX_SIZE * i) + 3];
            vertices[offset + (VERTEX_SIZE * i) + 4] = quadRenderer.getVertexArray()[quadRendererOffset + (VERTEX_SIZE * i) + 4];

            // Add texture index value
            vertices[offset + (VERTEX_SIZE * i) + 5] = quadRenderer.getVertexArray()[quadRendererOffset + (VERTEX_SIZE * i) + 5];
        }

        if(numQuadRenderers >= maxBatchSize)
            isFull = true;
    }

    public void render()
    {
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

        shader.use();

        shader.loadUniform("texture_array", 0);
        shader.loadUniform("projection", SceneManager.getCurrentScene().getCamera().getProjectionMatrix());
        shader.loadUniform("view", SceneManager.getCurrentScene().getCamera().getViewMatrix());

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, numQuadRenderers * 6, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        shader.detach();
    }

    private int[] generateIndices()
    {
        // 6 indices per quad
        int[] elements = new int[maxBatchSize * 6];
        for(int i = 0; i < maxBatchSize; i++)
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

    public boolean isFull() { return isFull; }
}
