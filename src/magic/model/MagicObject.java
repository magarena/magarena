package magic.model;

import magic.model.MagicCardDefinition;
import magic.model.MagicCopyable;
import magic.model.MagicMappable;
import magic.model.MagicPlayer;
import magic.model.MagicSource;

public interface MagicObject extends MagicCopyable, MagicMappable {
    boolean     isSpell();
    boolean     isPermanent();
    boolean     isCreature();
    boolean     isPlaneswalker();
    boolean     isPlayer();
    boolean     hasColor(final MagicColor color);
    boolean     hasAbility(final MagicAbility ability);
    boolean     hasType(final MagicType type);
    boolean     hasSubType(final MagicSubType subType);
    String      getName();
    MagicPlayer getController();
    MagicCardDefinition getCardDefinition();
}
