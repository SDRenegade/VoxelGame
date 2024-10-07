package items;

import blocks.Block;
import org.joml.Vector3f;
import world.Location;
import world.World;

public class ItemBlock implements InventoryItem {
    public static final ItemBlock GRASS_BLOCK = new ItemBlock(Block.GRASS_BLOCK);
    public static final ItemBlock DIRT = new ItemBlock(Block.DIRT);
    public static final ItemBlock STONE = new ItemBlock(Block.STONE);
    public static final ItemBlock COBBLESTONE = new ItemBlock(Block.COBBLESTONE);

    private Block block;

    public ItemBlock(Block block) {
        this.block = block;
    }

    @Override
    public void renderItem() {

    }

    @Override
    public void interact(Location location) {
        location.getWorld().setBlock(location, block.getBlockID());
    }

    public Block getBlock() {
        return block;
    }
}
