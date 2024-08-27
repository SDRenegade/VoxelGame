package components;

public class QuadRenderer extends Component {
    private float[] vertexArray;

    public QuadRenderer(float[] vertexArray)
    {
        this.vertexArray = vertexArray;
    }

    @Override
    public void update(double dt)
    {

    }

    public float[] getVertexArray() { return vertexArray; }
}
