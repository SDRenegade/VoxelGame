package renderer;

import components.Component;
import scenes.SceneManager;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class EntityRenderer extends Component {
    public static final int POS_SIZE = 3;
    public static final int TEX_COORDS_SIZE = 3;
    public static final int POS_OFFSET = 0;
    public static final int TEX_COORDS_OFFSET = POS_SIZE * Float.BYTES + POS_OFFSET;
    public static final int VERTEX_SIZE = POS_SIZE + TEX_COORDS_SIZE;
    public static final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    private Shader shader;
    private int vaoID, vboID, eboID;
    private float[] vertices;
    private int[] indices;

    public EntityRenderer(Shader shader) {
        this.shader = shader;
    }

    @Override
    public void start() {
        // Generate buffers
        vaoID = glGenVertexArrays();
        vboID = glGenBuffers();
        eboID = glGenBuffers();
    }

    @Override
    public void update(double dt) {

    }

    public void render() {
        shader.use();
        shader.loadUniform("textureArray", 1); // Change to proper textureArray index
        shader.loadUniform("view", SceneManager.getCurrentScene().getCamera().getViewMatrix());
        shader.loadUniform("projection", SceneManager.getCurrentScene().getCamera().getProjectionMatrix());
        glBindVertexArray(vaoID);

        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);

        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
        glVertexAttribPointer(1, TEX_COORDS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_COORDS_OFFSET);

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        //glDrawElements(GL_TRIANGLES, QUAD_ELEMENT_INDICES.length, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);

        glBindVertexArray(0);
        shader.detach();
    }
}
