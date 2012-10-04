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
    boolean     isPlayer();
    String      getName();
    MagicPlayer getController();
    MagicCardDefinition getCardDefinition();
}
