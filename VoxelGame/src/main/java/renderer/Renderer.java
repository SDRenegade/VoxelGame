package renderer;

import components.QuadRenderer;
import main.GameObject;

import java.util.ArrayList;
import java.util.List;

public class Renderer {
    private final int MAX_BATCH_SIZE = 1000;

    private List<RenderBatch> batches;

    public Renderer()
    {
        batches = new ArrayList<>();
    }

    public void add(GameObject gameObject)
    {
        QuadRenderer quadRenderer = gameObject.getComponent(QuadRenderer.class);
        if(quadRenderer != null)
            add(quadRenderer);
    }

    public void add(QuadRenderer quadRenderer)
    {
        int quadIndex = 0;
        // First fill any batches that are not already full
        for(RenderBatch batch : batches) {
            if(batch.isFull())
                continue;

            for(int i = quadIndex; i < quadRenderer.getVertexArray().length / (RenderBatch.VERTEX_SIZE * 4); i++) {
                batch.addQuadToBatch(quadRenderer, quadIndex);
                quadIndex++;

                if(batch.isFull())
                    break;
            }

            if(quadIndex > quadRenderer.getVertexArray().length / (RenderBatch.VERTEX_SIZE * 4))
                break;
        }

        // If all current batches are filled, add new batches
        while(quadIndex < quadRenderer.getVertexArray().length / (RenderBatch.VERTEX_SIZE * 4)) {
            RenderBatch newBatch = new RenderBatch(MAX_BATCH_SIZE);
            newBatch.start();
            batches.add(newBatch);
            if(!newBatch.isFull()) {
                for(int i = quadIndex; i < quadRenderer.getVertexArray().length / (RenderBatch.VERTEX_SIZE * 4); i++) {
                    newBatch.addQuadToBatch(quadRenderer, quadIndex);
                    quadIndex++;

                    if(newBatch.isFull())
                        break;
                }
            }
        }
    }

    public void render()
    {
        int drawCallsPerFrame = 0;
        for(RenderBatch batch : batches) {
            batch.render();
            drawCallsPerFrame++;
        }
        //System.out.println("Draw calls this frame: " + drawCallsPerFrame);
    }

}
