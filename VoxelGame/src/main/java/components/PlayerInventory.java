package components;

import items.InventoryItem;
import items.ItemStack;

public class PlayerInventory extends Component {
    public static final int INVENTORY_SLOTS = 36;
    public static final int ARMOR_SLOTS = 4;
    public static final int CRAFTING_SLOTS = 4;

    private ItemStack[] inventory;
    private ItemStack[] armor;
    private ItemStack[] crafting;

    public PlayerInventory()
    {
        inventory = new ItemStack[INVENTORY_SLOTS];
        armor = new ItemStack[ARMOR_SLOTS];
        crafting = new ItemStack[CRAFTING_SLOTS];
    }

    public boolean addItem(ItemStack itemStack)
    {
        if(itemStack == null || isFull())
            return false;

        for(int i = 0; i < inventory.length; i++) {
            if(inventory[i] == null) {
                inventory[i] = itemStack;
                break;
            }
        }
        return true;
    }

    public boolean removeItem(int slotIndex)
    {
        if(slotIndex >= INVENTORY_SLOTS || slotIndex < 0)
            return false;

        inventory[slotIndex] = null;
        return true;
    }

    public boolean contains(InventoryItem item)
    {
        if(item == null || isEmpty())
            return false;

        boolean hasItem = false;
        for(int i = 0; i < inventory.length; i++) {
            if(inventory[i].getItem() == item) {
                hasItem = true;
            }
        }
        return hasItem;
    }

    public boolean contains(ItemStack itemStack)
    {
        if(itemStack == null || itemStack.getItem() == null || isEmpty())
            return false;

        boolean hasItemStack = false;
        for(int i = 0; i < inventory.length; i++) {
            if(inventory[i] == itemStack) {
                hasItemStack = true;
            }
        }
        return hasItemStack;
    }

    public boolean isFull()
    {
        for(int i = 0; i < inventory.length; i++) {
            if(inventory[i] == null)
                return false;
        }

        return true;
    }

    public boolean isEmpty()
    {
        for(int i = 0; i < inventory.length; i++) {
            if(inventory[i] != null)
                return false;
        }

        return true;
    }

    public void clear()
    {
        for(int i = 0; i < inventory.length; i++)
            inventory[i] = null;
    }

    public ItemStack getItem(int slotIndex) {
        return inventory[slotIndex];
    }

    public void setItem(ItemStack itemStack, int slotIndex)
    {
        if(slotIndex >= INVENTORY_SLOTS || slotIndex < 0)
            return;

        inventory[slotIndex] = itemStack;
    }

    public ItemStack[] getContents() {
        return inventory;
    }

    public ItemStack getHelmet() {
        return armor[0];
    }

    public void setHelmet(ItemStack itemStack) {
        armor[0] = itemStack;
    }

    public ItemStack getChestplate() {
        return armor[1];
    }

    public void setChestplate(ItemStack itemStack) {
        armor[1] = itemStack;
    }

    public ItemStack getLeggigns() {
        return armor[2];
    }

    public void setLeggings(ItemStack itemStack) {
        armor[2] = itemStack;
    }

    public ItemStack getBoots() {
        return armor[3];
    }

    public void setBoots(ItemStack itemStack) {
        armor[3] = itemStack;
    }
}
