package components.ui;

import components.Component;
import components.PlayerInventory;

public class HotbarUI extends Component {
    public static final int MAX_HOTBAR_SLOTS = 9;

    private HotbarSlotUI[] hotbarSlots;
    private Image selectCursor;

    public HotbarUI()
    {
        hotbarSlots = new HotbarSlotUI[MAX_HOTBAR_SLOTS];
    }

    public void updateHotbarSlots(PlayerInventory playerInventory)
    {
        for(int i = 0; i < hotbarSlots.length; i++) {
            if(hotbarSlots[i] != null && hotbarSlots[i].getDisplay() != null) {
                //if(hotbarSlots[i].getDisplay().getTextureIndex() == playerInventory.getContents()[i].getItem())
            }
        }
    }

    public void addHotbarSlot(HotbarSlotUI hotbarSlot)
    {
        for(int i = 0; i < hotbarSlots.length; i++) {
            if(hotbarSlots[i] == null) {
                hotbarSlots[i] = hotbarSlot;
                return;
            }
        }
    }

    public HotbarSlotUI[] getHotbarSlots() {
        return hotbarSlots;
    }

    public void setHotbarSlots(HotbarSlotUI[] hotbarSlots) {
        this.hotbarSlots = hotbarSlots;
    }

    public Image getSelectCursor() {
        return selectCursor;
    }

    public void setSelectCursor(Image selectCursor) {
        this.selectCursor = selectCursor;
    }
}
