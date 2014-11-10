package magic.ui.theme;

import javax.swing.ImageIcon;
import magic.data.IconImages;

/**
 * Used to assign an ability with an icon which is displayed
 * on the annotated card image.
 */
public enum AbilityIcon {

    //
    // !! Please try to keep in order so it is easier to maintain !!
    //
    DEATHTOUCH("Deathtouch", IconImages.DEATHTOUCH, "A creature with Deathtouch will destroy any creature it damages, regardless of the opponent's monster's toughness."),
    DEFENDER("Defender", IconImages.DEFENDER, "Defender is a static ability that means the affected permanent cannot attack. It is commonly found on creatures with low power, and high toughness, such as most walls."),
    DOUBLE_STRIKE("Double Strike", IconImages.DOUBLESTRIKE, "A creature with double strike deals damage during the first combat damage step alongside creatures with first strike, then additionally deal damage during the second damage step along with regular creatures."),
    FEAR("Fear", IconImages.FEAR, "Fear is a static ability which restricts the types of creatures that can block a creature with fear. A creature with fear can't be blocked, except by artifact creatures and by creatures that are black."),
    FIRST_STRIKE("First Strike", IconImages.STRIKE, "First strike is a static ability that creates an additional combat damage step. A creature with first strike will deal its combat damage before a creature that doesn't. This often leads to the other creature dying before it gets a chance to strike."),
    FLYING("Flying", IconImages.FLYING, "A creature with flying cannot be blocked except by other creatures with either flying or reach. Creatures with flying can block other creatures with or without flying."),
    HEXPROOF("Hexproof", IconImages.HEXPROOF, "Hexproof is a static ability that means that the affected permanent or player cannot be the target of spells or abilities your opponents control, but can still be targeted by spells or abilities you control."),
    INDESTRUCTIBLE("Indestructible", IconImages.INDESTRUCTIBLE, "Indestructible is a static ability that means that any permanent that has indestructible cannot be destroyed, and cannot die as a result of lethal damage."),
    LIFELINK("Lifelink", IconImages.LIFELINK, "Lifelink is a static ability that modifies the result of combat damage. When a creature with lifelink deals combat damage, the controller of that creature also gains an amount of life equal to the amount of damage dealt damage, in addition to dealing the damage as normal."),
    REACH("Reach", IconImages.REACH, "Reach is a static ability that means the affected creature can block creatures with flying."),
    SHROUD("Shroud", IconImages.SHROUD, "Shroud is a static ability that means the affected permanent or player cannot be the target of any spells or abilities. Shroud can still be affected by effects that do not target. Such as deathtouch; spells that affect all creatures, such as Planar Cleansing; or effects that don't have the word target in them, such as Clone."),
    TRAMPLE("Trample", IconImages.TRAMPLE, "If a creature with trample would deal enough combat damage to its blockers to destroy them, it deals the rest of its damage to the defending player."),
    VIGILANCE("Vigilance", IconImages.VIGILANCE, "Vigilance is a static ability that means the affected creature does not need to tap when attacking, this allows that creature to be able to block on the next player's turn."),
    ;

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
