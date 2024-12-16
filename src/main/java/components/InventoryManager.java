package components;

import components.ui.HotbarUI;
import items.ItemBlock;
import items.ItemStack;
import listeners.PlayerInteractionEventArgs;
import listeners.PlayerInteractionListener;
import window.KeyListener;

import static org.lwjgl.glfw.GLFW.*;

public class InventoryManager extends Component implements PlayerInteractionListener {
    private PlayerInventory playerInventory;
    private HotbarUI hotbarUI;
    //private PlayerInventoryUI playerInventoryUI;
    private byte selectedHotbarSlot;

    public InventoryManager(PlayerInventory playerInventory)
    {
        this.playerInventory = playerInventory;
        selectedHotbarSlot = 0;

        // temp
        playerInventory.setItem(new ItemStack(ItemBlock.GRASS_BLOCK, 1), 0);
        playerInventory.setItem(new ItemStack(ItemBlock.DIRT, 1), 1);
        playerInventory.setItem(new ItemStack(ItemBlock.STONE, 1), 2);
        playerInventory.setItem(new ItemStack(ItemBlock.COBBLESTONE, 1), 3);
    }

    @Override
    public void update(double dt)
    {
        if(KeyListener.isKeyPressed(GLFW_KEY_1)) {
            selectedHotbarSlot = 0;
            System.out.println("hotbar slot 1 selected");
        }
        else if(KeyListener.isKeyPressed(GLFW_KEY_2)) {
            selectedHotbarSlot = 1;
            System.out.println("hotbar slot 2 selected");
        }
        else if(KeyListener.isKeyPressed(GLFW_KEY_3)) {
            selectedHotbarSlot = 2;
            System.out.println("hotbar slot 3 selected");
        }
        else if(KeyListener.isKeyPressed(GLFW_KEY_4)) {
            selectedHotbarSlot = 3;
            System.out.println("hotbar slot 4 selected");
        }
    }

    @Override
    public void onPlayerInteraction(Object sender, PlayerInteractionEventArgs eventArgs)
    {
        if(playerInventory.getItem(selectedHotbarSlot) == null)
            return;

        playerInventory.getItem(selectedHotbarSlot).getItem().interact(eventArgs.getLocation());
    }

    public void addPlayerInteractionListener(PlayerInteraction playerInteraction)
    {
        playerInteraction.addListener(this);
    }

    public PlayerInventory getPlayerInventory() {
        return playerInventory;
    }

    public void setPlayerInventory(PlayerInventory playerInventory) {
        this.playerInventory = playerInventory;
    }
}
