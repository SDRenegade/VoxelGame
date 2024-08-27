package renderer;

public enum BlockTextures {
    GRASS_BLOCK_TOP(0, "assets/textures/grass_block_top.png"),
    GRASS_BLOCK_SIDE(1, "assets/textures/grass_block_side.png"),
    DIRT(2, "assets/textures/dirt.png"),
    STONE(3, "assets/textures/stone.png"),
    COBBLESTONE(4, "assets/textures/cobblestone.png");

    private int textureIndex;
    private String texturePath;

    BlockTextures(int textureIndex, String texturePath)
    {
        this.textureIndex = textureIndex;
        this.texturePath = texturePath;
    }

    public int getTextureIndex() { return textureIndex; }

    public String getTexturePath() { return texturePath; }
}
