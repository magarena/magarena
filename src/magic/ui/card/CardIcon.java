package magic.ui.card;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public class CardIcon {

    private ImageIcon icon;
    private String name;
    private String description;
    private CompoundIcon compoundIcon;
    private final ImageIcon ICON_BACKGROUND;

    public CardIcon(final ImageIcon iconBackground) {
        this.ICON_BACKGROUND = iconBackground;
    }

    public ImageIcon getIcon() {
        return icon;
    }

    public void setIcon(ImageIcon icon) {
        this.icon = icon;
        this.compoundIcon = new CompoundIcon(CompoundIcon.Axis.Z_AXIS, ICON_BACKGROUND, icon);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public Icon getCompoundIcon() {
        return compoundIcon;
    }

}
