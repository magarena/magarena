package magic.ui.theme;

import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import magic.data.IconImages;
import magic.model.MagicAbility;
import magic.model.MagicCardDefinition;
import magic.model.MagicObject;
import magic.ui.card.CardIcon;

/**
 * Used to assign an ability with an icon which is displayed
 * on the annotated card image.
 */
public enum AbilityIcon {

    //
    // !! Please try to keep in order so it is easier to maintain !!
    //
    DEATHTOUCH(MagicAbility.Deathtouch, "Deathtouch", IconImages.DEATHTOUCH, "A creature with Deathtouch will destroy any creature it damages, regardless of the opponent's monster's toughness."),
    DEFENDER(MagicAbility.Defender, "Defender", IconImages.DEFENDER, "Defender is a static ability that means the affected permanent cannot attack. It is commonly found on creatures with low power, and high toughness, such as most walls."),
    DOUBLE_STRIKE(MagicAbility.DoubleStrike, "Double Strike", IconImages.DOUBLESTRIKE, "A creature with double strike deals damage during the first combat damage step alongside creatures with first strike, then additionally deal damage during the second damage step along with regular creatures."),
    FEAR(MagicAbility.Fear, "Fear", IconImages.FEAR, "Fear is a static ability which restricts the types of creatures that can block a creature with fear. A creature with fear can't be blocked, except by artifact creatures and by creatures that are black."),
    FIRST_STRIKE(MagicAbility.FirstStrike, "First Strike", IconImages.STRIKE, "First strike is a static ability that creates an additional combat damage step. A creature with first strike will deal its combat damage before a creature that doesn't. This often leads to the other creature dying before it gets a chance to strike."),
    FLYING(MagicAbility.Flying, "Flying", IconImages.FLYING, "A creature with flying cannot be blocked except by other creatures with either flying or reach. Creatures with flying can block other creatures with or without flying."),
    HASTE(MagicAbility.Haste, "Haste", null, "Haste allows creatures to ignore the rules informally known as \"summoning sickness\". This means that they can attack, and activate tap or untap abilities on the same turn they enter the battlefield."),
    HEXPROOF(MagicAbility.Hexproof, "Hexproof", IconImages.HEXPROOF, "Hexproof is a static ability that means that the affected permanent or player cannot be the target of spells or abilities your opponents control, but can still be targeted by spells or abilities you control."),
    INTIMIDATE(MagicAbility.Intimidate, "Intimidate", IconImages.INTIMIDATE, "Intimidate is a static ability which restricts the types of creatures that can block a creature with intimidate. A creature with intimidate can't be blocked, except by artifact creatures and by creatures that share a color with it."),
    INDESTRUCTIBLE(MagicAbility.Indestructible, "Indestructible", IconImages.INDESTRUCTIBLE, "Indestructible is a static ability that means that any permanent that has indestructible cannot be destroyed, and cannot die as a result of lethal damage."),
    INFECT(MagicAbility.Infect, "Infect", IconImages.INFECT, "A creature with infect doesn't deal damage as normal. If an infect creature deals damage to another creature, it will put that many -1/-1 counters on that creature instead. If an infect creature tries to deal damage to a player, that player gains that many poison counters instead. A player with 10 or more poison counters loses the game."),
    LIFELINK(MagicAbility.Lifelink, "Lifelink", IconImages.LIFELINK, "Lifelink is a static ability that modifies the result of combat damage. When a creature with lifelink deals combat damage, the controller of that creature also gains an amount of life equal to the amount of damage dealt damage, in addition to dealing the damage as normal."),
    REACH(MagicAbility.Reach, "Reach", IconImages.REACH, "Reach is a static ability that means the affected creature can block creatures with flying."),
    SHROUD(MagicAbility.Shroud, "Shroud", IconImages.SHROUD, "Shroud is a static ability that means the affected permanent or player cannot be the target of any spells or abilities. Shroud can still be affected by effects that do not target. Such as deathtouch; spells that affect all creatures, such as Planar Cleansing; or effects that don't have the word target in them, such as Clone."),
    TRAMPLE(MagicAbility.Trample, "Trample", IconImages.TRAMPLE, "If a creature with trample would deal enough combat damage to its blockers to destroy them, it deals the rest of its damage to the defending player."),
    VIGILANCE(MagicAbility.Vigilance, "Vigilance", IconImages.VIGILANCE, "Vigilance is a static ability that means the affected creature does not need to tap when attacking, this allows that creature to be able to block on the next player's turn."),
    WITHER(MagicAbility.Wither, "Wither", IconImages.WITHER, "Whenever a creature with wither deals damage to another creature, a -1/-1 counter is put on that creature instead for each point of damage dealt. A creature with more than one instance of wither does not deal twice the amount of -1/-1 counters in damage."),
    ;

    private final Theme THEME = ThemeFactory.getInstance().getCurrentTheme();

    private final ImageIcon defaultIcon;
    private final String iconName;
    private final String tooltip;
    private final MagicAbility ability;

    private AbilityIcon(final MagicAbility ability, final String name, final ImageIcon defaultIcon, final String tooltip) {
        this.iconName = name;
        this.defaultIcon = defaultIcon;
        this.tooltip = tooltip;
        this.ability = ability;
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

    public MagicAbility getAbility() {
        return ability;
    }

    public static List<CardIcon> getIcons(final MagicObject magicObject) {
        final List<CardIcon> icons = new ArrayList<>();
        for (AbilityIcon abilityIcon : AbilityIcon.values()) {
            if (abilityIcon.getIcon() != null && magicObject.hasAbility(abilityIcon.getAbility())) {
                icons.add(getAbilityIcon(abilityIcon));
            }
        }
        return icons;
    }

    public static List<CardIcon> getIcons(final MagicCardDefinition cardDef) {
        final List<CardIcon> icons = new ArrayList<>();
        for (AbilityIcon abilityIcon : AbilityIcon.values()) {
            if (abilityIcon.getIcon() != null && cardDef.hasAbility(abilityIcon.getAbility())) {
                icons.add(getAbilityIcon(abilityIcon));
            }
        }
        return icons;
    }

    private static CardIcon getAbilityIcon(final AbilityIcon abilityIcon) {
        final CardIcon icon = new CardIcon(IconImages.BACKDROP_ICON);
        icon.setName(abilityIcon.getName());
        icon.setDescription(abilityIcon.getTooltip());
        icon.setIcon(abilityIcon.getIcon());
        return icon;
    }

}
