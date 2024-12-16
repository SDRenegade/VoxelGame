package items;

import util.MathUtil;

public class ItemStack {
    public static final int MAX_STACK_SIZE = 64;

    private InventoryItem item;
    private int amt;

    public ItemStack(InventoryItem item, int amt)
    {
        this.item = item;
        this.amt = MathUtil.clamp(amt, 0, MAX_STACK_SIZE);
    }

    public InventoryItem getItem() {
        return item;
    }

    public void setItem(InventoryItem item) {
        this.item = item;
    }

    public int getAmt() {
        return amt;
    }

    public void setAmt(int amt) {
        this.amt = MathUtil.clamp(amt, 0, MAX_STACK_SIZE);
    }
}
