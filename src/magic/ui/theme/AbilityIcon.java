package magic.ui.theme;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.swing.ImageIcon;
import magic.data.MagicIcon;
import magic.ui.MagicImages;
import magic.model.MagicAbility;
import magic.model.MagicCardDefinition;
import magic.model.MagicObject;
import magic.ui.card.CardIcon;
import magic.ui.utility.MagicStyle;

/**
 * Used to assign an ability with an icon which is displayed
 * on the annotated card image.
 */
public enum AbilityIcon {

    //
    // !! Please try to keep in order so it is easier to maintain !!
    //
    BATTLECRY(MagicAbility.BattleCry, "Battle Cry", "Whenever a creature with battle cry attacks, each other attacking creature gets +1 power for the rest of the turn. This affects all attacking creatures, not just those controlled by the controller of the creature with battle cry."),
    BUYBACK(MagicAbility.Buyback, "Buyback", "As you cast a spell with buyback you may pay an additional cost. As the affected spell resolves, if this cost was paid, the spell will return to your hand instead of the graveyard."),
    DEATHTOUCH(MagicAbility.Deathtouch, "Deathtouch", MagicIcon.DEATHTOUCH, "A creature with Deathtouch will destroy any creature it damages, regardless of the opponent's monster's toughness."),
    DEFENDER(MagicAbility.Defender, "Defender", MagicIcon.DEFENDER, "A permanent with the defender ability cannot attack. It is commonly found on creatures with low power, and high toughness, such as most walls."),
    DOUBLE_STRIKE(MagicAbility.DoubleStrike, "Double Strike", MagicIcon.DOUBLESTRIKE, "A creature with double strike deals damage during the first combat damage step alongside creatures with first strike, then additionally deal damage during the second damage step along with regular creatures."),
    EXALTED(MagicAbility.Exalted, "Exalted", " Any creature that you control that attacks alone will get +1/+1 until the end of the turn. In large numbers, exalted is extremely deadly since a single creature will receive multiple bonuses for attacking alone while leaving the rest untapped and able to defend."),
    FEAR(MagicAbility.Fear, "Fear", MagicIcon.FEAR, "A creature with fear can't be blocked, except by artifact creatures and by creatures that are black."),
    FLASH(MagicAbility.Flash, "Flash", "Creatures, artifacts, and enchantments with flash can be cast at any time an instant could be cast."),
    FIRST_STRIKE(MagicAbility.FirstStrike, "First Strike", MagicIcon.STRIKE, "First strike creates an additional combat damage step. A creature with first strike will deal its combat damage before a creature that doesn't. This often leads to the other creature dying before it gets a chance to strike."),
    FLYING(MagicAbility.Flying, "Flying", MagicIcon.FLYING, "A creature with flying cannot be blocked except by other creatures with either flying or reach. Creatures with flying can block other creatures with or without flying."),
    HASTE(MagicAbility.Haste, "Haste", "Haste allows creatures to ignore the rules informally known as \"summoning sickness\". This means that they can attack, and activate tap or untap abilities on the same turn they enter the battlefield."),
    HEXPROOF(MagicAbility.Hexproof, "Hexproof", MagicIcon.HEXPROOF, "A permanent or player with hexproof cannot be the target of spells or abilities your opponents control, but can still be targeted by spells or abilities you control."),
    INTIMIDATE(MagicAbility.Intimidate, "Intimidate", MagicIcon.INTIMIDATE, "A creature with intimidate can't be blocked, except by artifact creatures and by creatures that share a color with it."),
    INDESTRUCTIBLE(MagicAbility.Indestructible, "Indestructible", MagicIcon.INDESTRUCTIBLE, "Any permanent that has indestructible cannot be destroyed, and cannot die as a result of lethal damage."),
    INFECT(MagicAbility.Infect, "Infect", MagicIcon.INFECT, "A creature with infect doesn't deal damage as normal. If an infect creature deals damage to another creature, it will put that many -1/-1 counters on that creature instead. If an infect creature tries to deal damage to a player, that player gains that many poison counters instead. A player with 10 or more poison counters loses the game."),
    LIFELINK(MagicAbility.Lifelink, "Lifelink", MagicIcon.LIFELINK, "When a creature with lifelink deals combat damage, the controller of that creature also gains an amount of life equal to the amount of damage dealt damage, in addition to dealing the damage as normal."),
    PROTECTION_BLACK(MagicAbility.ProtectionFromBlack, "Protection from Black", MagicIcon.PROTBLACK, AbilityIcon.PROTECTION_TOOLTIP),
    PROTECTION_BLUE(MagicAbility.ProtectionFromBlue, "Protection from Blue", MagicIcon.PROTBLUE, AbilityIcon.PROTECTION_TOOLTIP),
    PROTECTION_COLORS(MagicAbility.ProtectionFromAllColors, "Protection from All Colors", MagicIcon.PROTALLCOLORS, AbilityIcon.PROTECTION_TOOLTIP),
    PROTECTION_GREEN(MagicAbility.ProtectionFromGreen, "Protection from Green", MagicIcon.PROTGREEN, AbilityIcon.PROTECTION_TOOLTIP),
    PROTECTION_RED(MagicAbility.ProtectionFromRed, "Protection from Red", MagicIcon.PROTRED, AbilityIcon.PROTECTION_TOOLTIP),
    PROTECTION_WHITE(MagicAbility.ProtectionFromWhite, "Protection from White", MagicIcon.PROTWHITE, AbilityIcon.PROTECTION_TOOLTIP),
    REACH(MagicAbility.Reach, "Reach", MagicIcon.REACH, "Creatures with reach can block creatures with flying."),
    SHROUD(MagicAbility.Shroud, "Shroud", MagicIcon.SHROUD, "A permanent or player with shroud cannot be the target of any spells or abilities. Shroud can still be affected by effects that do not target. Such as deathtouch; spells that affect all creatures, such as Planar Cleansing; or effects that don't have the word target in them, such as Clone."),
    STORM(MagicAbility.Storm, "Storm", "When a player casts a spell with storm, they put a copy of that spell on the stack for each spell cast before the storm spell in the current turn."),
    TOTEM_ARMOR(MagicAbility.TotemArmor, "Totem Armor", "If a creature enchanted by an Aura with totem armor would be destroyed, all damage is removed from the creature and the Aura with totem armor is destroyed instead. If a creature is enchanted by multiple Auras with totem armor, only one Aura with totem armor (of the creature’s controller’s choice) is destroyed."),
    TRAMPLE(MagicAbility.Trample, "Trample", MagicIcon.TRAMPLE, "If a creature with trample would deal enough combat damage to its blockers to destroy them, it deals the rest of its damage to the defending player."),
    VIGILANCE(MagicAbility.Vigilance, "Vigilance", MagicIcon.VIGILANCE, "A creature with vigilance does not need to tap when attacking, this allows that creature to be able to block on the next player's turn."),
    WITHER(MagicAbility.Wither, "Wither", MagicIcon.WITHER, "Whenever a creature with wither deals damage to another creature, a -1/-1 counter is put on that creature instead for each point of damage dealt. A creature with more than one instance of wither does not deal twice the amount of -1/-1 counters in damage."),
    ;

