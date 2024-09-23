package renderer;

import components.Component;
import components.RectTransform;
import util.*;
import window.GameObject;
import window.Window;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class UiRenderer extends Component {
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
    private static final int COLOR_SIZE = 4;
    public static final int POS_OFFSET = 0;
    public static final int TEX_COORDS_OFFSET = POS_SIZE * Float.BYTES + POS_OFFSET;
    public static final int COLOR_OFFSET = TEX_COORDS_SIZE * Float.BYTES + TEX_COORDS_OFFSET;
    private static final int VERTEX_SIZE = POS_SIZE + TEX_COORDS_SIZE + COLOR_SIZE;
    private static final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    private Shader shader;
    private int vaoID, vboID, eboID;
    private float[] vertices;
    private int textureIndex;
    private Color32 color;

    public UiRenderer(Shader shader, int textureIndex, Color32 color)
    {
        this.shader = shader;
        this.textureIndex = textureIndex;
        this.color = color;
    }

    @Override
    public void start(GameObject gameObject)
    {
        this.gameObject = gameObject;

        // Generate and bind a VAO
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        updateVertexArray();

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

    private void updateVertexArray()
    {
        RectTransform rectTransform = gameObject.getComponent(RectTransform.class);
        vertices = new float[4 * VERTEX_SIZE];
        for(int i = 0; i < 4; i++) {
            float xPos = i <= 1 ? -rectTransform.getDimensions().x / 2 : rectTransform.getDimensions().x / 2;
            float yPos = i % 3 == 0 ? -rectTransform.getDimensions().y / 2 : rectTransform.getDimensions().y / 2;
            vertices[i * VERTEX_SIZE] = (xPos / Window.DEFAULT_WINDOW_WIDTH * ((float)Window.DEFAULT_WINDOW_WIDTH / (float)Window.getInstance().getWidth()) + rectTransform.getAnchor().x) * 2 - 1;
            vertices[i * VERTEX_SIZE + 1] = (yPos / Window.DEFAULT_WINDOW_HEIGHT * ((float)Window.DEFAULT_WINDOW_HEIGHT / (float)Window.getInstance().getHeight()) + rectTransform.getAnchor().y) * 2 - 1;

            vertices[i * VERTEX_SIZE + 2] = TEXTURE_COORDS_ARRAY[i * 2];
            vertices[i * VERTEX_SIZE + 3] = TEXTURE_COORDS_ARRAY[i * 2 + 1];
            vertices[i * VERTEX_SIZE + 4] = (float)textureIndex;

            vertices[i * VERTEX_SIZE + 5] = color.getRed01();
            vertices[i * VERTEX_SIZE + 6] = color.getGreen01();
            vertices[i * VERTEX_SIZE + 7] = color.getBlue01();
            vertices[i * VERTEX_SIZE + 8] = color.getAlpha01();
        }
    }

    @Override
    public void update(double dt)
    {
        updateVertexArray();

        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);
    }

    public void renderCrosshairs()
    {
        shader.use();;
        shader.loadUniform("textureArray", 1);
        RectTransform rectTransform = gameObject.getComponent(RectTransform.class);
        shader.loadUniform("transformationMatrix", rectTransform.getTransformationMatrix());
        glBindVertexArray(vaoID);

        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);

        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
        glVertexAttribPointer(1, TEX_COORDS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_COORDS_OFFSET);
        glVertexAttribPointer(2, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

        glDisable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glDrawElements(GL_TRIANGLES, QUAD_ELEMENT_INDICES.length, GL_UNSIGNED_INT, 0);

        glDisable(GL_BLEND);
        glEnable(GL_DEPTH_TEST);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);

        glBindVertexArray(0);
        shader.detach();
    }

}
