package renderer;

import components.Component;
import util.AssetPool;
import util.ShaderType;

import java.util.Map;

import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class UIRenderer extends Component {
    private Shader shader;
    private int vaoID;

    @Override
    public void start()
    {
        shader = AssetPool.getInstance().getShader(ShaderType.UI);

        // Generate and bind a VAO
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);
    }

    @Override
    public void update(double dt) {
        shader.use();;

        glBindVertexArray(vaoID);

        // TODO Call render methods on all UI elements

        glBindVertexArray(0);

        shader.detach();
    }

}
