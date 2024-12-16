package components.ui;

public class HotbarSlotUI {
    private Image background;
    private Image display;

    public HotbarSlotUI(Image background, Image display)
    {
        this.background = background;
        this.display = display;
    }

    public Image getBackground() {
        return background;
    }

    public void setBackground(Image background) {
        this.background = background;
    }

    public Image getDisplay() {
        return display;
    }

    public void setDisplay(Image display) {
        this.display = display;
    }
}
