package blocks;

public enum BlockFaces {
    FRONT(0),
    BACK(1),
    LEFT(2),
    RIGHT(3),
    TOP(4),
    BOTTOM(5);

    private int faceIndex;

    BlockFaces(int faceIndex)
    {
        this.faceIndex = faceIndex;
    }

    public int getFaceIndex() { return faceIndex; }
}
