package magic.ui.theme;

import javax.swing.ImageIcon;
import magic.data.IconImages;

public enum AbilityIcon {

    FLYING("Flying", IconImages.FLYING, "A creature with flying cannot be blocked except by other creatures with either flying or reach. Creatures with flying can block other creatures with or without flying."),
    FIRST_STRIKE("First Strike", IconImages.STRIKE, "First strike is a static ability that creates an additional combat damage step. A creature with first strike will deal its combat damage before a creature that doesn't. This often leads to the other creature dying before it gets a chance to strike."),
    TRAMPLE("Trample", IconImages.TRAMPLE, "If a creature with trample would deal enough combat damage to its blockers to destroy them, it deals the rest of its damage to the defending player."),
    ;
//    INFECT(IconImages.INFECT),
//    REACH(IconImages.REACH),
//    DEATHTOUCH(IconImages.DEATHTOUCH),
//    TOTEM_ARMOR(),

//    SHROUD(IconImages.SHROUD),
//    VIGILANCE(IconImages.VIGILANCE),
//    DOUBLE_STRIKE(IconImages.DOUBLESTRIKE),
//    BATTLECRY(),
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
