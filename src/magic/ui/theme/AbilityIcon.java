package magic.ui.theme;

import javax.swing.ImageIcon;
import magic.data.IconImages;

public enum AbilityIcon {

    ICON_INFECT(IconImages.INFECT),
    ICON_REACH(IconImages.REACH),
    ICON_DEATHTOUCH(IconImages.DEATHTOUCH),
    ICON_TOTEM_ARMOR(),
    ICON_TRAMPLE(IconImages.TRAMPLE),
    ICON_SHROUD(IconImages.SHROUD),
    ICON_VIGILANCE(IconImages.VIGILANCE),
    ICON_DOUBLE_STRIKE(IconImages.DOUBLESTRIKE),
    ICON_BATTLECRY(),
    ICON_FIRST_STRIKE(IconImages.STRIKE),
    ICON_FLYING(IconImages.FLYING),
    ICON_HASTE();

    private static final Theme THEME = ThemeFactory.getInstance().getCurrentTheme();
    private final ImageIcon defaultIcon;

    private AbilityIcon(final ImageIcon defaultIcon) {
        this.defaultIcon = defaultIcon;
    }
    private AbilityIcon() {
        this(IconImages.MISSING_ICON);
    }

    public ImageIcon getIcon() {
        final ImageIcon icon = THEME.getAbilityIcon(this);
        return icon == null ? defaultIcon : icon;
    }

}
