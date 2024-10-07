package renderer;

import components.*;
import components.ui.Graphic;
import components.ui.RectTransform;
import gameobjects.GameObject;
import org.joml.Vector2f;
import util.RectPivot;
import window.Window;

import java.util.ArrayList;
import java.util.List;

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
    private int[] indices;
    private List<Float> vertexList;
    private List<Integer> indexList;

    public UiRenderer(Shader shader) {
        this.shader = shader;
        vertexList = new ArrayList<>();
        indexList = new ArrayList<>();
    }

    @Override
    public void start() {
        // Generate buffers
        vaoID = glGenVertexArrays();
        vboID = glGenBuffers();
        eboID = glGenBuffers();
    }

    private void updateVertices(Graphic graphic) {
        RectTransform rectTransform = gameObject.getComponent(RectTransform.class);
        for(int i = 0; i < 4; i++) {
            float xPos, yPos;
            if(rectTransform.getPivot().getX() == 0f)
                xPos = i <= 1 ? 0 : rectTransform.getDimensions().x;
            else if(rectTransform.getPivot().getX() == 0.5f)
                xPos = i <= 1 ? -rectTransform.getDimensions().x / 2 : rectTransform.getDimensions().x / 2;
            else
                xPos = i <= 1 ? -rectTransform.getDimensions().x : 0;

            if(rectTransform.getPivot().getY() == 0f)
                yPos = i % 3 == 0 ? 0 : rectTransform.getDimensions().y;
            else if(rectTransform.getPivot().getY() == 0.5f)
                yPos = i % 3 == 0 ? -rectTransform.getDimensions().y / 2 : rectTransform.getDimensions().y / 2;
            else
                yPos = i % 3 == 0 ? -rectTransform.getDimensions().y : 0;

            Vector2f anchorActual = calculateAnchorPosition(gameObject.getParent(), rectTransform.getAnchor());

            vertexList.add((xPos / Window.DEFAULT_WINDOW_WIDTH * ((float)Window.DEFAULT_WINDOW_WIDTH / (float)Window.getInstance().getWidth()) + anchorActual.x) * 2 - 1);
            vertexList.add((yPos / Window.DEFAULT_WINDOW_HEIGHT * ((float)Window.DEFAULT_WINDOW_HEIGHT / (float)Window.getInstance().getHeight()) + anchorActual.y) * 2 - 1);

            vertexList.add(TEXTURE_COORDS_ARRAY[i * 2]);
            vertexList.add(TEXTURE_COORDS_ARRAY[i * 2 + 1]);
            vertexList.add((float)graphic.getTextureIndex());

            vertexList.add(graphic.getColor().getRed01());
            vertexList.add(graphic.getColor().getGreen01());
            vertexList.add(graphic.getColor().getBlue01());
            vertexList.add(graphic.getColor().getAlpha01());
        }
    }

    // Does not factor in GameObjects scale or rotation
    private Vector2f calculateAnchorPosition(GameObject parent, RectPivot childAnchorRelative) {
        if(parent == null)
            return new Vector2f(childAnchorRelative.getX(), childAnchorRelative.getY());

        RectTransform parentTransform = (RectTransform)parent.getTransform();
        Vector2f parentAnchorActual = calculateAnchorPosition(parent.getParent(), parentTransform.getPivot());

        float xPositive, xNegative, yPositive, yNegative;
        if(parentTransform.getPivot().getX() == 0f) {
            xPositive = ((parentTransform.getDimensions().x + parentTransform.getPosition().x) / Window.DEFAULT_WINDOW_WIDTH) * ((float)Window.DEFAULT_WINDOW_WIDTH / (float)Window.getInstance().getWidth()) + parentAnchorActual.x;
            xNegative = (parentTransform.getPosition().x / Window.DEFAULT_WINDOW_WIDTH) * ((float)Window.DEFAULT_WINDOW_WIDTH / (float)Window.getInstance().getWidth()) + parentAnchorActual.x;
        }
        else if(parentTransform.getPivot().getX() == 0.5f) {
            xPositive = ((parentTransform.getDimensions().x / 2 + parentTransform.getPosition().x) / Window.DEFAULT_WINDOW_WIDTH) * ((float)Window.DEFAULT_WINDOW_WIDTH / (float)Window.getInstance().getWidth()) + parentAnchorActual.x;
            xNegative = ((-parentTransform.getDimensions().x / 2 + parentTransform.getPosition().x) / Window.DEFAULT_WINDOW_WIDTH) * ((float)Window.DEFAULT_WINDOW_WIDTH / (float)Window.getInstance().getWidth()) + parentAnchorActual.x;
        }
        else {
            xPositive = parentTransform.getPosition().x * ((float)Window.DEFAULT_WINDOW_WIDTH / (float)Window.getInstance().getWidth()) + parentAnchorActual.x;
            xNegative = ((-parentTransform.getDimensions().x + parentTransform.getPosition().x) / Window.DEFAULT_WINDOW_WIDTH) * ((float)Window.DEFAULT_WINDOW_WIDTH / (float)Window.getInstance().getWidth()) + parentAnchorActual.x;
        }

        if(parentTransform.getPivot().getY() == 0f) {
            yPositive = ((parentTransform.getDimensions().y + parentTransform.getPosition().y) / Window.DEFAULT_WINDOW_HEIGHT) * ((float)Window.DEFAULT_WINDOW_HEIGHT / (float)Window.getInstance().getHeight()) + parentAnchorActual.y;
            yNegative = (parentTransform.getPosition().y / Window.DEFAULT_WINDOW_HEIGHT) * ((float)Window.DEFAULT_WINDOW_HEIGHT / (float)Window.getInstance().getHeight()) + parentAnchorActual.y;
        }
        else if(parentTransform.getPivot().getY() == 0.5f) {
            yPositive = ((parentTransform.getDimensions().y / 2 + parentTransform.getPosition().y) / Window.DEFAULT_WINDOW_HEIGHT) * ((float)Window.DEFAULT_WINDOW_HEIGHT / (float)Window.getInstance().getHeight()) + parentAnchorActual.y;
            yNegative = ((-parentTransform.getDimensions().y / 2 + parentTransform.getPosition().y) / Window.DEFAULT_WINDOW_HEIGHT) * ((float)Window.DEFAULT_WINDOW_HEIGHT / (float)Window.getInstance().getHeight()) + parentAnchorActual.y;
        }
        else {
            yPositive = (parentTransform.getPosition().y / Window.DEFAULT_WINDOW_HEIGHT) * ((float)Window.DEFAULT_WINDOW_HEIGHT / (float)Window.getInstance().getHeight()) + parentAnchorActual.y;
            yNegative = ((-parentTransform.getDimensions().y + parentTransform.getPosition().y) / Window.DEFAULT_WINDOW_HEIGHT) * ((float)Window.DEFAULT_WINDOW_HEIGHT / (float)Window.getInstance().getHeight()) + parentAnchorActual.y;
        }

        Vector2f childAnchorActual = new Vector2f();
        if(childAnchorRelative.getX() == 0f)
            childAnchorActual.x = xNegative;
        else if(childAnchorRelative.getX() == 0.5f)
            childAnchorActual.x = xNegative + ((xPositive - xNegative) / 2);
        else
            childAnchorActual.x = xPositive;

        if(childAnchorRelative.getY() == 0f)
            childAnchorActual.y = yNegative;
        else if(childAnchorRelative.getY() == 0.5f)
            childAnchorActual.y = yNegative + ((yPositive - yNegative) / 2);
        else
            childAnchorActual.y = yPositive;

        return childAnchorActual;
    }

    private void updateIndices(int graphicCount) {
        for(int i = 0; i < QUAD_ELEMENT_INDICES.length; i++)
            indexList.add(QUAD_ELEMENT_INDICES[i] + (4 * graphicCount));
    }

    @Override
    public void update(double dt) {
        int graphicCount = 0;
        for(int i = 0; i < gameObject.getComponentList().size(); i++) {
            if(gameObject.getComponentList().get(i) instanceof Graphic) {
                Graphic graphic = (Graphic)(gameObject.getComponentList().get(i));
                updateVertices(graphic);
                updateIndices(graphicCount);
                graphicCount++;
            }
        }

        vertices = new float[vertexList.size()];
        for(int i = 0; i < vertexList.size(); i++) {
            vertices[i] = vertexList.get(i);
        }
        vertexList.clear();

        indices = new int[indexList.size()];
        for(int i = 0; i < indexList.size(); i++)
            indices[i] = indexList.get(i);
        indexList.clear();

        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, QUAD_ELEMENT_INDICES, GL_STATIC_DRAW);
    }

    public void render() {
        shader.use();
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
