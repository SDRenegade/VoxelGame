package renderer;

import components.Component;
import window.Window;
import org.joml.Vector2f;
import util.AssetPool;
import util.MatrixMath;
import util.ShaderType;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class UiRenderer extends Component {
    public static final float[] VERTEX_POSITIONS = {
            -1.0f, -1.0f,
            -1.0f,  1.0f,
             1.0f,  1.0f,
             1.0f, -1.0f
    };
    public static final int[] QUAD_ELEMENT_INDICES = {
            3, 2, 0, 0, 2, 1
    };
    public static float[] TEXTURE_COORDS_ARRAY = {
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,
    };

    private static final int POS_SIZE = 2;
    private static final int TEX_COORDS_SIZE = 3;
    public static final int POS_OFFSET = 0;
    public static final int TEX_COORDS_OFFSET = POS_SIZE * Float.BYTES + POS_OFFSET;
    private static final int VERTEX_SIZE = POS_SIZE + TEX_COORDS_SIZE;
    private static final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    private Shader shader;
    private int vaoID, vboID, eboID;
    float[] vertices;

    @Override
    public void start()
    {
        shader = AssetPool.getInstance().getShader(ShaderType.UI);

        vertices = new float[4 * VERTEX_SIZE];
        for(int i = 0; i < 4; i++) {
            vertices[i * VERTEX_SIZE] = VERTEX_POSITIONS[i * 2];
            vertices[i * VERTEX_SIZE + 1] = VERTEX_POSITIONS[i * 2 + 1];

            vertices[i * VERTEX_SIZE + 2] = TEXTURE_COORDS_ARRAY[i * 2];
            vertices[i * VERTEX_SIZE + 3] = TEXTURE_COORDS_ARRAY[i * 2 + 1];
            vertices[i * VERTEX_SIZE + 4] = 0;
        }

        // Generate and bind a VAO
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Allocate space for the vertices
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

        // Create and upload indices buffer
        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, QUAD_ELEMENT_INDICES, GL_STATIC_DRAW);
    }

    @Override
    public void update(double dt) {}

    public void renderCrosshairs()
    {
        shader.use();;
        shader.loadUniform("textureArray", 1);
        shader.loadUniform("transformationMatrix", MatrixMath.createTransformationMatrixUI(new Vector2f(0f, 0f), new Vector2f(0.012f, 0.012f * Window.getAspectRatio())));
        glBindVertexArray(vaoID);

        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);

        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
        glVertexAttribPointer(1, TEX_COORDS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_COORDS_OFFSET);

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDisable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glDrawElements(GL_TRIANGLES, QUAD_ELEMENT_INDICES.length, GL_UNSIGNED_INT, 0);

        glDisable(GL_BLEND);
        glEnable(GL_DEPTH_TEST);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);
        shader.detach();
    }

}
