package magic.ui.theme;

import javax.swing.ImageIcon;
import magic.data.IconImages;

public enum AbilityIcon {

    FLYING("Flying", IconImages.FLYING, "Creatures with flying can’t be blocked except by creatures with flying or reach.");
//    INFECT(IconImages.INFECT),
//    REACH(IconImages.REACH),
//    DEATHTOUCH(IconImages.DEATHTOUCH),
//    TOTEM_ARMOR(),
//    TRAMPLE(IconImages.TRAMPLE),
//    SHROUD(IconImages.SHROUD),
//    VIGILANCE(IconImages.VIGILANCE),
//    DOUBLE_STRIKE(IconImages.DOUBLESTRIKE),
//    BATTLECRY(),
//    FIRST_STRIKE(IconImages.STRIKE),
//    HASTE();

    private static final Theme THEME = ThemeFactory.getInstance().getCurrentTheme();
    
    private final ImageIcon defaultIcon;
    private final String iconName;
    private final String tooltip;

    private AbilityIcon(final String name, final ImageIcon defaultIcon, final String tooltip) {
        this.iconName = name;
        this.defaultIcon = defaultIcon;
        this.tooltip = tooltip;
    }

    public ImageIcon getIcon() {
        final ImageIcon icon = THEME.getAbilityIcon(this);
        return icon == null ? defaultIcon : icon;
    }

    public String getName() {
        return iconName;
    }

    public String getTooltip() {
        return tooltip;
    }

}