    private final static String PROTECTION_TOOLTIP = "Protection is normally written \"Protection from {quality}\". For example, something with protection from black can’t be the target of black spells or abilities of black cards. Black spells and abilities that don’t use the word “target” still affect a creature with protection. Anything with protection from black can’t be damaged by black cards. Black creatures can’t block an attacking creature with protection from black.";

    private MagicIcon smallIcon;
    private ImageIcon iconImage;
    private final String iconName;
    private final String tooltip;
    private final MagicAbility ability;

    private AbilityIcon(final MagicAbility ability, final String name, final MagicIcon magicIcon, final String tooltip) {
        this.iconName = name;
        this.tooltip = tooltip;
        this.ability = ability;
        this.smallIcon = magicIcon;
        setIconImage(magicIcon);
    }
    private AbilityIcon(final MagicAbility ability, final String name, final String tooltip) {
        this(ability, name, null, tooltip);
    }

    private void setIconImage(final MagicIcon magicIcon) {
        final ImageIcon themeIcon = MagicStyle.getTheme().getAbilityIcon(this);
        if (themeIcon == null) {
            iconImage = magicIcon != null ? MagicImages.getIcon(magicIcon) : null;
        } else {
            iconImage = themeIcon;
        }
    }

    public ImageIcon getIcon() {
        return iconImage;
    }

    public String getName() {
        return iconName;
    }

    public String getTooltip() {
        return tooltip;
    }

    private MagicAbility getAbility() {
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

    private boolean hasSmallIcon() {
        return smallIcon != null;
    }
    
    private ImageIcon getSmallIcon() {
        return hasSmallIcon() ? MagicImages.getIcon(smallIcon) : null;
    }

    /**
     * Returns a list of small 16x16 ability icon images that are 
     * drawn onto a card image displayed on the battlefield.
     */
    public static  List<Image> getSmallAbilityIcons(final Set<MagicAbility> abilities) {
        return Stream.of(values())
            .filter(i -> abilities.contains(i.ability))
            .filter(AbilityIcon::hasSmallIcon)
            .map(i -> i.getSmallIcon().getImage())
            .collect(Collectors.toList());
    }

    private static CardIcon getAbilityIcon(final AbilityIcon abilityIcon) {
        final CardIcon icon = new CardIcon();
        icon.setName(abilityIcon.getName());
        icon.setDescription(abilityIcon.getTooltip());
        icon.setIcon(abilityIcon.getIcon());
        return icon;
    }

}
