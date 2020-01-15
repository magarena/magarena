package magic.model;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import magic.model.event.MagicManaActivation;

public interface IRenderableCard {

    Collection<MagicManaActivation> getManaActivations();
    Set<MagicSubType> getSubTypes();
    int getColorFlags();
    String getPowerToughnessText();
    String getName();
    boolean hasSubType(MagicSubType subType);
    boolean hasColor(MagicColor color);
    boolean hasType(MagicType type);
    boolean isToken();
    boolean isFlipCard();
    boolean isSplitCard();
    boolean isDoubleFaced();
    boolean hasAbility(MagicAbility ability);
    MagicCardDefinition getCardDefinition();

    default MagicCardDefinition getTransformedDefinition() {
        return getCardDefinition().getTransformedDefinition();
    }

    default MagicCardDefinition getSplitDefinition() {
        return getCardDefinition().getSplitDefinition();
    }

    default int getStartingLoyalty() {
        return getCardDefinition().getStartingLoyalty();
    }

    default boolean isMulti() {
        return getNumColors() > 1;
    }

    default boolean isHybrid() {
        final List<MagicCostManaType> list = getCost().getCostManaTypes(0);
        //If doesn't contain single color mana, and does contain hybrid mana. Checks for absence
        return Collections.disjoint(list, MagicCostManaType.MONO) && !Collections.disjoint(list, MagicCostManaType.HYBRID);
    }

    default boolean isPlaneswalker() {
        return hasType(MagicType.Planeswalker);
    }

    default boolean isLegendary() {
        return hasType(MagicType.Legendary);
    }

    default boolean isLand() {
        return hasType(MagicType.Land);
    }

    default boolean isCreature() {
        return hasType(MagicType.Creature);
    }

    default boolean isArtifact() {
        return hasType(MagicType.Artifact);
    }

    default boolean isEnchantment() {
        return hasType(MagicType.Enchantment);
    }

    default boolean isInstant() {
        return hasType(MagicType.Instant);
    }

    default boolean isSorcery() {
        return hasType(MagicType.Sorcery);
    }

    default boolean isSaga() {
        return isEnchantment() && hasSubType(MagicSubType.Saga);
    }

    default boolean isHidden() {
        return getCardDefinition().isHidden();
    }

    default boolean isSecondHalf() {
        return getCardDefinition().isSecondHalf();
    }

    default Character getRarityChar() {
        return getCardDefinition().getRarityChar();
    }

    default boolean hasText() {
        return getCardDefinition().hasText();
    }

    default int getNumColors() {
        int numColors = 0;
        for (final MagicColor color : MagicColor.values()) {
            if (hasColor(color)) {
                numColors++;
            }
        }
        return numColors;
    }

    default Set<MagicType> getTypes() {
        Set<MagicType> types = new HashSet<>();
        for (MagicType type : MagicType.values()) {
            if (hasType(type)) {
                types.add(type);
            }
        }
        return types;
    }

    default String getSubTypeText() {
        // returning from CardDefinition, no in-game changes
        return getCardDefinition().getSubTypeText();
    }

    default String getImageName() {
        return getCardDefinition().getImageName();
    }

    default String getText() {
        // returns oracle text, no in-game changes
        return getCardDefinition().getText();
    }

    default MagicManaCost getCost() {
        // returning from CardDefinition, no in-game changes
        return getCardDefinition().getCost();
    }

    default boolean hasOverlay() {
        return hasAbility(MagicAbility.Devoid) || hasAbility(MagicAbility.Miracle);
    }

    default boolean hasTextOverlay() {
        return hasAbility(MagicAbility.LevelUp) || isLand();
    }

    default long getRenderKey() {
        return getCardDefinition().getDistinctName().hashCode();
    }

    default boolean isUnknown() {
        return getCardDefinition() == MagicCardDefinition.UNKNOWN;
    }

    default String getImageUrl() {
        return getCardDefinition().getImageURL();
    }

    default boolean isInvalid() {
        return getCardDefinition().isInvalid();
    }
}
