package world;

public class Chunk {
    private ChunkData chunkData;
    private boolean isDirty;

    public Chunk(int xPos, int zPos, World world)
    {
        chunkData = new ChunkData(xPos, zPos, world);
        isDirty = false;
    }

    // Positions are chunk relative coordinates (player position / chunk size)
    public static long generateChunkID(int xPos, int zPos)
    {
        return (((long)xPos) << 32) | (zPos & 0xffffffffL);
    }

    public long getChunkID()
    {
        return (((long)chunkData.getXPos()) << 32) | (chunkData.getZPos() & 0xffffffffL);
    }

    public ChunkData getChunkData() { return chunkData; }

    public boolean isDirty() { return isDirty; }

    public void setDirty(boolean isDirty) { this.isDirty = isDirty; }

}
