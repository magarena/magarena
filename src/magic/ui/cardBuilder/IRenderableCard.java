package magic.ui.cardBuilder;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;

import magic.model.MagicAbility;
import magic.model.MagicColor;
import magic.model.MagicManaCost;
import magic.model.MagicSubType;
import magic.model.MagicType;
import magic.model.event.MagicManaActivation;

public interface IRenderableCard {

    MagicManaCost getCost();
    String getText();
    Collection<MagicManaActivation> getManaActivations();
    EnumSet<MagicSubType> getSubTypes();
    int getColorFlags();
    int getNumColors();
    String getImageName();
    String getPowerToughnessText();
    String getName();
    String getSubTypeText();
    Set<MagicType> getTypes();
    boolean hasSubType(MagicSubType subType);
    boolean hasColor(MagicColor color);
    boolean isHybrid();
    boolean isMulti();
    boolean hasType(MagicType type);
    boolean isToken();
    boolean isFlipCard();
    boolean isDoubleFaced();
    boolean hasAbility(MagicAbility ability);
    boolean hasText();
    int getStartingLoyalty();
    boolean isPlaneswalker();
    boolean isCreature();
    boolean isLand();
    boolean isArtifact();
    boolean isSorcery();
    boolean isInstant();
    boolean isEnchantment();
    boolean isHidden();
}
